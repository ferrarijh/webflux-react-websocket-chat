import {useState, useEffect} from 'react'
import Chat from './components/Chat'
import UserList from './components/UserList'
import './App.css'

function App() {

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
    <div className="App">
      <Chat setInput={setInput} input={input} chatMessages={chatMessages} socket={socket}/>
      <UserList users={users}/>
    </div>
  );
}

export default App;
