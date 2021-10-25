import {useState} from 'react'
import Chat from './components/Chat'
import UserList from './components/UserList'
import './App.css'

function App() {

  const [users, setUsers] = useState([])
  const [chatLog, setChatLog] = useState([])
  const [message, setMessage] = useState("")

  const socket = new WebSocket("ws://localhost:8080/chat")
  
  socket.onopen = (e) => {
    console.log("Opened connection!")
  }
  socket.onmessage = (ev) => {
    console.log("received: ", ev.data)
    let received = JSON.parse(ev.data)
    // if(received.content === undefined)
    //   setUsers(received.users)
    // else
    //   setChatLog((prev) => [...prev, JSON.parse(received.message)])
    setChatLog((prev) => [...prev, ev.data])
  }
  socket.onclose = (e) => {
    console.log("Closed connection.")
  }
  socket.onerror = (err) => {
    console.log("error: ", err.message)
  }

  const handleSubmit = e => {
    e.preventDefault();

    socket.send(message);
    console.log("sent: ", message);
  }

  return (
    <div className="App">
      {/* <ChatInput onSubmit={handleClick} input={message} onChange={setMessage}/> */}
      <Chat handleSubmit={handleSubmit} setMessage={setMessage} chatLog={chatLog}/>
      <UserList users={users}/>
    </div>
  );
}

export default App;
