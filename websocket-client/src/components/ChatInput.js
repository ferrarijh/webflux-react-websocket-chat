import React from 'react'

const ChatInput = (props) => {

    const handleSubmit = (e) => {
        e.preventDefault();
        props.socket.send(props.input)
        props.setInput("")
    }

    return (
        <div className="ChatInput">
            <form onSubmit={handleSubmit}>
            <input type="text" onChange={e=>props.setInput(e.target.value)}/>
            <input type="submit" className="Button" value="Send"/>
            </form>
        </div>
    )
}

export default ChatInput