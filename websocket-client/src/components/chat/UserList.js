import React from 'react'

const Users = (props) => {
    return (
        <div className="UserList">
            {props.users.map((user, index) => 
                <p className="User" key={index}>
                    {user}{props.username===user && <span className="me"> (me)</span>}
                </p> 
            )}
        </div>
    )
}

export default Users
