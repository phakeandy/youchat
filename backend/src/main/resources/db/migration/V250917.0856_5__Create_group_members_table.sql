-- Create group_members table
CREATE TABLE group_members (
    id BIGSERIAL PRIMARY KEY,
    group_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'member',
    settings JSONB DEFAULT '{}',
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_group_members_group_id FOREIGN KEY (
        group_id
    ) REFERENCES chat_groups (id) ON DELETE CASCADE,
    CONSTRAINT fk_group_members_user_id FOREIGN KEY (
        user_id
    ) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT uc_group_members_group_user UNIQUE (group_id, user_id)
);

-- Create index for group_id
CREATE INDEX idx_group_members_group_id ON group_members (group_id);

-- Create index for user_id
CREATE INDEX idx_group_members_user_id ON group_members (user_id);

-- Create index for role
CREATE INDEX idx_group_members_role ON group_members (role);

-- Create trigger for updated_at
CREATE TRIGGER update_group_members_updated_at
BEFORE UPDATE ON group_members
FOR EACH ROW
EXECUTE FUNCTION UPDATE_UPDATED_AT_COLUMN();
