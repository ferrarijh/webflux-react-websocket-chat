import React from 'react';

const Room = (props) => {
    return (
        <tr className={props.key ? "RoomOdd" : "RoomEven" }>
            <td><div className="Name">Name: {props.name}</div></td>
            <td><div className="Size">Size: {props.size}</div></td>
        </tr>
    )
};

export default Room