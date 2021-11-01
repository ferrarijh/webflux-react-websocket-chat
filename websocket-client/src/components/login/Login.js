import React from 'react'
import * as Message from '../chat/Message'
import './Login.css'
import {Redirect} from 'react-router-dom'

const Login = (props) => {

    const handleSubmit = e => {
        e.preventDefault()
        let usernameSubmit = e.target.value
        console.log("new Date().toISOString(): ", new Date().toISOString())

        fetch("http://localhost:8080/login", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: usernameSubmit,
                content: "",
                date: new Date().toISOString(),
                type: Message.USER_IN,
            })
        }).then(response => response.json())
        .then((msg)=>{
            if(msg.type === Message.IN_OK)
                onLogin(usernameSubmit)
            else
                console.log('Login rejected...')
        }).catch(error => console.log('Error: ', error))
    }

    const onLogin = (username) => {
        props.setIsAuth(true)
        props.setUsername(username)
    }

    return (
        <div className="Login">
        {props.isAuth ?
            <Redirect to="/chat"/> : 
            <form onSubmit={handleSubmit}>
                <div>
                    <span>username:</span><br/>
                    <input type="text"/>
                    <button type="submit">Join Chat</button>
                </div>
            </form>
        }
        </div>
    )
}

export default Login
