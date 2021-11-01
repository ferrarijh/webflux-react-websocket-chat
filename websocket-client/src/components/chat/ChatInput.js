import React from 'react'

const ChatInput = (props) => {

    const handleSubmit = (e) => {
        e.preventDefault();

        let jsonMessage = JSON.stringify({
            username: props.username,
            content: e.target.value,
            date: Date.now()
        })
        e.target.reset();
        props.socket.send(jsonMessage)
        props.setInput("")
    }

    return (
        <div className="ChatInput">
            <form onSubmit={handleSubmit}>
            <input type="text"/>
            <button type="submit" className="Button">Send</button>
            </form>
        </div>
    )
}

export default ChatInput