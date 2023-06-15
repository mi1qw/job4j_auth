## Accidents [Job4j.ru](http://Job4j.ru)

### REST, SECURITY 
[![Java CI with PostgreSQL](https://github.com/mi1qw/job4j_auth/actions/workflows/maven.yml/badge.svg)](https://github.com/mi1qw/job4j_auth/actions/workflows/maven.yml)
[![We recommend IntelliJ IDEA](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)


#### RestFul, Spring boot security JWT

Methods Get, Post, Put, Patch, Delete в контроллере.

Patch получает `userDTO`, обновляя базу данных.

***
DTO разбивается на два отдельных объекта


<p align="center">
    <img src="img/2023-06-16-00-43-44.png" width="693px"/>
</p>

Сохраняется в базе с учётом записи по умолчанию

<p align="center">
  <img width="432" src="img/2023-06-16-00-44-18.png"/>
</p>


***
### Запуск проекта
создайте базу _fullstack_auth_ через терминал PostgreSQL<br>
```sh
$ sudo -u postgres psql
$ create database fullstack_auth
$ \q
```
запуск
```sh
$ mvn spring-boot:run
```

***
Контакты<br>
Email: my@gmail.com