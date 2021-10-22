import {useState} from 'react'

function App() {

  const socket = new WebSocket("ws://localhost:8080/chat")
  socket.onopen = (e) => {
    console.log("Opened connection!")
  }
  socket.onmessage = (ev) => {
    console.log("received: ", ev.data)
  }
  socket.onclose = (e) => {
    console.log("Closed connection.")
  }
  socket.onerror = (err) => {
    console.log("error: ", err.message)
  }

  const [message, setMessage] = useState("")

  const handleClick = (e) => {
    e.preventDefault();

    socket.send(message);
    console.log("sent: ",message);
  }

  return (
    <div className="App">
      <form onSubmit={handleClick}>
        <input type="text" onChange={e=>setMessage(e.target.value)}/>
        <br/>
        <input type="submit"/>
      </form>
    </div>
  );
}

export default App;
