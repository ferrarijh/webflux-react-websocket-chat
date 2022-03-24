import { LoadingStatus as Status } from '../contexts/NetworkProvider';
import { useEffect, useState, useContext } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthProvider';
import { MessageType as Type } from '../chat/Message';
import Spinner from '../spinner/Spinner';
import './Login.css';
import Resources from '../../Resources';

const baseUrl = "http://" + Resources.HOSTNAME + ":" + Resources.PORT + "/chat/login";

const Login = () => {

    const navigate = useNavigate();
    const { isAuth, setIsAuth, setUsername } = useContext(AuthContext);
    const [status, setStatus] = useState(Status.IDLE);

    useEffect(() => {
        if (isAuth)
            navigate("../rooms", { replace: true });
    }, [isAuth]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        let newUsername = e.currentTarget.username.value;
        if (newUsername === "") {
            alert("Username can't be nothing!");
            return;
        }

        setStatus(Status.LOADING);

        let now = new Date().toISOString();
        let response = await fetch(baseUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: newUsername,
                date: now,
                type: Type.IN_REQ
            })
        }).catch(error => {
            setStatus(Status.DISCONNECTED);
            alert("Error: " + error);
            console.log("Error: ", error);
        });

        if (!response)
            return;

        let data = await response;
        if (response.ok && data.type == Type.IN_OK)
            onInOk(data);

        switch (response.status) {
            case 500:
                setStatus(Status.HTTP_500);
                break;
            default:
                alert("Unknown Error: ", JSON.stringify(data));
        }
    };

    const onInOk = (data) => {
        setStatus(Status.IDLE);
        setUsername(data.username);
        setIsAuth(true);
    };

    return (
        <div className="Login">
            <div className="LoginContainer">
                <p className="WelcomeGuide">Mood to chat?</p>
                <div className="Status">
                    {status === Status.LOADING && <Spinner />}
                    {status === Status.HTTP_500 && <span><i>Internal Server Error :(</i></span>}
                    {status === Status.DISCONNECTED && <span><i>Can't connect with the server :(</i></span>}
                </div>
                <form className="LoginForm" onSubmit={handleSubmit}>
                    <input type="text" className="Text" name='username' placeholder=" username" /><br />
                    <button type="submit" className="Button">Join Chat</button>
                </form>
                <Link to="../join"><div className="Link">Sign Up</div></Link>
                
                <form className="TestLoginForm" onSubmit={handleSubmit}>
                    <input type="text" className="Usenrame" name='username' placeholder=" username" /><br />
                    <input type="password" className="Password" name='password' placeholder=" username" /><br />
                    <button type="submit" className="Button">Sign in</button>
                </form>
            </div>
            <div className="Space" />
        </div>
    );
};

export default Login;