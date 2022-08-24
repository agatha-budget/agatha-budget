CREATE TABLE category_identifier (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    budget_id VARCHAR(36) NOT NULL,
    pattern VARCHAR(36) NOT NULL,
    category_id VARCHAR(36)
);