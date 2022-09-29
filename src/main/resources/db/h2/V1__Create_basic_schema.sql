CREATE TABLE person (
        id VARCHAR(36) NOT NULL PRIMARY KEY,
        email VARCHAR(255) UNIQUE,
        name VARCHAR(255),
        password VARCHAR(255),
        unlockingDate BIGINT DEFAULT 0,
        loginAttemptCount INT DEFAULT 0,
        deleted BOOLEAN DEFAULT false
);

CREATE TABLE budget (
        id VARCHAR(36) NOT NULL PRIMARY KEY,
        person_id VARCHAR(36) NOT NULL,
        name VARCHAR(100) NOT NULL,
        deleted BOOLEAN DEFAULT false,
        FOREIGN KEY (person_id) REFERENCES person(id)
);

CREATE TABLE account (
        id VARCHAR(36) NOT NULL PRIMARY KEY,
        budget_id VARCHAR(36) NOT NULL,
        name VARCHAR(100) NOT NULL,
        archived BOOLEAN DEFAULT false,
        deleted BOOLEAN DEFAULT false,
        FOREIGN KEY (budget_id) REFERENCES budget(id)
);

CREATE TABLE master_category (
        id VARCHAR(36) NOT NULL PRIMARY KEY,
        budget_id VARCHAR(36) NOT NULL,
        name VARCHAR(100) NOT NULL,
        deleted BOOLEAN DEFAULT false,
        FOREIGN KEY (budget_id) REFERENCES budget(id)
);

CREATE TABLE category (
        id VARCHAR(36) NOT NULL PRIMARY KEY,
        master_category_id VARCHAR(36) DEFAULT NULL,
        name VARCHAR(100) NOT NULL,
        archived BOOLEAN DEFAULT false,
        deleted BOOLEAN DEFAULT false,
        FOREIGN KEY (master_category_id) REFERENCES master_category(id)
);

INSERT INTO
       category (id, name)
VALUES
        ('universal_income_category', 'I18N_INCOME'),
        ('universal_transfert_category', 'I18N_TRANSFERT')
;

CREATE TABLE operation (
        id VARCHAR(36) NOT NULL PRIMARY KEY,
        account_id VARCHAR(36) NOT NULL,
        date_month INT DEFAULT NULL,
        date_day INT DEFAULT NULL,
        category_id VARCHAR(36) DEFAULT NULL,
        amount DECIMAL(10,2) NOT NULL DEFAULT 0,
        memo VARCHAR(280) DEFAULT NULL,
        FOREIGN KEY (account_id) REFERENCES account(id),
        FOREIGN KEY (category_id) REFERENCES category(id),
        CONSTRAINT no_negative_month_operation CHECK ( date_month%100 > 0),
        CONSTRAINT no_invalid_month_operation CHECK ( date_month%100 < 13),
        CONSTRAINT no_negative_day_operation CHECK ( date_day > 0),
        CONSTRAINT no_invalid_day_operation CHECK ( date_day < 32)
);

CREATE TABLE allocation (
        category_id VARCHAR(36) NOT NULL,
        date_month INT NOT NULL,
        amount DECIMAL(10,2),
        FOREIGN KEY (category_id) REFERENCES category(id),
        CONSTRAINT composite_id PRIMARY KEY (category_id, date_month),
        CONSTRAINT no_negative_month_allocation CHECK ( date_month%100 > 0),
        CONSTRAINT no_invalid_month_allocation CHECK ( date_month%100 < 13)
);
