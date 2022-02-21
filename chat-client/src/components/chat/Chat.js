import { useState, useEffect, useContext } from 'react';
import './Chat.css';
import ChatInput from './ChatInput';
import Message, { MessageType as Type } from './Message';
import { AuthContext } from '../contexts/AuthProvider';
import { useNavigate, useParams } from 'react-router-dom';
import Resources from '../../Resources';

const chatBaseUrl = "ws://" + Resources.HOSTNAME + ":" + Resources.PORT + "/chat/room/chat";

const Chat = (props) => {

  const { roomId } = useParams();
  const navigate = useNavigate();
  const { username, isAuth, setIsAuth } = useContext(AuthContext);
  const [users, setUsers] = useState([]);
  const [messages, setMessages] = useState([]);
  const [socket, setSocket] = useState(null);

  useEffect(() => {
    if (!isAuth)
      navigate("../login", { replace: true });
  }, [isAuth]);

  useEffect(() => {
    if (socket !== null)
      return;

    setUsers(prevUsers => [username, ...prevUsers]);

    var newSocket = new WebSocket(chatBaseUrl + "/" + roomId);

    newSocket.onopen = e => {
      setSocket(newSocket);
      let userInMessage = JSON.stringify({
        type: Type.USER_IN,
        username: username,
        date: new Date()
      });
      console.log(userInMessage)
      newSocket.send(userInMessage);
    };

    newSocket.onmessage = e => {
      let msg = JSON.parse(e.data);
      msg.date = new Date(msg.date).toLocaleString();

      console.log("onmessage: " + JSON.stringify(msg));

      switch (msg.type) {
        case Type.INIT:
          setUsers(prev => [...prev, msg.user_list]);
          break;
        case Type.USER_IN:
          if (msg.username === username)
            return;
          setUsers(prevUsers => [...prevUsers, msg.username]);
          setMessages(prev => [...prev, msg]);
          break;
        case Type.MESSAGE:
          setMessages(prev => [...prev, msg]);
          break;
        case Type.USER_OUT:
          setUsers(prevUsers => prevUsers.filter(u => u !== msg.username));
          setMessages(prev => [...prev, msg]);
          break;
        default:
          console.log("Default case for incoming message. You shouldn't see this!");
      }
    };

    newSocket.onclose = e => {
      console.log(e);
      setIsAuth(false);
    };

    newSocket.onerror = err => console.log("error: ", err.message);

    let cleanup = () => {
      console.log("Chat cleanup..");
      if (newSocket === null)
        return;

      newSocket.close();
      setSocket(null);
    };

    return cleanup;
  }, []);

  return (
    <div className="Chat">
      <div className="Header">Spread Love</div>
      <div className="Body">
        <div className="LeftContainer">
          <div className="MessageList">
            {messages.map((msg, index) =>
              <Message username={username} message={msg} key={index} />
            )}
          </div>
          <ChatInput username={username} socket={socket} />
        </div>
        <div className="UserList">
          {users.map((user, index) =>
            <div className="User" key={index}>
              {user}{username === user && <span className="Me"> (me)</span>}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Chat;
