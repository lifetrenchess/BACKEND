-- Add active column to travel_package table (MySQL doesn't support IF NOT EXISTS in ADD COLUMN)
-- This will be handled by JPA/Hibernate schema generation instead

-- Insert sample packages with active status
INSERT INTO travel_package (title, description, duration, price, include_service, main_image, highlights, active) VALUES
('Paris Adventure', 'Experience the magic of Paris with guided tours of the Eiffel Tower, Louvre Museum, and Notre-Dame Cathedral.', 7, 2500.00, 'Flights, Hotel, Guided Tours, Breakfast', 'paris.jpg', 'Eiffel Tower, Louvre Museum, Seine River Cruise', TRUE),
('Tokyo Discovery', 'Explore the vibrant culture of Tokyo with visits to Shibuya, Akihabara, and traditional temples.', 10, 3200.00, 'Flights, Hotel, Metro Pass, Cultural Tours', 'tokyo.jpg', 'Shibuya Crossing, Senso-ji Temple, Tsukiji Market', TRUE),
('New York City Experience', 'Discover the Big Apple with Broadway shows, Central Park, and iconic landmarks.', 8, 2800.00, 'Flights, Hotel, Broadway Tickets, City Tours', 'nyc.jpg', 'Times Square, Central Park, Statue of Liberty', TRUE),
('London Heritage Tour', 'Immerse yourself in British history with visits to Buckingham Palace, Tower of London, and Big Ben.', 9, 2900.00, 'Flights, Hotel, Royal Palace Tour, Afternoon Tea', 'london.jpg', 'Buckingham Palace, Tower Bridge, Westminster Abbey', TRUE),
('Rome Ancient Wonders', 'Step back in time with tours of the Colosseum, Vatican City, and Roman Forum.', 6, 2200.00, 'Flights, Hotel, Vatican Tour, Colosseum Access', 'rome.jpg', 'Colosseum, Vatican Museums, Trevi Fountain', TRUE),
('Barcelona Culture', 'Experience Catalan culture with Gaudi architecture, tapas tours, and Mediterranean beaches.', 7, 2400.00, 'Flights, Hotel, Gaudi Tour, Tapas Experience', 'barcelona.jpg', 'Sagrada Familia, Park Güell, La Rambla', TRUE),
('Amsterdam Canals', 'Discover the charm of Amsterdam with canal cruises, Anne Frank House, and Dutch museums.', 5, 1800.00, 'Flights, Hotel, Canal Cruise, Museum Pass', 'amsterdam.jpg', 'Anne Frank House, Van Gogh Museum, Canal District', TRUE),
('Prague Castle Tour', 'Explore the medieval beauty of Prague with castle tours, Charles Bridge, and Old Town.', 6, 2000.00, 'Flights, Hotel, Castle Tour, Old Town Walk', 'prague.jpg', 'Prague Castle, Charles Bridge, Old Town Square', TRUE),
('Vienna Classical', 'Experience classical music and imperial history in Vienna with palace tours and concerts.', 7, 2600.00, 'Flights, Hotel, Palace Tour, Classical Concert', 'vienna.jpg', 'Schönbrunn Palace, Vienna State Opera, Belvedere', TRUE),
('Budapest Thermal Baths', 'Relax in thermal baths and explore the historic architecture of Budapest.', 5, 1900.00, 'Flights, Hotel, Thermal Bath Pass, City Tour', 'budapest.jpg', 'Széchenyi Baths, Buda Castle, Chain Bridge', TRUE)
ON DUPLICATE KEY UPDATE active = TRUE; 