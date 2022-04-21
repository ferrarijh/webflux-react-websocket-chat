import React, {useEffect} from 'react';
import { useNavigate } from 'react-router-dom';
import './RoomThumbnail.css';

const RoomThumbnail = (props) => {

    const navigate = useNavigate();

    useEffect(() => {
        console.log("rendering RoomThumbnail..")
    }, [])

    const handleClick = () => {
        let url = props.baseUrl + "/" + props.room.id;
        fetch(url, {
            credentials: 'include',
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        }).then(resp => {
            if (!resp.ok)
                throw resp.status;
            else
                navigate("../room/chat/" + props.room.id);
        }).catch(thrown => {
            if (typeof thrown === "number") {
                if (thrown === 404)
                    alert("Room not found.");
                else
                    alert("Error status=" + thrown);
            }
            else
                alert("Unknown error: "+thrown);
            props.updateRoomList();
        });
    }

    return (
        <tr className="RoomThumbnail" onClick={handleClick}>
            <td>
                <div className="Title">{props.room.title}</div>
                <div className="Id">[ID] {props.room.id}</div>
            </td>
            <td><div className="Size">{props.room.size}</div></td>
        </tr>
    );
};

export default RoomThumbnail;