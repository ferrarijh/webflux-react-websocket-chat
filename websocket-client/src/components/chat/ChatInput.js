import {useState} from 'react'
import * as Type from './Message';

const ChatInput = (props) => {

    const [input, setInput] = useState("")

    const handleKeypress = e => {
        if(e.which === 13 && !e.shiftKey && input !== ""){
            submitInput(e)
        }
    }

    const handleChange = e => {
        if(input === "" && e.target.value === "\n")
            e.target.value = ""
        setInput(e.target.value)
    }

    const handleSubmit = e => {
        e.preventDefault();
        if(input === "")
            return
        submitInput(e)
    }

    const submitInput = e => {
        let jsonMessage = JSON.stringify({
            username: props.username,
            content: input,
            date: new Date(),
            type: Type.MESSAGE
        })
        e.target.value = ""
        e.preventDefault()
        setInput("")
        props.socket.send(jsonMessage)
    }

    return (
        <div className="ChatInput">
            <form onSubmit={handleSubmit}>
                <textarea type="text" name="message" className="input" onChange={handleChange} onKeyPress={handleKeypress}/>
                <button type="submit" className="button">Send</button>
            </form>
        </div>
    )
}

export default ChatInput