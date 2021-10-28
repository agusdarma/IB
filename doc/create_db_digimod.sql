create database if not exists db_ib_nabung_dividen;
use db_ib_nabung_dividen;

create table tbl_messages (
	id         int auto_increment not null,
	code       varchar(32) not null,
	msg_desc   varchar(1000) not null,
	msg_type   varchar(10) not null,
	message    varchar(500) not null,
	created_by int not null,
	created_on datetime not null,
	updated_by int not null,
	updated_on datetime not null,
	Constraint primary key(id),
	unique key tbl_messages_idx_code(code) 
);

create table system_setting (
	id            int not null,
	setting_name  varchar(64) not null,
	setting_desc  varchar(200) not null,
	setting_value varchar(255) not null,
	value_type    varchar(16) not null,
	updated_by    int not null,
	updated_on    datetime not null,
	Constraint primary key(id)
);

create table tbl_counter (
  app_name   char(10) not null,
  counter    int null,
  period     datetime null,
  Constraint primary key(app_name)
);

CREATE TABLE lookup_data (
  lookup_cat INT NOT NULL,
  lookup_value VARCHAR(50) NOT NULL,
  lookup_desc VARCHAR(255) NOT NULL,
  lookup_order INT NOT NULL,
  updated_by INT NOT NULL,
  updated_on DATETIME NOT NULL,
  PRIMARY KEY (lookup_cat, lookup_value)
);

/*user_status: 0: inactive, 1: active*/
/*session_id: store session from web to prevent duplicate access*/
CREATE TABLE user_data (
                id INT NOT NULL,
                user_code VARCHAR(16) NOT NULL,
                user_name VARCHAR(32) NOT NULL,
                user_password VARCHAR(64) NOT NULL,
				phone_no VARCHAR(20) NOT NULL,
                invalid_count INT NOT NULL,
                user_status INT NOT NULL,
                level_id INT NOT NULL,
				group_id INT NOT NULL,
                last_login_on DATETIME,
                pass_changed_on DATETIME,
                has_change_pass TINYINT NOT NULL,
                session_id VARCHAR(64) NOT NULL,
                last_access_on DATETIME,
                created_on DATETIME NOT NULL,
                created_by INT NOT NULL,
                updated_on DATETIME NOT NULL,
                updated_by INT NOT NULL,
                PRIMARY KEY (id)
);
CREATE UNIQUE INDEX idx_user_data_user_code ON user_data( user_code );

CREATE TABLE user_menu (
                menu_id INT NOT NULL,
                parent_id INT NOT NULL,
                menu_level INT NOT NULL,
                show_order INT NOT NULL,
                menu_icon VARCHAR(32) NOT NULL,
                menu_text VARCHAR(64) NOT NULL,
                menu_url VARCHAR(64) NOT NULL,
                menu_group VARCHAR(64) NOT NULL,
                always_include INT NOT NULL,
                PRIMARY KEY (menu_id)
);

/*level_type: 0:normal level, 1: maker, 2: checker, 3: approval*/
CREATE TABLE user_level (
                level_id INT NOT NULL,
                level_name VARCHAR(32) NOT NULL,
                level_type INT NOT NULL,
                level_desc VARCHAR(255) NOT NULL,
                created_on DATETIME NOT NULL,
                created_by INT NOT NULL,
                updated_on DATETIME NOT NULL,
                updated_by INT NOT NULL,
                PRIMARY KEY (level_id)
);
CREATE UNIQUE INDEX idx_user_level_level_name ON user_level( level_name );
                                          
/*access level 0 read only, 1 full access */                                       
CREATE TABLE user_level_menu (
                menu_id INT NOT NULL,
                level_id INT NOT NULL,
                access_level INT NOT NULL DEFAULT 1,
                PRIMARY KEY (menu_id, level_id)
);

CREATE TABLE user_group (
	id INT AUTO_INCREMENT NOT NULL,
	group_name VARCHAR(100) NOT NULL,
	group_desc VARCHAR(200) NOT NULL,
    created_on DATETIME NOT NULL,
    created_by INT NOT NULL,
    updated_on DATETIME NOT NULL,
    updated_by INT NOT NULL,
	PRIMARY KEY (id),
	unique INDEX user_group_idx_name(group_name)
);

create table user_group_approval (
	user_data_id INT NOT NULL,
	user_group_id INT NOT NULL,
	PRIMARY KEY (user_data_id, user_group_id),
	unique index user_group_approval_idx_group(user_group_id)
);

/*
--//user_activity only records successful action in system
--//action: LOGIN,LOGOUT,MAKE DIST,CHECK DIST,CALLBACK DIST,EXEC DIST,CREATE DATA,UPDATE DATA
--//module_name: taken from module name in user_menu
--//changed_attribute: avail only in CREATE DATA,UPDATE DATA, in JSON format List<List<String>: [[field,old,new]]
--//description:desc of action, 
--//target_table: main table that affected by the action
--//target_id: ID of main table that is affected, in some table, id is varchar n in other, id is int
--//created_on:timestamp when activity is saved in DB
*/
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


CREATE TABLE user_preference
(
 user_id INT NOT NULL,
 font_size Varchar(50) NULL,
 font_family Varchar(100) NULL,
 language Varchar(100) NULL,
 theme Varchar(100) NULL,
 PRIMARY KEY (user_id)
);

/*
--// srac_data is enc with json -> phoneNo, sracNumber, sracName, sracPin, sracStatus, 
--// srac_data is enc with srac_key - id - created_by
*/
CREATE TABLE source_account
(
 id INT AUTO_INCREMENT NOT NULL,
 phone_no VARCHAR(50) NOT NULL,
 srac_data VARCHAR(1000) NOT NULL,
 srac_key VARCHAR(50) NOT NULL,
 created_on DATETIME NOT NULL,
 created_by INT NOT NULL,
 updated_on DATETIME NOT NULL,
 updated_by INT NOT NULL,
 PRIMARY KEY (id)
);

/*
--// syslogno: yyMMddAxxxxx1
--// file_data: file name of data to be distributed, in format phoneNo, accNo, amount, prefixed with yyyyMMddHHmmss_
--// file_assignment: file name of assignment, prefixed with yyyyMMddHHmmss_
--// status: 1: uploaded by maker, 2: checked by checker, 3: approved by callback, 4: approved by approval,  
--//                              12: rejected by checker, 13: rejected by callback, 14: rejected by approval
--//                              50: process distribution, 51: distribution finished, 52: dist partial finished
--// uploaded_amount: the number of detail
--// uploaded_value: total value of money to be distributed
--// process_success: number of detail that has been successfully processed
--// process_failed: number of detail that has been processed but failed
--// process_value: total value of money that has been successfully processed
*/
CREATE TABLE distribution_header
(
 syslogno VARCHAR(14) NOT NULL,
 group_id INT NOT NULL,
 group_name VARCHAR(100) NOT NULL,
 file_data VARCHAR(50) NOT NULL,
 file_assignment VARCHAR(50) NOT NULL,
 source_account_id INT NOT NULL,
 phone_no VARCHAR(50) NOT NULL,
 srac_number VARCHAR(50) NOT NULL,
 maker_remarks VARCHAR(1000) NOT NULL,
 checker_remarks VARCHAR(1000) NOT NULL,
 callback_remarks VARCHAR(1000) NOT NULL,
 approval_remarks VARCHAR(1000) NOT NULL,
 status INT NOT NULL,
 uploaded_amount INT,
 uploaded_value DECIMAL(12,2),
 uploaded_by INT NOT NULL,
 uploaded_on DATETIME,
 checked_by INT NOT NULL,
 checked_on DATETIME,
 callback_by INT NOT NULL,
 callback_on DATETIME,
 approved_by INT NOT NULL,
 approved_on DATETIME,
 process_success INT,
 process_failed INT,
 process_value DECIMAL(12,2),
 process_started DATETIME,
 process_finished DATETIME,
 PRIMARY KEY(syslogno)
);

/*
--//process_status: 1: init, 2: success, 3: failed, 21: skip success, 22: skip failed
--// process_remarks: remarks from approval when need to skip process again
*/
CREATE TABLE distribution_detail
(
 syslogno VARCHAR(14) NOT NULL,
 detail_id INT NOT NULL,
 phone_no VARCHAR(50) NOT NULL,
 account_no VARCHAR(50) NOT NULL,
 account_name VARCHAR(100) NOT NULL,
 money_value DECIMAL(12,2) NOT NULL,
 host_ref_no VARCHAR(20),
 host_rc VARCHAR(10),
 host_message VARCHAR(500) NOT NULL,
 process_status INT NOT NULL,
 process_started DATETIME,
 process_finished DATETIME,
 process_remarks VARCHAR(500),
 data_check VARCHAR(100) NOT NULL,
 nama VARCHAR(100) NOT NULL,
 alamat VARCHAR(100) NOT NULL,
 jenisPajak VARCHAR(100) NOT NULL,
 jenisSetoran VARCHAR(100) NOT NULL,
 startDatePbb DATETIME,
 endDatePbb DATETIME,
 tahunPajak VARCHAR(100) NOT NULL,
 jumlahSetor DECIMAL(12,2) NOT NULL,
 PRIMARY KEY(syslogno, detail_id),
 INDEX detail_idx_syslogno(syslogno)
);


CREATE TABLE master_trading_account
(
 id INT AUTO_INCREMENT NOT NULL,
 trading_account_no VARCHAR(100) NOT NULL,
 trading_server VARCHAR(100) NOT NULL,
 name VARCHAR(100) NOT NULL,
 password_trading VARCHAR(100) NOT NULL,
 password_investor VARCHAR(100) NOT NULL,
 status INT NOT NULL,
 ib_user_code VARCHAR(100) NOT NULL,
 created_on DATETIME NOT NULL,
 created_by INT NOT NULL,
 updated_on DATETIME NOT NULL,
 updated_by INT NOT NULL,
 PRIMARY KEY (id)
);

CREATE TABLE history_trading
(
 id INT AUTO_INCREMENT NOT NULL,
 symbol VARCHAR(16) NOT NULL,
 open_time VARCHAR(32) NOT NULL,
 close_time VARCHAR(32) NOT NULL,
 action VARCHAR(16) NOT NULL,
 size_lot VARCHAR(16) NOT NULL,
 open_price VARCHAR(16) NOT NULL,
 close_price VARCHAR(16) NOT NULL,
 profit VARCHAR(16) NOT NULL,
 myfxbook_id VARCHAR(16) NOT NULL,
 PRIMARY KEY (id)
);
