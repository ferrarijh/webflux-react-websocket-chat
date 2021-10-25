import React from 'react'
import Message from './Message'

const ChatLog = (props) => {
    return(
        <div className="ChatLog">
            {props.chatLog.map((msg) => 
                // <Message key={msg.time} id={msg.id} content={msg.content}/>
                <Message message={msg}/>
            )}
        </div>
    )
}

export default ChatLog