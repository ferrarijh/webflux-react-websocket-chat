import React, { useEffect, useState } from 'react';
import { LoadingStatus as Status } from '../contexts/NetworkProvider';
import Resources from '../../Resources';
import Spinner from '../spinner/Spinner';
import './Rooms.css';

const roomsUrl = "http://" + Resources.HOSTNAME + "/rooms";
const roomUrl = "http://" + Resources.HOSTNAME + "/room";

const Rooms = () => {

    const [roomsList, setRoomsList] = useState([]);
    const [status, setStatus] = setState(Status.IDLE);

    useEffect(() => {
        updateRoomsList();
    }, []);

    const updateRoomsList = () => {
        setState(Status.LOADING);

        fetch(roomUrl, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        }).then(resp => resp.json())
        .then(data => {
            setStatus(Status.IDLE);
            setRoomsList(data.list);
        }).catch(err => {
            setStatus(Status.ERROR);
            console.log(err);
        });
    };

    const handleSubmit = e => {
        e.preventDefault();
        let roomName = e.currentTarget.roomNameInput.value;
        joinOrCreateRoom(roomName);
    };

    const joinOrCreateRoom = (roomName) => {
        fetch(roomUrl, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        }).then(resp => {
            if(!resp.ok)
                alert("Chat room not found. Creating new room...");
            navigate("../room/"+roomName);
        }).catch(err => {
            setStatus(Status.ERROR);
            console.log(err);
        });
    };

    return (
        <div className="RoomsContainer">
            <div className="UpdateRooms">
                <button className="UpdateRoomsButton" onClick={updateRoomsList}>Update Rooms List</button>
                {status === Status.LOADING && <Spinner />}
            </div>
            <form onSubmit={handleSubmit}>
                <input type="text" name="roomNameInput" placeholder="Room name" /><br />
                <button type="submit">Join/Create Chat Room</button>
            </form>
            <table className="Table">
                <thead>
                    <tr>
                        <th>Room Name</th>
                        <th># of chatters</th>
                    </tr>
                </thead>
                <tbody>
                    {roomsList.forEach((room, idx) => {
                        <Room key={idx} name={room.name} size={room.size} />
                    })}
                </tbody>
            </table>
            {roomsList.length === 0 && <div className="NoRoomGuide">There are no rooms right now.</div>}
        </div>
    );
};

export default Rooms;