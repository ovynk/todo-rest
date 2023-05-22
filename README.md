# Simple project Web ToDo list with implementation of chat

Project is written to practice and learn new skills.
<br>
Spring boot 3.0.5. Maven. PostgreSQL.

## Capabilities

In this project you'll see the following:

* Obtain and persist data in database
* Controllers
* Mapping Requests (GET and POST)
* Models (Role, User, ToDo, Task, ChatMessage and others)
* Registration, Logging In and Out
* Roles privileges (Admin and user)
* Chat in each todo
* Exception handling
* Bootstrap for style of pages

## Configuration details

* PostgreSQL Driver
* Spring Data and Hibernate
* Spring MVC pattern
* Thymeleaf template engine
* Exception handling
* Spring security
* WebSocket (SockJS and STOMP)

## Run

Application use postgreSQL database so you need to create one ***empty*** and<br>
edit application.properties with url to database, username and password.

![prop-setting](https://user-images.githubusercontent.com/90598021/235520032-ccf66dea-3929-485e-896b-b18e251c5c3f.png)

You do not need create anything in empty database. Project has data.sql file.<br> 
Application.properties is configured so that ***every time running*** tables generates automtically<br>

There are users in data.sql

| Login         | Password | Role  |
| ------------- |:--------:|:-----:|
| mike@mail.com | 1111     | ADMIN |
| nick@mail.com | 2222     | USER  |
| nora@mail.com | 3333     | USER  |

## Fast review

https://user-images.githubusercontent.com/90598021/235752046-ef07b6f0-ed31-4ff3-835b-c35e39285392.mp4
