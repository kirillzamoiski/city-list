INSERT INTO users (uuid, login, password)
VALUES ('2ba910dc-f312-404f-aaf8-e23bc93ce7e2', 'admin', '$2a$12$c3dxJb8pzsZyaIER7A5GzOaFxh1RyVmzwXMvuYLiSxoMZFfod6VZa'),

       ('b1d98027-266f-4d48-9e11-847ef00b7fd4', 'user', '$2a$12$wa5XPhQDeeeDLKartYGfeOcfKMkdTzy2rVysHox4piNb8MNnnhXsO');

INSERT INTO roles (name)
VALUES ('ADMIN'),
       ('USER');

INSERT INTO user_roles (user_uuid, role_name)
VALUES ('2ba910dc-f312-404f-aaf8-e23bc93ce7e2', 'ADMIN'),
       ('b1d98027-266f-4d48-9e11-847ef00b7fd4', 'USER');