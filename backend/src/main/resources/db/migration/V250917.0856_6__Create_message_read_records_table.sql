-- Create message_read_records table
CREATE TABLE message_read_records (
    id BIGSERIAL PRIMARY KEY,
    message_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    read_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_message_read_records_message_id FOREIGN KEY (
        message_id
    ) REFERENCES messages (id) ON DELETE CASCADE,
    CONSTRAINT fk_message_read_records_user_id FOREIGN KEY (
        user_id
    ) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT uc_message_read_records_message_user UNIQUE (message_id, user_id)
);

-- Create index for message_id
CREATE INDEX idx_message_read_records_message_id ON message_read_records (
    message_id
);

-- Create index for user_id
CREATE INDEX idx_message_read_records_user_id ON message_read_records (user_id);

-- Create index for read_at
CREATE INDEX idx_message_read_records_read_at ON message_read_records (read_at);

-- Create composite index for user and read_at (for finding unread messages)
CREATE INDEX idx_message_read_records_user_read_at ON message_read_records (
    user_id, read_at
);
