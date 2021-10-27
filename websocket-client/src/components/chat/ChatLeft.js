import React from 'react'
import ChatMessages from './ChatMessages'
import ChatInput from './ChatInput'

const ChatLeft = (props) => {
    return (
        <div className="ChatLeft">
            <ChatMessages chatMessages={props.chatMessages} messageKey={props.messageKey}/>
            <ChatInput setInput={props.setInput} input={props.input} socket={props.socket}/>
        </div>
    )
}

export default ChatLeft
