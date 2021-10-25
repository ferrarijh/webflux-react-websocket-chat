import React from 'react'
import ChatLog from './ChatLog'
import ChatInput from './ChatInput'

const Chat = (props) => {
    return (
        <div className="Chat">
            <ChatLog chatLog={props.chatLog}/>
            <ChatInput handleSubmit={props.handleSubmit} setMessage={props.setMessage}/>
        </div>
    )
}

export default Chat
