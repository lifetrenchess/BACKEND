-- Password is 'Ayush@1806' encoded with BCrypt
INSERT INTO users (user_name, user_email, user_password, user_role, user_contact_number, active)
SELECT 'System Admin', 'lifetrenchess@gmail.com', '$2a$10$glJDx/VGIc6gHvpxDRRGs.PTPwMNZyG0VDUoNc5GiB0ifg1y6ZAI.', 'ADMIN', '1234567890', true
WHERE NOT EXISTS (SELECT 1 FROM users WHERE user_role = 'ADMIN');
 
-- Insert agent 1 //Janu@123
INSERT INTO users (user_name, user_email, user_password, user_role, user_contact_number, active)
SELECT 'Insurance Agent ', 'insuranceagent@aventra.com', '$2a$12$x2DwVmi3J3L2CyzExIzCVOBhtW2h16M4KCgGqk2X8EHOREI7JBEzS', 'TRAVEL_AGENT', '8247345590', true
WHERE NOT EXISTS (SELECT 1 FROM users WHERE user_email = 'agent1@aventra.com');
 
-- Insert agent 2 //Juhi@123
INSERT INTO users (user_name, user_email, user_password, user_role, user_contact_number, active)
SELECT 'Booking Agent', 'bookingagent@aventra.com', '$2a$12$pyOZIqKkyfscMwYE48IezOjqCPLOpVfDwtkK7GRSScxtx7sdm7oLC', 'TRAVEL_AGENT', '8438523301', true
WHERE NOT EXISTS (SELECT 1 FROM users WHERE user_email = 'agent2@aventra.com');
 
-- Insert agent 3 //Ananya@123
INSERT INTO users (user_name, user_email, user_password, user_role, user_contact_number, active)
SELECT 'Review Agent', 'reviewagent@aventra.com', '$2a$12$w7ByavKVPy3nU/k2Ygj7heZ3n8gwX2O04zFAlukf0fKV6PBW9TCNe', 'TRAVEL_AGENT', '7483010256', true
WHERE NOT EXISTS (SELECT 1 FROM users WHERE user_email = 'agent3@aventra.com');
 
-- Insert agent 4 //Swathi@123
INSERT INTO users (user_name, user_email, user_password, user_role, user_contact_number, active)
SELECT ' Package Agent ', 'packageagent@aventra.com', '$2a$12$V4/9QlJXTJ1aC0B7lSSH4eURG29hdRnPiOw3YQoe1qpca/gVUjQQa', 'TRAVEL_AGENT', '9444444444', true
WHERE NOT EXISTS (SELECT 1 FROM users WHERE user_email = 'agent4@aventra.com');
 
 
 