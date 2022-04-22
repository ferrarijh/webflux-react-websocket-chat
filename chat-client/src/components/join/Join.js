import {useState} from 'react';
import { useNavigate } from 'react-router-dom';
import { LoadingStatus as Status } from '../contexts/NetworkProvider';
import Resources from '../../Resources';
import Spinner from '../spinner/Spinner';
import './Join.css';

const joinUrl = "http://"+Resources.HOSTNAME +":"+Resources.PORT+"/chat/user/join";

const Join = () => {

    const navigate = useNavigate();
    const [status, setStatus] = useState(Status.IDLE);

    const handleSubmit = async e => {
        e.preventDefault();
        let username = e.currentTarget.username.value;
        let password = e.currentTarget.password.value;

        if(!username){
            alert("Please submit a username.");
            return;
        }
        else if (!password){
            alert("Please submit a password.");
            return;
        }

        setStatus(Status.LOADING);

        let response = await fetch(joinUrl, {
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
            return;
        }

        switch(response.status){
            case 409:
                setStatus(Status.HTTP_409);
                break;
            case 500:
                setStatus(Status.HTTP_500);
                break;
            case 503:
                setStatus(Status.HTTP_503);
                break;
            default:
                setStatus(Status.UNKNOWN);
        }
        alert(data.error);
    }

    const renderGuide = () => {
        switch(status){
            case Status.IDLE:
                return <></>;
            case Status.LOADING:
                return <div className="SpinnerContainer"><Spinner/></div>;
            case Status.DISCONNECTED:
                return <div className="ErrorGuide">Failed to connect with the server :(</div>;
            case Status.HTTP_500:
                return <span><i>Internal Server Error :(</i></span>;
            case Status.HTTP_503:
                return <span><i>Service Available :(</i></span>;
        }
    }

    return (
        <div className="Join">
            <div className="JoinContainer">
                <p className="WelcomeGuide">Become a member</p>

                <div className="Status">{renderGuide()}</div>
                
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