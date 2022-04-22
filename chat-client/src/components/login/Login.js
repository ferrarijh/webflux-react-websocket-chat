import { LoadingStatus as Status } from '../contexts/NetworkProvider';
import { useEffect, useState, useContext } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthProvider';
import Spinner from '../spinner/Spinner';
import './Login.css';
import Resources from '../../Resources';

const baseUrl = "http://" + Resources.HOSTNAME + ":" + Resources.PORT + "/chat";
const accessTokenUrl = baseUrl + "/user/signin";
const validateTokenUrl = baseUrl + "/validate-token";

const Login = () => {

    const navigate = useNavigate();
    const { isAuth, setIsAuth, setUsername } = useContext(AuthContext);
    const [status, setStatus] = useState(Status.IDLE);

    /* If already authenticated, navigate to Rooms component */
    useEffect(() => {
        if (isAuth)
            navigate("../rooms", { replace: true });
    }, [isAuth]);
    
    const setAuthenticated = (username) => {
        setStatus(Status.IDLE);
        setUsername(username);
        setIsAuth(true);
    }

    /* Check if the browser has access token */ 
    useEffect(async () => {
        console.log("useEffect() with empty dependencies called..");
        if(status === Status.LOADING){
            alert("Attempting to sign in... Please wait");
            return;
        }

        setStatus(Status.LOADING);
        
        let response = await fetch(validateTokenUrl, {
            credentials: "include",
            method: "GET"
        }).catch(error => {
            console.log(error);
            setStatus(Status.DISCONNECTED);
        });

        /* Cors error or disconnection */
        if(!response)
            return;

        if(response.ok){
            let data = await response.json();
            setAuthenticated(data.username);
            return;
        }

        switch (response.status) {
            case 401:
                setStatus(Status.HTTP_401);
                break;
            case 404:
                setStatus(Status.HTTP_404);
                break;
            case 500:
                setStatus(Status.HTTP_500);
                break;
            default:
                setStatus(Status.UNKNOWN);
        }
    }, [])

    const displayStatus = () => {
        switch (status) {
            case Status.LOADING:
                return <Spinner />;
            case Status.HTTP_401:
                return <></>;
            case Status.HTTP_404:
                return <span><i>Username not found. Please try again, or sign up!</i></span>
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

        if(status === Status.LOADING){
            alert("Attempting to sign in... Please wait");
            return;
        }

        let username = e.currentTarget.username.value;
        let password = e.currentTarget.password.value;
        if (username === "" || password === "") {
            alert("Please fill in both username and password.");
            return;
        }

        setStatus(Status.LOADING);

        let response = await fetch(accessTokenUrl, {
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

        if (response.ok) {
            setAuthenticated(username);
            return;
        }

        let errorData = await response.json();

        /* Error Cases */
        switch (response.status) {
            case 404:
                setStatus(Status.HTTP_404);
                break;
            case 500:
                setStatus(Status.HTTP_500);
                break;
            default:
                alert("Unknown Error: " + errorData.message);
                setStatus(Status.UNKNOWN);
                console.log(errorData);
        }
    };

    return (
        <div className="Login">
            <div className="LoginContainer">
                <p className="WelcomeGuide">Mood to chat?</p>

                <div className="Status">{displayStatus()}</div>

                <form className="LoginForm" onSubmit={handleSignInSubmit}>
                    <input type="text" className="Username" name='username' placeholder=" username" /><br />
                    <input type="password" className="Password" name='password' placeholder=" password" /><br />
                    <button type="submit" className="Button">Sign in</button>
                </form>

                <Link to="../join"><div className="Link">Sign Up</div></Link>
            </div>
            <div className="Space" />
        </div>
    );
};

export default Login;