import React from 'react'

const ChatInput = (props) => {

    return (
        <div className="ChatInput">
            <form onSubmit={props.handleSubmit}>
            <input type="text" onChange={e=>props.setMessage(e.target.value)}/>
            <input type="submit" className="Button" value="Send"/>
            </form>
        </div>
    )
}

export default ChatInput