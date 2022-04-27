import './Message.css';

const MessageType = Object.freeze({
    INIT: "INIT",
    USER_IN: "USER_IN",
    USER_OUT: "USER_OUT",
    MESSAGE: "MESSAGE",
    USER_LIST: "USER_LIST",
    IN_OK: "IN_OK",
    IN_REQ: "IN_REQ"
});

const Message = (props) => {

    const renderMessage = () => {
        switch (props.message.type) {
            case MessageType.USER_IN:
                return <div className="UserIn">{props.message.username} joined.</div>;
            case MessageType.MESSAGE:
                if (props.message.username === props.username)
                    return (
                        <div className="MyMessage">
                            <p className="Content">{props.message.content}</p>
                            <p className="Date">{props.message.date}</p>
                        </div>
                    );
                else
                    return (
                        <div className="OthersMessage">
                            <p className="Username">{props.message.username}</p>
                            <p className="Content">{props.message.content}</p>
                            <p className="Date">{props.message.date}</p>
                        </div>
                    );
            case MessageType.USER_OUT:
                return <div className="UserOut">{props.message.username} left chat.</div>;
            default:
                return <div>Default case. You shouldn't see this!</div>;
        }
    };

    return (
        <div className="Message">
            {renderMessage()}
        </div>
    );
}

export default Message;
export { MessageType };