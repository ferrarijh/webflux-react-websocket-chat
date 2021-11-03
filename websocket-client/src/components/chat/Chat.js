import {useState, useEffect} from 'react'
import ChatLeft from './ChatLeft'
import UserList from './UserList'
import Header from '../Header'
import './Chat.css'
import * as Type from './Message'

const Chat = (props) => {

  const [chatMessages, setChatMessages] = useState([])
  const [socket, setSocket] = useState(null)
  
  useEffect(()=>{

    if (socket !== null)
      return

    var ws = new WebSocket("ws://localhost:8080/chat")
      
    ws.onopen = e => {
      setSocket(ws);
      let userInMessage = JSON.stringify({
        username: props.username,
        content: "I'm in!",
        date: new Date(),
        type: Type.USER_IN
      })
      ws.send(userInMessage)
    }

    ws.onmessage = e => {
      let msg = JSON.parse(e.data)
      if(msg.type === Type.USER_IN){
        props.setUsers(prevUsers => [...prevUsers, msg.username])
        setChatMessages(prev => [...prev, msg.username+" joined."])
      }
      else if(msg.type === Type.MESSAGE){
        let message = msg.username + ":" + msg.content
        setChatMessages(prevMessages => [...prevMessages, message])
      }
    }

    ws.onclose = e => {
      console.log(e)
      props.setIsAuth(false);
    }
  
    ws.onerror = err => console.log("error: ", err.message)
    
  })

  return (
      <div className="Chat">
        <Header/>
        <div className="ChatBody">
          <ChatLeft username={props.username} chatMessages={chatMessages} socket={socket}/>
          <UserList users={props.users}/>
        </div>
      </div>
  );
}

export default Chat;
