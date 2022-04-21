import { LoadingStatus as Status } from '../contexts/NetworkProvider';
import { useEffect, useState, useContext } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthProvider';
import { MessageType as Type } from '../chat/Message';
import Spinner from '../spinner/Spinner';
import './Login.css';
import Resources from '../../Resources';

const baseUrl = "http://" + Resources.HOSTNAME + ":" + Resources.PORT + "/chat/login";

const baseUrlTest = "http://"+Resources.HOSTNAME+":"+Resources.PORT+"/chat/user/signin";

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
                alert("Unknown Error: ", response);
                setStatus(Status.UNKNOWN);
                console.log(response);
        }
    };

    const onInOk = (data) => {
        setStatus(Status.IDLE);
        setUsername(data.username);
        setIsAuth(true);
    };

    const setAuthenticated = (username) => {
        setStatus(Status.IDLE);
        setUsername(username);
        setIsAuth(true);
    }

    const displayStatus = () => {
        switch(status){
            case Status.LOADING:
                return <Spinner/>;
            case Status.HTTP_500:
                return <span><i>Internal Server Error :(</i></span>;
            case Status.DISCONNECTED:
                return <span><i>Can't connect with the server :(</i></span>;
            case Status.UNKNOWN:
                return <span><i>Unknown Error :(</i></span>
        }
    }

    const handleSignInSubmit = async (e) => {
        e.preventDefault();
        let username = e.currentTarget.username.value;
        let password = e.currentTarget.password.value;
        if (username === "" || password === "") {
            alert("Username/Password can't be nothing!");
            return;
        }

        setStatus(Status.LOADING);

        let response = await fetch(baseUrlTest, {
            credentials: 'include',
            method: 'POST',
            headers: {
                'Content-Type': "application/json"
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        }).catch(error => {
            setStatus(Status.DISCONNECTED);
            alert("Error: " + error);
            console.log("Error: ", error);
        });

        if (!response)
            return;

        if (response.ok){
            setAuthenticated(username);
            return;
        }

        let errorData = await response.json();
        /* Error Cases */
        switch (response.status) {
            case 500:
                setStatus(Status.HTTP_500);
                break;
            default:
                alert("Unknown Error: "+errorData.message);
                setStatus(Status.UNKNOWN);
                console.log(errorData);
        }
    };

    return (
        <div className="Login">
            <div className="LoginContainer">
                <p className="WelcomeGuide">Mood to chat?</p>

                <div className="Status">{displayStatus()}</div>

                <form className="LoginForm" onSubmit={handleSubmit}>
                    <input type="text" className="Text" name='username' placeholder=" username" /><br />
                    <button type="submit" className="Button">Join Chat</button>
                </form>

                <Link to="../join"><div className="Link">Sign Up</div></Link>
                
                <form className="TestLoginForm" onSubmit={handleSignInSubmit}>
                    <input type="text" className="Username" name='username' placeholder=" username" /><br />
                    <input type="password" className="Password" name='password' placeholder=" password" /><br />
                    <button type="submit" className="Button">Sign in</button>
                </form>
            </div>
            <div className="Space" />
        </div>
    );
};

export default Login;