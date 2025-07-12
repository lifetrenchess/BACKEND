-- Pre-registered Admin and Agent users for Travel Booking System
-- These users are created automatically when the service starts

-- Admin User
INSERT INTO users (username, email, password, role, created_at, updated_at) VALUES
('admin', 'admin@aventra.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ADMIN', NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- Travel Agent User
INSERT INTO users (username, email, password, role, created_at, updated_at) VALUES
('agent1', 'agent1@aventra.com', '$2a$10$8K1p/a0dL1LXMIgoEDFrwOe6g7fKjKqKqKqKqKqKqKqKqKqKqKqKqK', 'AGENT', NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- Sample User
INSERT INTO users (username, email, password, role, created_at, updated_at) VALUES
('user1', 'user1@example.com', '$2a$10$8K1p/a0dL1LXMIgoEDFrwOe6g7fKjKqKqKqKqKqKqKqKqKqKqKqKqK', 'USER', NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW(); 