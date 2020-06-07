-- Scripts on this file must be written on one line.
-- If your script ends with a semicolon (;), please add another one, like the following script:

-- Create authorities
Insert into GI_AUTHORITY (ID,NAME) values (1,'ROLE1');
Insert into GI_AUTHORITY (ID,NAME) values (2,'ROLE2');
Insert into GI_AUTHORITY (ID,NAME) values (3,'ROLE3');

-- Create admin user
Insert into GI_USUARIO (ID,ACCOUNT_NON_EXPIRED,ACCOUNT_NON_LOCKED,ADMIN,CREDENTIALS_NON_EXPIRED,ENABLED,LAST_NAME,NAME,PASSWORD,PERMISOS_ASIGNADOS,USERNAME) values (-1,'1','1','1','1','1','Administrator','System','$2a$10$lZZez.MZIq2VQf8.K72kheQ5Xsev6e3S0QP3aHrk6X67Zww6AK6jG','USUARIO_CAPACIDAD_VISIBILIDAD','admin');

