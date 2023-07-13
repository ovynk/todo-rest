# Project REST ToDo list

Project is written based on previous project Web ToDo list
<br>
Spring boot 3.0.5. Maven. H2 database. JWT.

## Capabilities

In this project you'll see the following:

* Registration, Logging In and Out
* Obtain and persist data in database
* RestControllers
* Models (Role, User, ToDo, Task)
* Create, update, read, remove models(GET, POST, PUT, DELETE mappings)
* Roles privileges (Admin and user)
* Exception handling

## Configuration details

* H2 database
* Spring Data and Hibernate
* Spring security
* Json web token

## Run

You need to run project than h2 database will be craeted automatically with some start data.
If you have created database you need to change sql init mode to never in application.yml.

There are users in data.sql

| Login         | Password | Role  |
| ------------- |:--------:|:-----:|
| mike@mail.com | 1111     | ADMIN |
| nick@mail.com | 2222     | USER  |
| nora@mail.com | 3333     | USER  |
