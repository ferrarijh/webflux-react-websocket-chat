import {React, useState} from 'react'
import {BrowserRouter as Router, Route, Switch, Redirect} from 'react-router-dom'
import Chat from './components/chat/Chat'
import Login from './components/login/Login'
import './App.css'

const RouteLogin = ({component: Component, path, isAuth, ...rest}) => {
    return (
        <Route path={path}
            render={ () => isAuth ?
                <Redirect to={"/chat"}/> :
                <Component {...rest} />
        }/>
    )
}

const RouteChatIfAuth = ({component: Component, path, isAuth, ...rest}) => {
    return (
        <Route path={path} 
            render={ () => isAuth ? 
                <Component {...rest}/> : 
                <Redirect to={"/login"}/>
        }/>
    )
}

export const LOADING = "LOADING"
export const SUCCESS = "SUCCESS"
export const ERROR = "ERROR"

function App() {

    const [isAuth, setIsAuth] = useState(false)
    const [username, setUsername] = useState(null)
    const [users, setUsers] = useState([])
    const [netstat, setNetstat] = useState(SUCCESS)

    return (
        <div className="App">
            <Router>
                <Switch>
                    <Redirect exact from="/" to="/login"/>
                    <RouteLogin path="/login" component={Login} 
                        username={username} 
                        isAuth={isAuth}
                        setUsername={setUsername}
                        setIsAuth={setIsAuth}
                        users={users}
                        setUsers={setUsers}
                        netstat={netstat}
                        setNetstat={setNetstat}/>
                    <RouteChatIfAuth path="/chat" component={Chat} 
                        username={username} 
                        isAuth={isAuth} 
                        setIsAuth={setIsAuth}
                        users={users}
                        setUsers={setUsers}/>
                </Switch>
            </Router>
        </div>
    )
}

export default App
