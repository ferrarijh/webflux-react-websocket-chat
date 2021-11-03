import * as Type from './Message';

const ChatInput = (props) => {

    const handleSubmit = (e) => {
        e.preventDefault();

        let input = e.currentTarget.message.value

        let jsonMessage = JSON.stringify({
            username: props.username,
            content: input,
            date: new Date(),
            type: Type.MESSAGE
        })
        e.target.reset();
        props.socket.send(jsonMessage)
    }

    return (
        <div className="ChatInput">
            <form onSubmit={handleSubmit}>
            <input type="text" name='message'/>
            <button type="submit" className="Button">Send</button>
            </form>
        </div>
    )
}

export default ChatInput