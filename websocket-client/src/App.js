import {React, useState, useEffect} from 'react'
import {BrowserRouter as Router, Route, Switch, Redirect} from 'react-router-dom'
import Chat from './components/chat/Chat'
import Login from './components/login/Login'
import './App.css'

function App() {

    const [isAuth, setIsAuth] = useState(false)
    const [username, setUsername] = useState(null)

    useEffect(()=>{
        console.log("isAuth changed to ", isAuth)
    }, [isAuth])

    const RouteChatIfAuth = ({component: Component, ...rest}) => {
        console.log("RouteChatIfAuth...")
        return (
            <Route {...rest} 
                render={ props =>
                 isAuth ? <Component {...props} username={username} isAuth={isAuth} setIsAuth={setIsAuth}/> : 
                    <Redirect to={{
                        pathname: "/login",
                        state: { from: props.location }
                    }}/>
            }/>
        )
    }

    const RouteLogin = ({component: Component, ...rest}) => {
        return (
            <Route {...rest}
                render={props=>{
                    console.log("rendering Login..")
                    return <Component {...props} isAuth={isAuth} setIsAuth={setIsAuth} setUsername={setUsername}/>
            }}/>
        )
    }

    return (
        <div className="App">
            <Router>
                <Switch>
                    <Route exact path="/" ><Redirect to="/login"/></Route>

                    <RouteChatIfAuth path="/chat" component={Chat}/>
                    <RouteLogin path="/login" component={Login}/>
                </Switch>
            </Router>
        </div>
    )
}

export default App
