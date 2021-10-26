import React from 'react'
import ChatMessages from './ChatMessages'
import ChatInput from './ChatInput'

const Chat = (props) => {
    return (
        <div className="Chat">
            <ChatMessages chatMessages={props.chatMessages} messageKey={props.messageKey}/>
            <ChatInput setInput={props.setInput} input={props.input} socket={props.socket}/>
        </div>
    )
}

export default Chat
