insert into usr_role(id, title)
values (0, 'ADMIN');

insert into usr(id, email, first_name, last_name, login, password, patronymic, phone_number, status, user_role)
values (0, 'adm@email.com', 'admin', 'admin', 'admin', '$2a$12$5oTwFF8.aAciwfonIwP7Q.sIgvbWF8vLplBkwyn9WOkcAm/wRGzHS',
        null, '+79998887766', 'ACTIVE', 0);