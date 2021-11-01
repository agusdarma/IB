ALTER TABLE user_data ADD branch_id INT ,
ALTER TABLE user_data ADD email VARCHAR(100)
ALTER TABLE user_data ADD pct_sharing_profit DOUBLE(2,1)

ALTER TABLE master_trading_account ADD myfxbook_id VARCHAR(32)
ALTER TABLE master_trading_account ADD commission_in_dollar INT NOT NULL DEFAULT 2,

ALTER TABLE history_trading ADD total_commission VARCHAR(32)
ALTER TABLE history_trading ADD company_commission VARCHAR(32)
ALTER TABLE history_trading ADD client_commission VARCHAR(32)




CREATE TABLE user_branch (
	id INT AUTO_INCREMENT NOT NULL,
	branch_name VARCHAR(100) NOT NULL,
	branch_desc VARCHAR(200) NOT NULL,
    created_on DATETIME NOT NULL,
    created_by INT NOT NULL,
    updated_on DATETIME NOT NULL,
    updated_by INT NOT NULL,
	PRIMARY KEY (id),
	unique INDEX user_branch_idx_name(branch_name)
);