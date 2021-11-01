import React from 'react'

export const USER_IN = "USER_IN"
export const USER_OUT = "USER_OUT"
export const MESSAGE = "MESSAGE"
export const USER_LIST = "USER_LIST"
export const IN_OK = "IN_OK"

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