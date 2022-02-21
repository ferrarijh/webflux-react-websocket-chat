import React, { useContext } from 'react'
import { AuthContext } from './AuthProvider'
import { Navigate } from 'react-router-dom';

const RequireAuth = (props) => {

    const {isAuth} = useContext(AuthContext);
    
    return (
        <div>
            {isAuth
                ? <>{props.children}</>
                : <Navigate to="/chat/login"/>
            }
        </div>
    );
};

export default RequireAuth;