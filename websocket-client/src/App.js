import {React} from 'react';
import {BrowserRouter as Router, Navigate, Route, Routes} from 'react-router-dom';
import Chat from './components/chat/Chat';
import Login from './components/login/Login';
import './App.css';
import AuthProvider from './components/contexts/AuthProvider';

function App() {

    return (
        <div className="App">
            <AuthProvider>
            <Router>
                <Routes>
                    <Route path="" element={<Navigate to="chat"/>}/>
                    <Route path="chat">
                        <Route path="" element={<Navigate to="login"/>}/>
                        <Route path="login" element={<Login/>}/>
                        <Route path="chat" element={<Chat/>}/>
                    </Route>
                </Routes>
            </Router>
            </AuthProvider>
        </div>
    );
}

export default App;
