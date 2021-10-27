import React from 'react'
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom'
import Chat from './components/chat/Chat'
import Login from './components/login/Login'
import './App.css'

const App = () => {
    return (
        <div className="App">
            <Router>
                <Switch>
                    <Route path="/chat" component={Chat}/>
                    <Route path="/login" component={Login}/>
                </Switch>
            </Router>
        </div>
    )
}

export default App
