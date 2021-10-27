import React from 'react'

const Users = (props) => {
    return (
        <div className="UserList">
            {props.users.map((user) => 
                <p> {user} </p>
            )}
        </div>
    )
}

export default Users
