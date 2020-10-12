INSERT INTO `role` (`name`) VALUES ('ROLE_ADMIN');
INSERT INTO `role` (`name`) VALUES ('ROLE_USER');
INSERT INTO `role` (`name`) VALUES ('ROLE_SUPERVISOR');
INSERT INTO `role` (`name`) VALUES ('ROLE_FINANCE');

INSERT INTO `user` (`email`, `first_name`, `last_name`, `password`, `username`) VALUES ('admin@admin.io', 'Admin', 'Admin', '$2a$10$ixr.yMuYGoOSjaVAjDiINeGQP9fukWYcmW9qfdoaO410Ief.I76oG', 'admin');
INSERT INTO `user_roles` (`users_id`, `roles_id`) VALUES ('1', '1');

INSERT INTO `user` (`email`, `first_name`, `last_name`, `password`, `username`) VALUES ('user@user.io', 'User', 'User', '$2a$10$8gyrExC9KjySo/VWMYN98ufqSUShGiQcVVcfc65NqXmsSqq9QxYtO', 'user'); /*password is test*/
INSERT INTO `user_roles` (`users_id`, `roles_id`) VALUES ('2', '2');

INSERT INTO `user` (`email`, `first_name`, `last_name`, `password`, `username`) VALUES ('supervisor@supervisor.io', 'Supervisor', 'Supervisor', '$2a$10$8gyrExC9KjySo/VWMYN98ufqSUShGiQcVVcfc65NqXmsSqq9QxYtO', 'supervisor');
INSERT INTO `user_roles` (`users_id`, `roles_id`) VALUES ('3', '3');

INSERT INTO `user` (`email`, `first_name`, `last_name`, `password`, `username`) VALUES ('finance@finance.io', 'Finance', 'Finance', '$2a$10$8gyrExC9KjySo/VWMYN98ufqSUShGiQcVVcfc65NqXmsSqq9QxYtO', 'finance');
INSERT INTO `user_roles` (`users_id`, `roles_id`) VALUES ('4', '4');
