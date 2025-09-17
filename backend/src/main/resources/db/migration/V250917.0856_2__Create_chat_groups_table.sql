-- Create chat_groups table
CREATE TABLE chat_groups (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    avatar_url VARCHAR(500),
    owner_id BIGINT NOT NULL,
    settings JSONB DEFAULT '{}',
    announcement JSONB DEFAULT '{}',
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_chat_groups_owner_id FOREIGN KEY (
        owner_id
    ) REFERENCES users (id) ON DELETE CASCADE
);

-- Create index for owner_id
CREATE INDEX idx_chat_groups_owner_id ON chat_groups (owner_id);

-- Create index for created_at
CREATE INDEX idx_chat_groups_created_at ON chat_groups (created_at);

-- Create trigger for updated_at
CREATE TRIGGER update_chat_groups_updated_at
BEFORE UPDATE ON chat_groups
FOR EACH ROW
EXECUTE FUNCTION UPDATE_UPDATED_AT_COLUMN();
