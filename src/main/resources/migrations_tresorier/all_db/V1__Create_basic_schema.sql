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

CREATE TABLE category (
        id VARCHAR(36) NOT NULL PRIMARY KEY,
        budget_id VARCHAR(36) DEFAULT NULL,
        name VARCHAR(100) NOT NULL,
        archived BOOLEAN DEFAULT false,
        deleted BOOLEAN DEFAULT false,
        FOREIGN KEY (budget_id) REFERENCES budget(id)
);

CREATE TABLE operation (
        id VARCHAR(36) NOT NULL PRIMARY KEY,
        operation_date BIGINT NOT NULL,
        account_id VARCHAR(36) NOT NULL,
        category_id VARCHAR(36) NOT NULL,
        amount DECIMAL(10,2) NOT NULL DEFAULT 0,
        memo VARCHAR(280) DEFAULT NULL,
        FOREIGN KEY (account_id) REFERENCES account(id),
        FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE allocation (
        id VARCHAR(36) NOT NULL PRIMARY KEY,
        category_id VARCHAR(36) NOT NULL,
        year INT NOT NULL,
        month INT NOT NULL,
        amount DECIMAL(10,2),
        FOREIGN KEY (category_id) REFERENCES category(id),
        CONSTRAINT only_one_allocation_per_month_and_budget UNIQUE (category_id, month, year),
        CONSTRAINT no_negative_month CHECK ( month > 0),
        CONSTRAINT no_invalid_month CHECK ( month < 13)
);
