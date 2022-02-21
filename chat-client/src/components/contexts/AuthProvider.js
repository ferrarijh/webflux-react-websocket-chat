import React, { useState } from 'react';
import { LoadingStatus } from './NetworkProvider';

const AuthContext = React.createContext(null);

const AuthProvider = (props) => {

    const [isAuth, setIsAuth] = useState(false);
    const [username, setUsername] = useState(null);
    const [authStatus, setAuthStatus] = useState(LoadingStatus.IDLE);
    
    return (
        <AuthContext.Provider value={{username, setUsername, isAuth, setIsAuth, authStatus, setAuthStatus}}>
            {props.children}
        </AuthContext.Provider>
    )
};

export default AuthProvider;
export {AuthContext};