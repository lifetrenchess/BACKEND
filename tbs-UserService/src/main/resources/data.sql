-- Insert initial admin if no admin exists
-- Password is 'Ayush@1806' encoded with BCrypt
INSERT INTO users (user_name, user_email, user_password, user_role, user_contact_number, active) 
SELECT 'System Admin', 'lifetrenchess@gmail.com', '$2a$10$glJDx/VGIc6gHvpxDRRGs.PTPwMNZyG0VDUoNc5GiB0ifg1y6ZAI.', 'ADMIN', '1234567890', true
WHERE NOT EXISTS (SELECT 1 FROM users WHERE user_role = 'ADMIN'); 