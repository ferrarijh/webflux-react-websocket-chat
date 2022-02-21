import React, {useEffect} from 'react';
import { useNavigate } from 'react-router-dom';

const RoomThumbnail = (props) => {

    const navigate = useNavigate();

    useEffect(() => {
        console.log("rendering RoomThumbnail..")
    }, [])

    const handleClick = () => {
        let url = props.baseUrl + "/" + props.room.id;
        fetch(url, {
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
                <div className="ID">ID: {props.room.id}</div>
                <div className="Title">Title: {props.room.title}</div>
            </td>
            <td><div className="Size">Size: {props.room.size}</div></td>
        </tr>
    );
};

export default RoomThumbnail;