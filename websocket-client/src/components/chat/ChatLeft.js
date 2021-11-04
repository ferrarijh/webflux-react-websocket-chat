import React from 'react'
import ChatMessages from './ChatMessages'
import ChatInput from './ChatInput'

const ChatLeft = (props) => {
    return (
        <div className="ChatLeft">
            <ChatMessages username={props.username} chatMessages={props.chatMessages} />
            <ChatInput username={props.username} socket={props.socket}/>
        </div>
    )
}

export default ChatLeft
