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

    // const formatDate = () => {
    //     let date = props.message.date
    //     return date.slice(0, 10)+", "+date.slice(11, -5)
    // }

    const renderMessage = (type) => {
        switch(type){
            case MessageType.USER_IN:
                return <div className="UserIn">{props.message.username} joined.</div>;
            case MessageType.MESSAGE:
                if (props.message.username === props.username)
                    return (
                        <div className="MyMessage">
                            <p className="content">{props.message.content}</p>
                            <p className="date">{props.message.date}</p>
                        </div>
                    );
                else
                    return (
                        <div className="OthersMessage">
                            <p className="username">{props.message.username}</p>
                            <p className="content">{props.message.content}</p>
                            <p className="date">{props.message.date}</p>
                        </div>
                    );
            case MessageType.USER_OUT:
                return (
                    <div className="UserOut">
                        <p>{props.message.username} left chat.</p>
                    </div>
                );
            default:
                return <div>Default case. You shouldn't see this!</div>;
        }
    };

    return (
        <div className="Message">
            {renderMessage(props.message.type)}
        </div>
    );

    
}

export default Message;
export {MessageType};