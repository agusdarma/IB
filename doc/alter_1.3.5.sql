ALTER TABLE distribution_detail ADD kota_wp VARCHAR(100)

insert into user_menu (menu_id, parent_id, menu_level, show_order, menu_icon, menu_text, menu_url, menu_group, always_include)
values (311, 3, 2, 11, 'icon_manage_user.png', 'Create Id Billing Approval Non NPWP', 'CreateIdBillingNonNpwp', 'Operational Module', 0);