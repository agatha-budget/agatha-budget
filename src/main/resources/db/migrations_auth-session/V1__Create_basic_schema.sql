CREATE TABLE IF NOT EXISTS key_value (
    name VARCHAR(128),
    value TEXT,
    created_at_time BIGINT,
    PRIMARY KEY(name)
);

CREATE TABLE IF NOT EXISTS session_info (
    session_handle VARCHAR(255) NOT NULL,
    user_id VARCHAR(128) NOT NULL,
    refresh_token_hash_2 VARCHAR(128) NOT NULL,
    session_data TEXT,
    expires_at BIGINT NOT NULL,
    created_at_time BIGINT NOT NULL,
    jwt_user_payload TEXT,
    PRIMARY KEY(session_handle)
);
