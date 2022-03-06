# webflux-react-websocket-chat
Scalable WebSocket chat service with Spring React, Spring WebFlux and Redis.

## Stack
Backend:
- Spring WebFlux
- Redis - For chat room implementation

Frontend:
- React

## Demo
<div>
	<img src="https://github.com/ferrarijh/webflux-react-websocket-chat/blob/master/demo/demo.gif">
</div>

## How to run
Assuming docker is installed and running,

1. Clone repository.
2. Execute `build.sh` which builds `chat-server` and `chat-client`. Building `chat-server` requires jdk11.
3. Run command `docker compose up -d` at the project root directory.
4. Go to `http://localhost:3000` or `http://localhost:3001` to start chatting.

React app running on port 3000 and 3001 send requests respectively to port 8080's and 8081's Spring Application.
