import React, { useContext, useEffect, useState } from 'react';
import { LoadingStatus as Status } from '../contexts/NetworkProvider';
import Resources from '../../Resources';
import Spinner from '../spinner/Spinner';
import './Rooms.css';
import RoomThumbnail from './RoomThumbnail';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthProvider';

const baseUrl = "http://"+Resources.HOSTNAME +":"+Resources.PORT+"/chat";
const roomBaseUrl = baseUrl + "/room";
const roomsBaseUrl = baseUrl + "/rooms";
const roomChatBaseUrl = roomBaseUrl + "/chat";

const Rooms = () => {

    const navigate = useNavigate();
    const [roomList, setRoomList] = useState([]);
    const [status, setStatus] = useState(Status.IDLE);
    const {isAuth} = useContext(AuthContext);

    useEffect(() => {
        console.log("Rooms().. isAuth="+isAuth);
        console.log("process.env.REACT_APP_SERVER_PORT=%s", process.env.REACT_APP_SERVER_PORT);
        console.log("Resources=%s", JSON.stringify(Resources));
        updateRoomList();
    }, []);

    const updateRoomList = async () => {
        setStatus(Status.LOADING);

        let newRoomList = await fetch(roomsBaseUrl, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        }).then(resp => resp.json())
        .catch(err => {
            setStatus(Status.ERROR);
            console.log(err);
        });

        if(!newRoomList)
            return;

        setStatus(Status.IDLE);
        setRoomList(newRoomList);
    }

    const handleSubmit = e => {
        e.preventDefault();
        let title = e.currentTarget.titleInput.value;
        if(title === ""){
            alert("Title should not be empty!");
            return;
        }
        createRoom(title);
    };

    const createRoom = async (title) => {
        let response = await fetch(roomBaseUrl, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({title: title})
        }).catch(err => {
            alert("Server not responding.. Error: "+err);
            setStatus(Status.ERROR);
        });

        if(!response)
            return;

        if(!response.ok){
            alert("Create request rejected. Status="+response.status);
            return;
        }

        let newRoom = await response.json();
        navigate("../room/chat/"+newRoom.id);
    };

    const renderGuide = () => {
        switch(status){
            case Status.IDLE:
                return roomList.length === 0 && <div className="NoRoomGuide">There are no rooms right now.</div>;
            case Status.LOADING:
                return <div className="SpinnerContainer"><Spinner/></div>;
            case Status.ERROR:
                return <div className="ErrorGuide">Failed to connect with the server :(</div>;
        }
    }

    return (
        <div className="Rooms">
            <div className="UpdateRoomList">
                <button className="UpdateRoomListButton" onClick={updateRoomList}>Update Room List</button>
            </div>
            <form className="CreateRoomForm" onSubmit={handleSubmit}>
                <input type="text" className="Text" name="titleInput" placeholder="Room Title" /><br />
                <button type="submit" className="Button">Create Chat Room</button>
            </form>
            <table className="Table">
                <thead>
                    <tr>
                        <th>Room Info.</th>
                        <th># of chatters</th>
                    </tr>
                </thead>
                <tbody>
                    {roomList.map((room) => 
                        <RoomThumbnail key={room.id} room={room} baseUrl={roomsBaseUrl} updateRoomList={updateRoomList}/>
                    )}
                </tbody>
            </table>
            <div className="Guide">{renderGuide()}</div>
        </div>
    );
};

export default Rooms;