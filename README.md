# webflux-react-websocket-chat
Scalable WebSocket chat service with Spring React, Spring WebFlux and Redis.

## Stack
<div>
	<img src="https://github.com/ferrarijh/webflux-react-websocket-chat/blob/master/demo/chat-drawio.png">
</div>
<hr/>

Backend:
- Spring WebFlux
- Redis: For chat room implementation. Messages are sent and received via respective Pub/Sub channel associated with each room. Respective collection of users per room is stored as a sorted set.

Frontend:
- React

## Demo
<div>
	<img src="https://github.com/ferrarijh/webflux-react-websocket-chat/blob/master/demo/demo.gif">
</div>

## How to run
Assuming docker is installed and running,

1. Clone repository.
2. Execute `build.sh` which builds `chat-server`. This requires jdk11. (`chat-client` will be built in the docker container)
3. Run command `docker compose up -d` at the project root directory.
4. Go to `http://localhost:3000` or `http://localhost:3001` to start chatting.

React app running on port 3000 and 3001 send requests respectively to port 8080's and 8081's Spring Application.

## Troubleshooting

Occasionally on windows, docker may complain that it failed to bind to specified ports. This is due to windows blocking a range of ports on startup. Such ports can be checked with command:

`netsh interface ipv4 show excludedportrange protocol=tcp`

In such case you may want to rewrite `docker-compose.yml` so contaiers can bind to available ports.
