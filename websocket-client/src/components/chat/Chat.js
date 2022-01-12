import {useState, useEffect} from 'react'
import ChatLeft from './ChatLeft'
import UserList from './UserList'
import Header from './Header'
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
        content: "",
        date: new Date(),
        type: Type.USER_IN
      })
      ws.send(userInMessage)
    }

    ws.onmessage = e => {
      let msg = JSON.parse(e.data)
      msg.date = new Date(msg.date).toLocaleString()
      if(msg.type === Type.USER_IN){
        props.setUsers(prevUsers => [...prevUsers, msg.username])
        setChatMessages(prev => [...prev, msg])
      }
      else if(msg.type === Type.MESSAGE){
        setChatMessages(prev => [...prev, msg])
      }
      else if(msg.type === Type.USER_OUT){
        props.setUsers(prevUsers => prevUsers.filter(u => u !== msg.username))
        setChatMessages(prev => [...prev, msg])
      }
    }

    ws.onclose = e => {
      console.log(e)
      props.setIsAuth(false);
    }
  
    ws.onerror = err => console.log("error: ", err.message)
    
    // const cleanup = () => { ws.close() }
    // window.addEventListener('beforeTabClose', cleanup)
    // return ()=>{ window.removeEventListener('beforeTabClose', cleanup) }
  })

  return (
      <div className="Chat">
        <Header/>
        <div className="ChatBody">
          <ChatLeft username={props.username} chatMessages={chatMessages} socket={socket}/>
          <UserList username={props.username} users={props.users}/>
        </div>
      </div>
  );
}

export default Chat;
