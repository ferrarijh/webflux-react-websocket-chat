import React, { useEffect, useState } from 'react';
import { LoadingStatus as Status } from '../contexts/NetworkProvider';
import Resources from '../../Resources';
import Spinner from '../spinner/Spinner';
import './Rooms.css';
import RoomThumbnail from './RoomThumbnail';
import { useNavigate } from 'react-router-dom';

const baseUrl = "http://"+Resources.HOSTNAME +":"+Resources.PORT+"/chat";
const roomBaseUrl = baseUrl + "/room";
const roomsBaseUrl = baseUrl + "/rooms";
const roomChatBaseUrl = roomBaseUrl + "/chat";

const Rooms = () => {

    const navigate = useNavigate();
    const [roomList, setRoomList] = useState([]);
    const [status, setStatus] = useState(Status.IDLE);

    useEffect(() => {
        updateRoomList();
    }, []);

    const updateRoomList = () => {
        setStatus(Status.LOADING);

        fetch(roomsBaseUrl, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        }).then(resp => resp.json())
        .then(data => {
            setStatus(Status.IDLE);
            setRoomList(data);
            console.log(data);
        }).catch(err => {
            setStatus(Status.ERROR);
            console.log(err);
        });
    };

    const handleSubmit = e => {
        e.preventDefault();
        let title = e.currentTarget.titleInput.value;
        console.log("title="+title);
        createRoom(title);
    };

    const createRoom = (title) => {
        fetch(roomBaseUrl, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({title: title})
        }).then(resp => {
            if(!resp.ok)
                throw resp.status;
            return resp.json()
        }).then(data => {
            navigate("../room/chat/"+data.id);
        }).catch(thrown => {
            if(typeof thrown === "number")
                alert("Failed to create room. Status="+thrown);
            else
                alert("Error: "+thrown);
            setStatus(Status.ERROR);
        });
    };

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
            {(roomList.length === 0 && status === Status.IDLE) &&
                 <div className="NoRoomGuide">There are no rooms right now.</div>}
            {status === Status.LOADING && <Spinner />}
        </div>
    );
};

export default Rooms;