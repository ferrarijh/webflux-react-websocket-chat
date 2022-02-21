import { LoadingStatus } from '../contexts/NetworkProvider';
import { useEffect, useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthProvider';
import { MessageType as Type } from '../chat/Message';
import Spinner from '../spinner/Spinner';
import './Login.css';
import Resources from '../../Resources';

const baseUrl = "http://"+Resources.HOSTNAME+":"+Resources.PORT+"/chat/login";

const Login = () => {

    const navigate = useNavigate();
    const {isAuth, setIsAuth, setUsername} = useContext(AuthContext);
    const [status, setStatus] = useState(LoadingStatus.IDLE);

    useEffect(() => {
        if(isAuth)
            navigate("../rooms", {replace: true});
    }, [isAuth]);

    const handleSubmit = e => {
        e.preventDefault();
        let newUsername = e.currentTarget.username.value;
        if(newUsername === "")
            return;

        setStatus(LoadingStatus.LOADING);

        let now = new Date().toISOString();

        fetch(baseUrl, {
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
            <div className="LoginContainer">
                <p className="WelcomeGuide">Mood to chat?</p>
                <div className="Status">
                    {status === LoadingStatus.LOADING && <Spinner/>}
                    {status === LoadingStatus.ERROR && <span><i>Can't connect with the server :(</i></span>}
                </div>
                <form className="LoginForm" onSubmit={handleSubmit}>
                    <input type="text" className="Text" name='username' placeholder=" username"/><br/>
                    <button type="submit" className="Button">Join Chat</button>
                </form>
            </div>
            <div className="Space"/>
        </div>
    );
};

export default Login;