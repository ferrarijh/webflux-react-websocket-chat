import {LOADING, SUCCESS, ERROR} from '../../App'
import Spinner from '../spinner/Spinner'
import * as Message from '../chat/Message'
import './Login.css'

const Login = (props) => {

    const handleSubmit = e => {
        e.preventDefault()
        let usernameSubmit = e.currentTarget.username.value
        if(usernameSubmit === "")
            return

        props.setNetstat(LOADING)

        let now = new Date().toISOString()

        fetch("http://localhost:8080/login", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: usernameSubmit,
                content: "",
                date: now,
                type: Message.IN_REQ
            })
        }).then(response => response.json())
        .then(data => {
            console.log("data: ", data)
            if(data.type === Message.IN_OK)
                onInOk(data)
            else
                console.log('Login rejected... Response type is '+data.type)
        }).catch(error => {
            props.setNetstat(ERROR)
            console.log('Error: ', error)
        })
    }

    const onInOk = async (data) => {
        props.setNetstat(SUCCESS)
        props.setUsername(data.username)
        props.setUsers(data.userList)
        props.setIsAuth(true)
    }

    return (
        <div className="Login">
            <div className="loginContainer">
                <p className="welcome">Mood to chat?</p>
                {/* <Spinner/> */}
                <div className="status">
                    {props.netstat === LOADING && <Spinner/>}
                    {props.netstat === ERROR && <span>something went wrong :(</span>}
                </div>
                <form className="loginForm" onSubmit={handleSubmit}>
                    <input type="text" className="inputText" name='username' placeholder=" username"/><br/>
                    <button type="submit" className="inputButton">Join Chat</button>
                </form>
            </div>
            <div className="space"/>
        </div>
    )
}

export default Login
