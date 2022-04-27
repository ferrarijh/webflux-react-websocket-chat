import {useState, useEffect, useRef, useContext} from 'react';
import { AuthContext } from '../contexts/AuthProvider';
import {MessageType as Type} from './Message';

const ChatInput = (props) => {

    const {username} = useContext(AuthContext)
    const [input, setInput] = useState("");
    const textInputRef = useRef(null);

    useEffect(()=>{
        textInputRef.current.focus();
    }, []);

    const handleKeypress = e => {
        if(e.which === 13 && !e.shiftKey && input !== ""){
            e.preventDefault();
            submitInput();
            e.target.value = "";
            setInput("");
        }
    };

    const handleChange = e => {
        if(input === "" && e.target.value === "\n")
            e.target.value = "";
        setInput(e.target.value);
    };

    const handleClick = e => {
        e.preventDefault();
        if(input === "")
            return;

        submitInput();
        textInputRef.current.value = "";
        setInput("");
    };

    const submitInput = () => {
        let jsonMessage = JSON.stringify({
            username: username,
            content: input,
            date: new Date(),
            type: Type.MESSAGE
        });
        props.socket.send(jsonMessage);
    };

    return (
        <div className="ChatInput">
            <form onSubmit={handleClick}>
                <textarea ref={textInputRef} type="text" name="message" onChange={handleChange} onKeyPress={handleKeypress}/>
                <button type="submit">Send</button>
            </form>
        </div>
    );
};

export default ChatInput;