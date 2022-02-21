import { React } from 'react';
import { BrowserRouter as Router, Navigate, Route, Routes } from 'react-router-dom';
import Chat from './components/chat/Chat';
import Login from './components/login/Login';
import './App.css';
import AuthProvider from './components/contexts/AuthProvider';
import Rooms from './components/rooms/Rooms';
import RequireAuth from './components/contexts/RequireAuth';

function App() {

    return (
        <div className="App">
            <AuthProvider>
                <Router>
                    <Routes>
                        <Route path="" element={<Navigate to="chat" replace />} />
                        <Route path="chat">
                            <Route path="" element={<Navigate to="login" replace />} />
                            <Route path="login" element={<Login />} />
                            <Route path="rooms" element={
                                <RequireAuth>
                                    <Rooms />
                                </RequireAuth>
                            } />
                            <Route path="room/chat/:roomId" element={
                                <RequireAuth>
                                    <Chat />
                                </RequireAuth>
                            } />
                        </Route>
                    </Routes>
                </Router>
            </AuthProvider>
        </div>
    );
}

export default App;
