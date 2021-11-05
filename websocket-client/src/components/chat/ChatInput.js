import {useState, useEffect, useRef} from 'react'
import * as Type from './Message';

const ChatInput = (props) => {

    const [input, setInput] = useState("")
    const textInput = useRef(null)

    useEffect(()=>{
        textInput.current.focus()
    }, [])

    const handleKeypress = e => {
        if(e.which === 13 && !e.shiftKey && input !== ""){
            e.preventDefault()
            submitInput()
            e.target.value = ""
            setInput("")
        }
    }

    const handleChange = e => {
        if(input === "" && e.target.value === "\n")
            e.target.value = ""
        setInput(e.target.value)
    }

    const handleClick = e => {
        e.preventDefault();
        if(input === "")
            return

        submitInput()
        textInput.current.value = ""
        setInput("")
    }

    const submitInput = () => {
        let jsonMessage = JSON.stringify({
            username: props.username,
            content: input,
            date: new Date(),
            type: Type.MESSAGE
        })
        props.socket.send(jsonMessage)
    }

    return (
        <div className="ChatInput">
            <form onSubmit={handleClick}>
                <textarea ref={textInput} type="text" name="message" className="input" onChange={handleChange} onKeyPress={handleKeypress}/>
                <button type="submit" className="button">Send</button>
            </form>
        </div>
    )
}

export default ChatInput