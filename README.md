# Springboot 쇼핑몰 프로젝트

| **스프링 부트 쇼핑몰프로젝트 with JPA** 라는 책을 보면서 만들어 보는 프로젝트


## 프로젝트

- Docker
```
$ docker run --name shopping-mysql -e MYSQL_ROOT_PASSWORD=1234 -d -p 3306:3306 mysql:8.0

$ docker exec -it shopping-mysql bash

$ mysql -u root -p1
```

- Create database : mysql 8.0
```
$ create database shop default character set utf8 collate utf8_general_ci;

$ show databases;
```