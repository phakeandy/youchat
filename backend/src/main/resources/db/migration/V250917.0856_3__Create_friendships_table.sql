-- Create friendships table
CREATE TABLE friendships (
    user1_id BIGINT NOT NULL,
    user2_id BIGINT NOT NULL,
    extra_info JSONB DEFAULT '{}',
    CONSTRAINT fk_friendships_user1_id FOREIGN KEY (
        user1_id
    ) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_friendships_user2_id FOREIGN KEY (
        user2_id
    ) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT pk_friendships PRIMARY KEY (user1_id, user2_id),
    CONSTRAINT chk_friendships_different_users CHECK (user1_id < user2_id),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Create index for user1_id
CREATE INDEX idx_friendships_user1_id ON friendships (user1_id);

-- Create index for user2_id
CREATE INDEX idx_friendships_user2_id ON friendships (user2_id);

-- Create index for created_at
CREATE INDEX idx_friendships_created_at ON friendships (created_at);

-- Create trigger for updated_at
CREATE TRIGGER update_friendships_updated_at
BEFORE UPDATE ON friendships
FOR EACH ROW
EXECUTE FUNCTION UPDATE_UPDATED_AT_COLUMN();
