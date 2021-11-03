import React from 'react'

const Users = (props) => {
    return (
        <div className="UserList">
            {props.users.map((user, index) => 
                <p key={index}> {user} </p>
            )}
        </div>
    )
}

export default Users
