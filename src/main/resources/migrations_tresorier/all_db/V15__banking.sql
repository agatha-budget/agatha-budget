CREATE TABLE bank_agreement (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    budget_id VARCHAR(36) NOT NULL,
    bank_id VARCHAR(36) NOT NULL,
    timestamp BIGINT DEFAULT 0,
    nordigen_requisition_id VARCHAR(36) DEFAULT NULL,
    archived BOOLEAN DEFAULT false,
    deleted BOOLEAN DEFAULT false,
    FOREIGN KEY (budget_id) REFERENCES budget(id)
);

CREATE TABLE bank_account (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    name VARCHAR(36) NOT NULL,
    agreement_id VARCHAR(36) NOT NULL,
    deleted BOOLEAN DEFAULT false,
    FOREIGN KEY (agreement_id) REFERENCES bank_agreement(id)
);

ALTER TABLE account
ADD COLUMN bank_account_id VARCHAR(36) DEFAULT NULL;

ALTER TABLE account
ADD FOREIGN KEY (bank_account_id) REFERENCES bank_account(id);

ALTER TABLE operation
ADD COLUMN import_identifier VARCHAR(150) DEFAULT NULL UNIQUE;