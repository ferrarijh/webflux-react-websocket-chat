import * as Message from '../chat/Message'
import './Login.css'

const Login = (props) => {

    const handleSubmit = e => {
        e.preventDefault()
        let usernameSubmit = e.currentTarget.username.value
        let now = new Date().toISOString()

        fetch("http://localhost:8080/login", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: usernameSubmit,
                content: "incoming with normal http...",
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
        }).catch(error => console.log('Error: ', error))
    }

    const onInOk = async (data) => {
        props.setUsername(data.username)
        props.setUsers(data.userList)
        props.setIsAuth(true)
    }

    return (
        <div className="Login">
            <form onSubmit={handleSubmit}>
                <span>username:</span><br/>
                <input type="text" name='username'/>
                <button type="submit">Join Chat</button>
            </form>
        </div>
    )
}

export default Login
