# webflux-react-websocket-chat
Scalable WebSocket chat service with React, Spring Web MVC/WebFlux, Redis and MySQL.

## Stack

Backend:
- Spring WebFlux
- Redis: For chat room implementation. Messages are sent and received via respective Pub/Sub channel associated with each room. Respective collection of users per room is stored as a sorted set.
- MySQL: For user database.

Frontend:
- React

## Architecture & diagram

#### Service architecture:
<div>
	<img src="https://github.com/ferrarijh/webflux-react-websocket-chat/blob/master/demo/chat-architecture-whole.drawio.png">
</div>
<br/>
Gateway receives requests at `127.0.0.1:8080` and load-balances authorized requests to respective chat servers.

#### Chat diagram:
<div>
	<img src="https://github.com/ferrarijh/webflux-react-websocket-chat/blob/master/demo/chat-architecture-chat.drawio.png">
</div>

## Demo

### Sign up & Sign In
<div>
	<img src="https://github.com/ferrarijh/webflux-react-websocket-chat/blob/master/demo/demo-join-login.gif">
</div>

### Sign In & Chat
<div>
	<img src="https://github.com/ferrarijh/webflux-react-websocket-chat/blob/master/demo/demo-login-chat.gif">
</div>

## How to run
Assuming docker is installed and running,

1. Clone repository.
2. Execute `build.sh` which builds all sub projects. (requires jdk11)
3. Run command `docker compose up -d` at the project root directory.
4. Go to `http://127.0.0.1:3000` to sign up and start chatting.

## Troubleshooting

### Windows Port Binding Issue
Occasionally on windows, docker may complain that it failed to bind to specified ports. This is due to windows blocking a range of ports on startup. Such ports can be checked with command:

`netsh interface ipv4 show excludedportrange protocol=tcp`

In such case you may want to rewrite `docker-compose.yml` so contaiers can bind to available ports.
