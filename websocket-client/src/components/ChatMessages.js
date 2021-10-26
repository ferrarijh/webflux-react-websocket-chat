import React from 'react'
import Message from './Message'

const ChatMessages = (props) => {
    return(
        <div className="ChatMessages">
            {props.chatMessages.map((msg, index) => 
                // <Message key={msg.time} id={msg.id} content={msg.content}/>
                <Message message={msg} key={index}/>
            )}
        </div>
    )
}

export default ChatMessages