import {useState, useEffect} from 'react'
import ChatLeft from './ChatLeft'
import UserList from './UserList'
import Header from '../Header'
import './Chat.css'

function Chat() {

  const [users, setUsers] = useState([])
  const [chatMessages, setChatMessages] = useState([])
  const [input, setInput] = useState("")
  const [socket, _] = useState(new WebSocket("ws://localhost:8080/chat"))
  
  useEffect(()=>{
    socket.onopen = e => console.log("Opened connection!")
    socket.onmessage = onOpen
    socket.onclose = e => console.log("Closed connection.")
    socket.onerror = err => console.log("error: ", err.message)

  }, [socket])

  const onOpen = (e) => {
    console.log("received: ", e.data)
    setChatMessages((prevMessages) => [...prevMessages, e.data])
  }

  return (
    <div className="Chat">
      <Header/>
      <div className="ChatBody">
      <ChatLeft setInput={setInput} input={input} chatMessages={chatMessages} socket={socket}/>
      <UserList users={users}/>
      </div>
    </div>
  );
}

export default Chat;
