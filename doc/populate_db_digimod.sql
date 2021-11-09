-- #######################################################USER DATA##############################################
-- // user: root, pass: embadmin
insert into user_data(id, user_code, user_name, user_password, phone_no, invalid_count, user_status, level_id, group_id, last_login_on, pass_changed_on, has_change_pass, session_id, created_on, created_by, updated_on, updated_by)
VALUES (1, 'root', 'root', '363954B0B33B212FC4F0E599B99861F7294052932C9B0633C2C3288F5577C88B', '000', 0, 1, 1, 0, now(), now(), 1, '', now(), 1, now(), 1);

--#######################################################USER LEVEL##############################################
insert into user_level(level_id, level_name, level_type, level_desc, created_on, created_by, updated_on, updated_by)
values(1, 'Administrator', 0, 'Level for Administrator', now(), 1, now(), 1);

--#######################################################USER_PREFERENCE#################################################
insert into user_preference(user_id, font_size, font_family, language, theme)
values(1, 'Medium',  'Utsaah Regular', 'English', 'Hermonville Vineyards');

--#######################################################USER MENU##############################################
insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include) 
values (1, 0, 1, 1, 'icon_security.png', 'User Management', '', 'Security Module', 0);
insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (2, 0, 1, 2, 'icon_security.png', 'System Management', '', 'System Support Module', 0);
insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (3, 0, 1, 3, 'icon_security.png', 'Operational', '', 'Operational Module', 0);
insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (4, 0, 1, 4, 'icon_security.png', 'Laporan', '', 'Reporting Module', 0);

insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (101, 1, 2, 1, 'icon_manage_user.png', 'Manage User Data', 'UserData', 'Security Module', 0);
insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (102, 1, 2, 2, 'icon_manage_level.png', 'Manage Permission', 'UserLevel', 'Security Module', 0);
insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (103, 1, 2, 3, 'icon_change_password.png', 'Change Password', 'ChangePassword', 'Security Module', 1);
insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (104, 1, 2, 4, 'icon_manage_user.png', 'Reset Password', 'ResetPassword', 'Security Module', 0);
insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (106, 1, 2, 6, 'icon_manage_user.png', 'Manage Member', 'ManageMember', 'Security Module', 0);
insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (107, 1, 2, 7, 'icon_manage_user.png', 'Withdraw Commission', 'WithdrawAction', 'Security Module', 0);

insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (201, 2, 2, 1, 'icon_manage_user.png', 'Manage Source Account', 'SourceAccount', 'System Support Module', 0);
insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (202, 2, 2, 2, 'icon_manage_user.png', 'Manage User Group', 'UserGroup', 'System Support Module', 0);
insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (203, 2, 2, 3, 'icon_manage_user.png', 'Manage System Setting', 'SystemSetting', 'System Support Module', 0);
insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (204, 2, 2, 4, 'icon_manage_user.png', 'Manage Group Approval', 'GroupApproval', 'System Support Module', 0);
insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (205, 2, 2, 5, 'icon_manage_user.png', 'Manage Trading Account', 'MstTradeAccount', 'System Support Module', 0);

insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (301, 3, 2, 1, 'icon_manage_user.png', 'Maker', 'DistMaker', 'Operational Module', 0);
insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (302, 3, 2, 2, 'icon_manage_user.png', 'Checker', 'DistChecker', 'Operational Module', 0);
insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (303, 3, 2, 3, 'icon_manage_user.png', 'Callback', 'DistCallback', 'Operational Module', 0);
insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (304, 3, 2, 4, 'icon_manage_user.png', 'Approval', 'DistApproval', 'Operational Module', 0);
insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (305, 3, 2, 5, 'icon_manage_user.png', 'Check Acc Statement', 'CheckAccStatement', 'Operational Module', 0);
insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (306, 3, 2, 6, 'icon_manage_user.png', 'Upload Data Other', 'DistMakerOther', 'Operational Module', 0);
insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (307, 3, 2, 7, 'icon_manage_user.png', 'Create Id Billing', 'CreateIdBilling', 'Operational Module', 0);
insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (308, 3, 2, 8, 'icon_manage_user.png', 'Create Id Billing Checker', 'CreateIdBillingChecker', 'Operational Module', 0);
insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (309, 3, 2, 9, 'icon_manage_user.png', 'Create Id Billing Callback', 'CreateIdBillingCallback', 'Operational Module', 0);
insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (310, 3, 2, 10, 'icon_manage_user.png', 'Create Id Billing Approval', 'CreateIdBillingApproval', 'Operational Module', 0);

insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (401, 4, 2, 1, 'icon_manage_user.png', 'Laporan Distribusi Uang', 'DistMoneyReport', 'Reporting Module', 0);
insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (402, 4, 2, 1, 'icon_manage_user.png', 'Laporan Aktivitas User', 'UserActivityReport', 'Reporting Module', 0);
insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (403, 4, 2, 3, 'icon_manage_user.png', 'Laporan Transaksi Member', 'MemberTransactionReport', 'Reporting Module', 0);

--#######################################################USER LEVEL MENU##############################################
insert into user_level_menu(menu_id, level_id) values(1,1);

insert into user_level_menu(menu_id, level_id) values(101,1);
insert into user_level_menu(menu_id, level_id) values(102,1);
insert into user_level_menu(menu_id, level_id) values(103,1);
insert into user_level_menu(menu_id, level_id) values(104,1);

--#######################################################LOOKUP_DATA#################################################
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (0,'0','Library Category',1,1,now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (0,'1','User Status',2,1,now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (0,'2','Distribution Status',2,1,now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (0,'3','User Level Type',2,1,now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (0,'4','Process Status',2,1,now());

Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (1,'0','Inactive',1,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (1,'1','Active',2,1, now());

--// status: 1: uploaded by maker, 2: checked by checker, 3: approved by callback, 4: approved by approval,  
--//                              12: rejected by checker, 13: rejected by callback, 14: rejected by approval
--//                              50: process distribution, 51: distribution finished, 52: dist partial finished
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (2,'1','Uploaded by Maker',1,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (2,'2','Checked by Checker',2,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (2,'3','Checked by Callback',3,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (2,'4','Approved by Approval',3,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (2,'12','Rejected by Checker',12,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (2,'13','Rejected By Callback',13,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (2,'14','Rejected By Approval',13,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (2,'50','In Distribution Process',4,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (2,'51','Distribution Finished',5,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (2,'52','Partial Finished',6,1, now());

Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (3,'0','Normal',1,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (3,'1','Maker',2,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (3,'2','Checker',3,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (3,'3','Callback',4,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (3,'4','Approval',4,1, now());

--//process_status: 1: init, 2: success, 3: failed, 21: skip success, 22: skip failed
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (4,'1','Init',1,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (4,'2','Success',2,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (4,'3','Failed',3,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (4,'21','Skip Success',4,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (4,'22','Skip Failed',4,1, now());

Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (5,'014','Bank BCA',1,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (5,'008','Bank MANDIRI',2,1, now());

Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411111','PPh Minyak Bumi',1,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411112','PPh Gas Alam',2,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411121','PPh 21',3,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411122','PPh 22',4,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411123','PPh 22 Impor',5,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411124','PPh 23',6,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411125','PPh 25 Orang Pribadi',7,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411127','PPh 26',8,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411128','PPh Final',9,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411129','PPh Non Migas',10,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411211','PPN Dalam Negeri',11,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411212','PPN Impor',12,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411219','PPN Lainnya',13,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411221','PPn BM dalam negeri',14,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411222','PPn BM Impor',15,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411229','PPn BM Lainnya',16,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411313','PBB Perkebunan',17,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411314','PBB Perhutanan',18,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411315','PBB Pertambangan Minerba',19,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411316','PBB Pertambangan Migas',20,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411317','PBB Pertambangan Pabum',21,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411319','PBB Sektor Lainnya',22,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411611','Bea Materai',23,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411612','PPn Benda Materai',24,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411619','Pajak Tidak langsung lainnya',25,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411621','Bunga/Denda Penagihan PPh',26,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411622','Bunga/Denda Penagihan PPN',27,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411623','Bunga/Denda Penagihan PPnBM',28,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (6,'411624','Bunga/Denda Penagihan PTLL',29,1, now());

Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (7,'100','Masa',1,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (7,'106','Pembayaran Masa atas BAPK/BAP',2,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (7,'199','Pembayaran Pendahuluan SKP',3,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (7,'300','STP',4,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (7,'310','SKPKB',5,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (7,'311','SKPKB Final Bayar JHT/Pensiun/Pesangon',6,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (7,'320','SKBKBT',7,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (7,'321','SKPKBT Final Bayar JHT/Pensiun/Pesangon',8,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (7,'390','Pembayaran SK Pembetulan/Keberatan/Putusan Bandin',9,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (7,'401','Final 21 Bayar JHT/Pensiun/Pesangon',10,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (7,'500','Pengungkapan ketidak benaran',11,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (7,'501','Penghentian Penyidikan',12,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (7,'510','Sanksi/Denda/Kenaikan Pengungkapan ketidak benaran',13,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (7,'511','Sanksi Denda Penghentian Penyidikan',14,1, now());

Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (8,'1','Pending',1,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (8,'2','Process',2,1, now());
Insert into lookup_data (lookup_cat,lookup_value,lookup_desc,lookup_order,updated_by,updated_on) values (8,'3','Completed',3,1, now());



-- // system setting for WEB
insert into system_setting(id, setting_name, setting_desc, setting_value, value_type, updated_by, updated_on)
values(40, 'Email MyFxBook', 'Email MyFxBook', 'agusdk2011@gmail.com', 'STRING', 1, now());
insert into system_setting(id, setting_name, setting_desc, setting_value, value_type, updated_by, updated_on)
values(41, 'Password MyFxBook', 'Password MyFxBook', 'r4H4s14181014', 'STRING', 1, now());
insert into system_setting(id, setting_name, setting_desc, setting_value, value_type, updated_by, updated_on)
values(51, 'Max Invalid Login', 'Determine How Many invalid login user try before being blocked', '3', 'INT', 1, now());
insert into system_setting(id, setting_name, setting_desc, setting_value, value_type, updated_by, updated_on)
values(52, 'Password Minimum Length', 'Determine Minimum Length of user web password', '8', 'INT', 1, now());
insert into system_setting(id, setting_name, setting_desc, setting_value, value_type, updated_by, updated_on)
values(53, 'Password Expiry Day', 'Determine how many days before web password expireds', '180', 'INT', 1, now());
insert into system_setting(id, setting_name, setting_desc, setting_value, value_type, updated_by, updated_on)
values(54, 'Double Login Time', 'Determine how many idle minutes before user can login again', '10', 'INT', 1, now()); 
insert into system_setting(id, setting_name, setting_desc, setting_value, value_type, updated_by, updated_on)
values(55, 'Limit Display User Activity', 'Limitation on How many user activity will be displayed in home', '10', 'INT', 1, now()); 

insert into system_setting(id, setting_name, setting_desc, setting_value, value_type, updated_by, updated_on)
values(101, 'OTP LENGTH', 'Length of OTP to be sent', '6', 'INT', 1, now());
insert into system_setting(id, setting_name, setting_desc, setting_value, value_type, updated_by, updated_on)
values(102, 'SMS GATEWAY URL', 'URL of SMS Gateway', 'http://localhost:1200/sms-gateway', 'STRING', 1, now());
insert into system_setting(id, setting_name, setting_desc, setting_value, value_type, updated_by, updated_on)
values(103, 'SMS GATEWAY ENC KEY', 'Encryption Key of SMS Gateway', 'eMobile', 'STRING', 1, now());
insert into system_setting(id, setting_name, setting_desc, setting_value, value_type, updated_by, updated_on)
values(104, 'USSD MMBS URL', 'URL of USSD MMBS', 'http://localhost:1300/ussd-mmbs', 'STRING', 1, now());
insert into system_setting(id, setting_name, setting_desc, setting_value, value_type, updated_by, updated_on)
values(105, 'USSD MMBS ENC KEY', 'Encryption Key of USSD MMBS', 'eMobile', 'STRING', 1, now());
insert into system_setting(id, setting_name, setting_desc, setting_value, value_type, updated_by, updated_on)
values(106, 'USSD BALANCE SYNTAX', 'Syntax for USSD Check Balance', 'SAL @{SRAC}', 'STRING', 1, now());
insert into system_setting(id, setting_name, setting_desc, setting_value, value_type, updated_by, updated_on)
values(107, 'USSD TRANSFER SYNTAX', 'Syntax for USSD Transfer', 'TRF @{SRAC} @{DSAC} @{AMOUNT}', 'STRING', 1, now());
insert into system_setting(id, setting_name, setting_desc, setting_value, value_type, updated_by, updated_on)
values(108, 'CHECK BALANCE INTERVAL', 'Interval for check balance, in seconds', '3600', 'INT', 1, now());

insert into system_setting(id, setting_name, setting_desc, setting_value, value_type, updated_by, updated_on)
values(110, 'COLLEGA URL', 'URL of Collega Server', 'http://<IP Address>:<Port>/Gateway/gateway/services/v2/postData', 'STRING', 1, now());
insert into system_setting(id, setting_name, setting_desc, setting_value, value_type, updated_by, updated_on)
values(111, 'LOCAL IP ADDRESS', 'Local IP Address used for create HMAC', '127.0.0.1', 'STRING', 1, now());
insert into system_setting(id, setting_name, setting_desc, setting_value, value_type, updated_by, updated_on)
values(112, 'COLLEGA AUTH KEY', 'Collega Auth Key', 'secret', 'STRING', 1, now());
insert into system_setting(id, setting_name, setting_desc, setting_value, value_type, updated_by, updated_on)
values(113, 'COLLEGA PARAM DATA', 'Parameter Data for Trx to Collega', '{"usergtw":"gntgw","channel":"25","corpid":"128","prodid":"BS","branch":"001","dsacPajak":"1234567789","dsacPajakName":"PAJAK DAERAH"}', 'STRING', 1, now());



         
