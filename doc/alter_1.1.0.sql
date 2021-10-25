insert into system_setting(id, setting_name, setting_desc, setting_value, value_type, updated_by, updated_on)
values(110, 'COLLEGA URL', 'URL of Collega Server', 'http://<IP Address>:<Port>/Gateway/gateway/services/v2/postData', 'STRING', 1, now());
insert into system_setting(id, setting_name, setting_desc, setting_value, value_type, updated_by, updated_on)
values(111, 'LOCAL IP ADDRESS', 'Local IP Address used for create HMAC', '127.0.0.1', 'STRING', 1, now());
insert into system_setting(id, setting_name, setting_desc, setting_value, value_type, updated_by, updated_on)
values(112, 'COLLEGA AUTH KEY', 'Collega Auth Key', 'secret', 'STRING', 1, now());
insert into system_setting(id, setting_name, setting_desc, setting_value, value_type, updated_by, updated_on)
values(113, 'COLLEGA PARAM DATA', 'Parameter Data for Trx to Collega', '{"usergtw":"usergtw","channel":"99","corpid":"128","prodid":"6013","branch":"001"}', 'STRING', 1, now());


