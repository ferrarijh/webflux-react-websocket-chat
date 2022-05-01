import { React } from 'react';
import { BrowserRouter as Router, Navigate, Route, Routes } from 'react-router-dom';
import Chat from './components/chat/Chat';
import Login from './components/login/Login';
import Join from './components/join/Join';
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
                        <Route path="" element={<Navigate to="/chat/login" replace={true} />} />
                        <Route path="chat">
                            <Route path="login" element={
                                // <RequireAuth requireAuth={false}>
                                    <Login />
                                // </RequireAuth>
                            } />
                            <Route path="join" element={
                                // <RequireAuth requireAuth={false}>
                                    <Join />
                                // </RequireAuth>
                            } />
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
