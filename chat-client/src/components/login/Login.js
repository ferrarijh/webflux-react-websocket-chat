import { LoadingStatus } from '../contexts/NetworkProvider';
import { useEffect, useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthProvider';
import { MessageType as Type } from '../chat/Message';
import Spinner from '../spinner/Spinner';
import './Login.css';

const Login = () => {

    const navigate = useNavigate();
    const {isAuth, setIsAuth, setUsername} = useContext(AuthContext);
    const [status, setStatus] = useState(LoadingStatus.IDLE);

    useEffect(() => {
        if(isAuth)
            navigate("../chat")
    }, [isAuth]);

    const handleSubmit = e => {
        e.preventDefault();
        let newUsername = e.currentTarget.username.value;
        if(newUsername === "")
            return;

        setStatus(LoadingStatus.LOADING);

        let now = new Date().toISOString();

        fetch("http://localhost:8080/login", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: newUsername,
                date: now,
                type: Type.IN_REQ
            })
        }).then(response => response.json())
        .then(data => {
            console.log("data: ", data);
            if(data.type === Type.IN_OK)
                onInOk(data);
            else
                console.log("Login rejected... Response type is "+data.type);
        }).catch(error => {
            setStatus(LoadingStatus.ERROR);
            alert("Error: "+error);
            console.log("Error: ", error);
        });
    };

    const onInOk = (data) => {
        setStatus(LoadingStatus.IDLE);
        setUsername(data.username);
        setIsAuth(true);
    };

    return (
        <div className="Login">
            <div className="loginContainer">
                <p className="welcome">Mood to chat?</p>
                <div className="status">
                    {status === LoadingStatus.LOADING && <Spinner/>}
                    {status === LoadingStatus.ERROR && <span>something went wrong :(</span>}
                </div>
                <form className="loginForm" onSubmit={handleSubmit}>
                    <input type="text" className="inputText" name='username' placeholder=" username"/><br/>
                    <button type="submit" className="inputButton">Join Chat</button>
                </form>
            </div>
            <div className="space"/>
        </div>
    );
};

export default Login;