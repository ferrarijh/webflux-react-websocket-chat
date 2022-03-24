import {useState} from 'react';
import { useNavigate } from 'react-router-dom';
import { LoadingStatus as Status } from '../contexts/NetworkProvider';
import Resources from '../../Resources';
import Spinner from '../spinner/Spinner';
import './Join.css';

const baseUrl = "http://"+Resources.HOSTNAME +":"+Resources.PORT+"/chat/user/join";

const Join = () => {

    const navigate = useNavigate();
    const [status, setStatus] = useState(Status.IDLE);

    const handleSubmit = async e => {
        e.preventDefault();
        let username = e.currentTarget.username.value;
        let password = e.currentTarget.password.value;

        if(!username){
            alert("Please submit username.");
            return;
        }
        else if (!password){
            alert("Please submit password.");
            return;
        }

        setStatus(Status.LOADING);

        let response = await fetch(baseUrl, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        }).catch(error => {
            setStatus(Status.DISCONNECTED);
            console.log(error);
        });

        if(!response)
            return;

        let data = await response.json();
        if(response.ok){
            setStatus(Status.IDLE);
            console.log(data);
            alert(data.message);
            navigate(-1);
        }
        else if(response.status === 409){
            setStatus(Status.HTTP_409);
            alert(data.message);
        }else{
            alert("Unknown Exception: "+data);
        }
    }

    return (
        <div className="Join">
            <div className="JoinContainer">
                <p className="WelcomeGuide">Become a member</p>
                <div className="Status">
                    {status === Status.LOADING && <Spinner/>}
                    {status === Status.DISCONNECTED && <span><i>Can't connect with the server :(</i></span>}
                    {status === Status.HTTP_500 && <span><i>Internal Server Error :(</i></span>}
                </div>
                <form className="JoinForm" onSubmit={handleSubmit}>
                    <label>Username</label><br/>
                    <input type="text" className="Username" name='username' placeholder=" username"/><br/>
                    {status === Status.HTTP_409 && <div className="ConflictWarning"><i>Username already exists.</i></div>}
                    <br/>
                    <label>Password</label><br/>
                    <input type="password" className="Password" name='password' placeholder=" password"/><br/>
                    <br/>
                    <button type="submit" className="Button">Join</button>
                </form>
            </div>
            <div className="Space"/>
        </div>
    );
}

export default Join;