import React from 'react'
import {Link} from 'react-router-dom'
import './Login.css'

const Login = () => {
    return (
        <div className="Login">
            <form onSubmit={()=>{}}>
                <div>
                    <span>username:</span><br/>
                    <input type="text"/>
                </div>
            </form>
            <Link to="/chat">Join Chat</Link>
        </div>
    )
}

export default Login
