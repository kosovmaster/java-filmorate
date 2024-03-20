DROP ALL OBJECTS;

create table GENRES
(
    GENRE_ID INTEGER auto_increment
        primary key,
    NAME     CHARACTER VARYING(40) not null
);

create table MPA
(
    MPA_ID INTEGER auto_increment
        primary key,
    NAME   CHARACTER VARYING(60)
);

create table FILMS
(
    FILM_ID       INTEGER auto_increment
        primary key,
    NAME          CHARACTER VARYING(100) not null,
    DESCRIPTION   CHARACTER VARYING(200) not null,
    RELEASE_DATE  TIMESTAMP              not null,
    DURATION      INTEGER                not null,
    MPA_ID INTEGER
        references MPA
);

create table FILM_GENRE
(
    ID       INTEGER auto_increment
        primary key,
    FILM_ID  INTEGER not null
        references FILMS,
    GENRE_ID INTEGER not null
        references GENRES
);

create table USERS
(
    USER_ID  INTEGER auto_increment
        primary key,
    EMAIL    CHARACTER VARYING(60) not null,
    LOGIN    CHARACTER VARYING(60) not null,
    NAME     CHARACTER VARYING(60),
    BIRTHDAY DATE                  not null
);

create table FRIENDS
(
    RELATIONSHIP_ID INTEGER auto_increment
        primary key,
    USER_ID         INTEGER not null
        references USERS,
    FRIEND_ID       INTEGER not null
        references USERS
);

create table LIKES
(
    LIKE_ID INTEGER auto_increment
        primary key,
    FILM_ID INTEGER not null
        references FILMS,
    USER_ID INTEGER not null
        references USERS
);