import {useState, useEffect} from 'react'
import {Redirect} from 'react-router-dom'
import ChatLeft from './ChatLeft'
import UserList from './UserList'
import Header from '../Header'
import './Chat.css'
import * as Type from './Message'

const Chat = (props) => {

  console.log("rendering Chat...")

  const [users, setUsers] = useState([props.username])
  const [chatMessages, setChatMessages] = useState([])
  const [socket, _] = useState(new WebSocket("ws://localhost:8080/chat"))
  
  useEffect(()=>{
    socket.onopen = onOpen
    socket.onmessage = onMessage
    socket.onclose = onClose
    socket.onerror = err => console.log("error: ", err.message)

  }, [socket])

  const onOpen = (e) => {
    console.log(e)

    let userInMessage = JSON.stringify({
      username: props.username,
      content: "",
      date: Date.now(),
      type: Type.USER_IN
    })
    socket.send(userInMessage)
  }

  const onMessage = (e) => {
    console.log("received: ", e.data)
    let messageObj = JSON.parse(e.data)
    if (users.find(user => user === messageObj.username) === undefined)
      setUsers(users => [...users, messageObj.username])
    
    setChatMessages((prevMessages) => [...prevMessages, e.data])
  }

  const onClose = (e) => {
    console.log(e)
    props.setIsAuth(false);
  }

  return (
    <>
    {props.isAuth === false ? 
      <Redirect to="/login"/> : 
      <div className="Chat">
        <Header/>
        <div className="ChatBody">
          <ChatLeft chatMessages={chatMessages} socket={socket}/>
          <UserList users={users}/>
        </div>
      </div>
    }
    </>
    
  );
}

export default Chat;
