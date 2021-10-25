drop table user_activity;

CREATE TABLE user_activity
(
 id INT AUTO_INCREMENT NOT NULL,
 user_data_id int NOT NULL,
 user_code VARCHAR(16) NOT NULL,
 user_name VARCHAR(32) NOT NULL,
 action Varchar(50) NULL,
 module_name Varchar(50) NULL,
 changed_attribute Text NULL,
 description Varchar(200) NULL,
 target_table Varchar(50) NULL,
 target_id Varchar(50) NULL,
 ip_address Varchar(50) DEFAULT '0.0.0.0',
 created_on Datetime NOT NULL,
 PRIMARY KEY (id),
 INDEX user_activity_idx_user_data_id(user_data_id)
);

insert into system_setting(id, setting_name, setting_desc, setting_value, value_type, updated_by, updated_on)
values(55, 'Limit Display User Activity', 'Limitation on How many user activity will be displayed in home', '10', 'INT', 1, now()); 

