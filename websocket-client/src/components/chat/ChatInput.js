import React from 'react'

const ChatInput = (props) => {

    const handleSubmit = (e) => {
        e.preventDefault();
        e.target.reset();
        props.socket.send(props.input)
        props.setInput("")
    }

    return (
        <div className="ChatInput">
            <form onSubmit={handleSubmit}>
            <input type="text" onChange={e=>props.setInput(e.target.value)}/>
            <button type="submit" className="Button">Send</button>
            </form>
        </div>
    )
}

export default ChatInput