import { useState, useEffect, useContext } from 'react';
import './Chat.css';
import ChatInput from './ChatInput';
import Message, { MessageType as Type } from './Message';
import { AuthContext } from '../contexts/AuthProvider';
import { useNavigate } from 'react-router-dom';

const Chat = (props) => {

  const navigate = useNavigate();
  const { username, isAuth, setIsAuth } = useContext(AuthContext);
  const [users, setUsers] = useState([]);
  const [messages, setMessages] = useState([]);
  const [socket, setSocket] = useState(null);

  useEffect(() => {
    if (!isAuth)
      navigate("../login");

    return () => {
      if (socket === null)
        return;

      socket.close();
      setSocket(null);
    }
  }, [isAuth]);

  useEffect(() => {
    if (socket !== null)
      return;

    setUsers(prevUsers => [username, ...prevUsers]);

    var newSocket = new WebSocket("ws://localhost:8080/chat");

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

      console.log("onmessage: "+JSON.stringify(msg));

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
      }
    };

    newSocket.onclose = e => {
      console.log(e);
      setIsAuth(false);
    };

    newSocket.onerror = err => console.log("error: ", err.message);

    // const cleanup = () => { ws.close() }
    // window.addEventListener('beforeTabClose', cleanup)
    // return ()=>{ window.removeEventListener('beforeTabClose', cleanup) }
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
