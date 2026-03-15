-- Seed default admin and viewer users for development/testing
-- Password for both: Password123 (BCrypt hash)
INSERT INTO users (id, email, password_hash, first_name, last_name, role, status, created_at, updated_at)
VALUES
    (gen_random_uuid(), 'admin@fintech.com',
     '$2a$10$s/icl9R67Rx7udZGtcEo8.qq8zVFThR62swB5VTTQmW7fLi8ckcWq',
     'Admin', 'User', 'ADMIN', 'ACTIVE', NOW(), NOW()),
    (gen_random_uuid(), 'viewer@fintech.com',
     '$2a$10$s/icl9R67Rx7udZGtcEo8.qq8zVFThR62swB5VTTQmW7fLi8ckcWq',
     'Viewer', 'User', 'VIEWER', 'ACTIVE', NOW(), NOW())
ON CONFLICT (email) DO NOTHING;
