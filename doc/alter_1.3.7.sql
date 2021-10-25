ALTER TABLE user_data ADD branch_id INT ,


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