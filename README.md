# About

Websocket ping pong game

# How to start

Application use H2 database, then you need just start, and you will get 3 users (admin, user, operator).

Swagger will be available on http://localhost:8080/swagger-ui.html

# Test websocket

Use this tool for test websocket - http://jxy.me/websocket-debug-tool/

1. Login by GET http://localhost:8080/users/signin?password=operator&username=operator
2. Connect to ws://localhost:8080/secured/ping with STOMP connect header:  

`{"Authorization": "{{yourJwtToken}}"}`

3. Subscribe to topic `/user/secured/topic` and you will receive initial message
4. Send your answer to `/app/secured/ping` topic
