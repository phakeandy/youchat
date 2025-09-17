-- Create messages table
CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    conversation_id VARCHAR(100) NOT NULL,
    sender_id BIGINT NOT NULL,
    type VARCHAR(20) NOT NULL DEFAULT 'text',
    content JSONB NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_messages_sender_id FOREIGN KEY (sender_id) REFERENCES users (
        id
    ) ON DELETE CASCADE
);

-- Create index for conversation_id
CREATE INDEX idx_messages_conversation_id ON messages (conversation_id);

-- Create index for sender_id
CREATE INDEX idx_messages_sender_id ON messages (sender_id);

-- Create index for created_at
CREATE INDEX idx_messages_created_at ON messages (created_at);

-- Create index for type
CREATE INDEX idx_messages_type ON messages (type);

-- Create composite index for conversation and created_at (for ordering)
CREATE INDEX idx_messages_conversation_created_at ON messages (
    conversation_id, created_at
);

-- Create trigger for updated_at
CREATE TRIGGER update_messages_updated_at
BEFORE UPDATE ON messages
FOR EACH ROW
EXECUTE FUNCTION UPDATE_UPDATED_AT_COLUMN();
