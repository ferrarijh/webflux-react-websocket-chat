export const USER_IN = "USER_IN"
export const USER_OUT = "USER_OUT"
export const MESSAGE = "MESSAGE"
export const USER_LIST = "USER_LIST"
export const IN_OK = "IN_OK"
export const IN_REQ = "IN_REQ"

const Message = (props) => {

    // const formatDate = () => {
    //     let date = props.message.date
    //     return date.slice(0, 10)+", "+date.slice(11, -5)
    // }

    switch(props.message.type){
        case USER_IN:
            return (
                <div className="userIn">
                    <p>{props.message.username} joined.</p>
                </div>
            )
        case MESSAGE:
            return (
                props.message.username !== props.username ?
                    <div className="messageOthers">
                        <p className="username">{props.message.username}</p>
                        <p className="content">{props.message.content}</p>
                        <p className="date">{props.message.date}</p>
                    </div> :
                    <div className="messageMe">
                        <p className="content">{props.message.content}</p>
                        <p className="date">{props.message.date}</p>
                    </div>
            )
        case USER_OUT:
            return (
                <div className="userOut">
                    <p>{props.message.username} left chat.</p>
                </div>
            )
        default:
            return(
                <div>default case. you shouldn't see this!</div>
            )
    }
}

export default Message