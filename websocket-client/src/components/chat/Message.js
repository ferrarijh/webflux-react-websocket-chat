import React from 'react'

const Message = (props) => {
    return (
        <div className="Message">
            {/* <div>
                <p>{props.id}</p>
            </div>
            <div>
                <p>{props.content}</p>
            </div> */}
            <p>
                {props.message}
            </p>
        </div>
    )
}

export default Message