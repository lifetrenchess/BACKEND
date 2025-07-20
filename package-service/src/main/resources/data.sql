data.sql in package-service:        -- =========================
-- Package 1: Paris Adventure
-- =========================
INSERT IGNORE INTO travel_package (package_id, title, description, duration, price, include_service, exclude_service, main_image, highlights, active, destination)
VALUES (1, 'Paris Adventure', 'Experience the magic of Paris with guided tours of the Eiffel Tower, Louvre Museum, and Notre-Dame Cathedral.', 7, 4000.00, 'Flights, Hotel, Guided Tours, Breakfast', 'Lunch, Personal Expenses', 'https://images.unsplash.com/photo-1502602898657-3e91760cbb34?auto=format&amp;fit=crop&amp;w=800&amp;q=80', 'Eiffel Tower, Louvre Museum, Seine River Cruise, Montmartre', TRUE, 'Paris');
 
INSERT IGNORE INTO flight (airline, departure, arrival, departure_time, arrival_time, package_id) VALUES
('Air France', 'DEL (Indira Gandhi International Airport, New Delhi)', 'CDG (Charles de Gaulle Airport, Paris)', '2025-07-01 10:15 AM', '2025-07-01 06:30 PM', 1),
('Air France', 'CDG (Charles de Gaulle Airport, Paris)', 'DEL (Indira Gandhi International Airport, New Delhi)', '2025-07-07 11:00 AM', '2025-07-07 09:45 PM', 1);
 
INSERT IGNORE INTO hotel (name, location, star_rating, check_in_time, check_out_time, package_id) VALUES
('Shangri-La Paris', 'Paris', 5, '02:00 PM', '12:00 PM', 1);
 
INSERT IGNORE INTO sightseeing (place_name, description, time, package_id) VALUES
('Eiffel Tower', 'Visit the iconic Eiffel Tower with summit access', '2025-07-02 09:00 AM', 1),
('Louvre Museum', 'Tour the world-famous Louvre with skip-the-line tickets', '2025-07-03 10:00 AM', 1),
('Montmartre', 'Explore the artistic hilltop district and Sacré-Cœur Basilica', '2025-07-04 11:00 AM', 1);
 
-- =========================
-- Package 2: Tokyo Discovery
-- =========================
INSERT IGNORE INTO travel_package (package_id, title, description, duration, price, include_service, exclude_service, main_image, highlights, active, destination)
VALUES (2, 'Tokyo Discovery', 'Explore the vibrant culture of Tokyo with visits to Shibuya, Akihabara, and traditional temples.', 10, 10200.00, 'Flights, Hotel, Metro Pass, Cultural Tours', 'Lunch, Personal Expenses', 'https://images.unsplash.com/photo-1464983953574-0892a716854b?auto=format&amp;fit=crop&amp;w=800&amp;q=80', 'Shibuya Crossing, Senso-ji Temple, Tsukiji Market, Meiji Shrine', TRUE, 'Tokyo');
 
INSERT IGNORE INTO flight (airline, departure, arrival, departure_time, arrival_time, package_id) VALUES
('Japan Airlines', 'DEL (Indira Gandhi International Airport, New Delhi)', 'NRT (Narita International Airport, Tokyo)', '2025-08-01 09:30 AM', '2025-08-01 08:00 PM', 2),
('Japan Airlines', 'NRT (Narita International Airport, Tokyo)', 'DEL (Indira Gandhi International Airport, New Delhi)', '2025-08-10 10:00 AM', '2025-08-10 07:45 PM', 2);
 
INSERT IGNORE INTO hotel (name, location, star_rating, check_in_time, check_out_time, package_id) VALUES
('The Okura Tokyo', 'Tokyo', 5, '03:00 PM', '11:00 AM', 2);
 
INSERT IGNORE INTO sightseeing (place_name, description, time, package_id) VALUES
('Senso-ji Temple', 'Visit the oldest temple in Tokyo with a cultural guide', '2025-08-02 09:00 AM', 2),
('Akihabara', 'Explore the electronics and anime district with local insights', '2025-08-03 02:00 PM', 2),
('Meiji Shrine', 'Peaceful walk through the forested shrine grounds', '2025-08-04 10:00 AM', 2);
 
-- =========================
-- Package 3: New York City Experience
-- =========================
INSERT IGNORE  INTO travel_package (package_id, title, description, duration, price, include_service, exclude_service, main_image, highlights, active, destination)
VALUES (3, 'New York City Experience', 'Discover the Big Apple with Broadway shows, Central Park, and iconic landmarks.', 8, 30000.00, 'Flights, Hotel, Broadway Tickets, City Tours', 'Lunch, Personal Expenses', 'https://images.unsplash.com/photo-1469474968028-56623f02e42e?auto=format&amp;fit=crop&amp;w=800&amp;q=80', 'Times Square, Central Park, Statue of Liberty, Empire State Building', TRUE, 'New York');
 
INSERT IGNORE INTO flight (airline, departure, arrival, departure_time, arrival_time, package_id) VALUES
('United Airlines', 'DEL (Indira Gandhi International Airport, New Delhi)', 'JFK (John F. Kennedy International Airport, New York)', '2025-09-01 08:45 AM', '2025-09-01 06:30 PM', 3),
('United Airlines', 'JFK (John F. Kennedy International Airport, New York)', 'DEL (Indira Gandhi International Airport, New Delhi)', '2025-09-08 01:00 PM', '2025-09-09 11:00 AM', 3);
 
INSERT IGNORE INTO hotel (name, location, star_rating, check_in_time, check_out_time, package_id) VALUES
('The Peninsula New York', 'New York', 5, '03:00 PM', '12:00 PM', 3);
 
INSERT IGNORE  INTO sightseeing (place_name, description, time, package_id) VALUES
('Statue of Liberty', 'Ferry tour to the Statue of Liberty and Ellis Island', '2025-09-02 10:00 AM', 3),
('Central Park', 'Guided walk through Central Park with a local historian', '2025-09-03 11:00 AM', 3),
('Empire State Building', 'Observation deck visit with skyline views', '2025-09-04 12:00 PM', 3);
 
-- =========================
-- Package 4: London Heritage Tour
-- =========================
INSERT IGNORE INTO travel_package (package_id, title, description, duration, price, include_service, exclude_service, main_image, highlights, active, destination)
VALUES (4, 'London Heritage Tour', 'Immerse yourself in British history with visits to Buckingham Palace, Tower of London, and Big Ben.', 9, 19300.00, 'Flights, Hotel, Royal Palace Tour, Afternoon Tea', 'Lunch, Personal Expenses', 'https://images.unsplash.com/photo-1465101178521-c1a9136a3b99?auto=format&amp;fit=crop&amp;w=800&amp;q=80', 'Buckingham Palace, Tower Bridge, Westminster Abbey, London Eye', TRUE, 'London');
 
INSERT IGNORE INTO flight (airline, departure, arrival, departure_time, arrival_time, package_id) VALUES
('British Airways', 'DEL (Indira Gandhi International Airport, New Delhi)', 'LHR (London Heathrow Airport, London)', '2025-10-01 09:00 AM', '2025-10-01 03:30 PM', 4),
('British Airways', 'LHR (London Heathrow Airport, London)', 'DEL (Indira Gandhi International Airport, New Delhi)', '2025-10-09 05:00 PM', '2025-10-10 05:30 AM', 4);
 
INSERT IGNORE INTO hotel (name, location, star_rating, check_in_time, check_out_time, package_id) VALUES
('The Langham London', 'London', 5, '02:00 PM', '12:00 PM', 4);
 
INSERT IGNORE INTO sightseeing (place_name, description, time, package_id) VALUES
('Buckingham Palace', 'Tour of the royal residence with Changing of the Guard', '2025-10-02 09:00 AM', 4),
('Tower of London', 'Visit the historic Tower and Crown Jewels', '2025-10-03 10:00 AM', 4),
('London Eye', 'Ride the iconic Ferris wheel with panoramic views', '2025-10-04 12:00 PM', 4);
 
-- =========================
-- Package 5: Rome Ancient Wonders
-- =========================
INSERT IGNORE INTO travel_package (package_id, title, description, duration, price, include_service, exclude_service, main_image, highlights, active, destination)
VALUES (5, 'Rome Ancient Wonders', 'Step back in time with tours of the Colosseum, Vatican City, and Roman Forum.', 6, 19999.00, 'Flights, Hotel, Vatican Tour, Colosseum Access', 'Lunch, Personal Expenses', 'https://images.unsplash.com/photo-1509395176047-4a66953fd231?auto=format&amp;fit=crop&amp;w=800&amp;q=80', 'Colosseum, Vatican Museums, Trevi Fountain, Pantheon', TRUE, 'Rome');
 
INSERT IGNORE INTO flight (airline, departure, arrival, departure_time, arrival_time, package_id) VALUES
('ITA Airways', 'DEL (Indira Gandhi International Airport, New Delhi)', 'FCO (Leonardo da Vinci–Fiumicino Airport, Rome)', '2025-11-01 07:30 AM', '2025-11-01 01:45 PM', 5),
('ITA Airways', 'FCO (Leonardo da Vinci–Fiumicino Airport, Rome)', 'DEL (Indira Gandhi International Airport, New Delhi)', '2025-11-06 06:00 PM', '2025-11-07 06:30 AM', 5);
 
INSERT IGNORE INTO hotel (name, location, star_rating, check_in_time, check_out_time, package_id) VALUES
('Rome Cavalieri, A Waldorf Astoria Hotel', 'Rome', 5, '02:00 PM', '12:00 PM', 5);
 
INSERT IGNORE INTO sightseeing (place_name, description, time, package_id) VALUES
('Colosseum', 'Guided tour of the Colosseum and Roman Forum', '2025-11-02 09:00 AM', 5),
('Vatican Museums', 'Tour of the Vatican Museums and Sistine Chapel', '2025-11-03 10:00 AM', 5),
('Pantheon', 'Visit the ancient Roman temple turned church', '2025-11-04 11:00 AM', 5);
 
-- =========================
-- Package 6: Barcelona Culture
-- =========================
INSERT IGNORE INTO travel_package (package_id, title, description, duration, price, include_service, exclude_service, main_image, highlights, active, destination)
VALUES (6, 'Barcelona Culture', 'Experience Catalan culture with Gaudi architecture, tapas tours, and Mediterranean beaches.', 7, 18000.00, 'Flights, Hotel, Gaudi Tour, Tapas Experience', 'Lunch, Personal Expenses', 'https://images.unsplash.com/photo-1467269204594-9661b134dd2b?auto=format&amp;fit=crop&amp;w=800&amp;q=80', 'Sagrada Familia, Park Güell, La Rambla, Gothic Quarter', TRUE, 'Barcelona');
 
INSERT IGNORE INTO flight (airline, departure, arrival, departure_time, arrival_time, package_id) VALUES
('Etihad Airways', 'DEL (Indira Gandhi International Airport, New Delhi)', 'BCN (Barcelona El Prat Airport, Barcelona)', '2025-12-01 04:50 AM', '2025-12-01 06:30 PM', 6),
('Etihad Airways', 'BCN (Barcelona El Prat Airport, Barcelona)', 'DEL (Indira Gandhi International Airport, New Delhi)', '2025-12-07 06:00 PM', '2025-12-08 08:00 AM', 6);
 
INSERT IGNORE INTO hotel (name, location, star_rating, check_in_time, check_out_time, package_id) VALUES
('W Barcelona', 'Barcelona', 5, '03:00 PM', '12:00 PM', 6);
 
INSERT IGNORE INTO sightseeing (place_name, description, time, package_id) VALUES
('Sagrada Familia', 'Tour of Gaudi’s masterpiece with guide', '2025-12-02 09:00 AM', 6),
('Park Güell', 'Walk through Park Güell and Gaudi’s mosaic art', '2025-12-03 10:00 AM', 6),
('Gothic Quarter', 'Explore medieval streets and tapas tasting', '2025-12-04 11:00 AM', 6);
 
 
-- =========================
-- Package 7: Amsterdam Canals
-- =========================
INSERT IGNORE INTO travel_package (package_id, title, description, duration, price, include_service, exclude_service, main_image, highlights, active, destination)
VALUES (7, 'Amsterdam Canals', 'Discover the charm of Amsterdam with canal cruises, Anne Frank House, and Dutch museums.', 5, 21500.00, 'Flights, Hotel, Canal Cruise, Museum Pass', 'Lunch, Personal Expenses', 'https://images.unsplash.com/photo-1501785888041-af3ef285b470?auto=format&amp;fit=crop&amp;w=800&amp;q=80', 'Anne Frank House, Van Gogh Museum, Canal District, Rijksmuseum', TRUE, 'Amsterdam');
 
INSERT IGNORE INTO flight (airline, departure, arrival, departure_time, arrival_time, package_id) VALUES
('KLM', 'DEL (Indira Gandhi International Airport, New Delhi)', 'AMS (Amsterdam Schiphol Airport, Amsterdam)', '2025-01-01 01:00 PM', '2025-01-01 07:30 PM', 7),
('KLM', 'AMS (Amsterdam Schiphol Airport, Amsterdam)', 'DEL (Indira Gandhi International Airport, New Delhi)', '2025-01-05 09:00 AM', '2025-01-05 11:30 PM', 7);
 
INSERT IGNORE INTO hotel (name, location, star_rating, check_in_time, check_out_time, package_id) VALUES
('Pulitzer Amsterdam', 'Amsterdam', 5, '02:00 PM', '12:00 PM', 7);
 
INSERT IGNORE INTO sightseeing (place_name, description, time, package_id) VALUES
('Anne Frank House', 'Tour the Anne Frank House with audio guide', '2025-01-02 09:00 AM', 7),
('Van Gogh Museum', 'Visit the Van Gogh Museum with skip-the-line access', '2025-01-03 10:00 AM', 7),
('Canal Cruise', 'Evening canal cruise with commentary', '2025-01-04 06:00 PM', 7);
 
-- =========================
-- Package 8: Prague Castle Tour
-- =========================
INSERT IGNORE INTO travel_package (package_id, title, description, duration, price, include_service, exclude_service, main_image, highlights, active, destination)
VALUES (8, 'Prague Castle Tour', 'Explore the medieval beauty of Prague with castle tours, Charles Bridge, and Old Town.', 6, 11400.00, 'Flights, Hotel, Castle Tour, Old Town Walk', 'Lunch, Personal Expenses', 'https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&amp;fit=crop&amp;w=800&amp;q=80', 'Prague Castle, Charles Bridge, Old Town Square, Astronomical Clock', TRUE, 'Prague');
 
INSERT IGNORE INTO flight (airline, departure, arrival, departure_time, arrival_time, package_id) VALUES
('Czech Airlines', 'DEL (Indira Gandhi International Airport, New Delhi)', 'PRG (Václav Havel Airport, Prague)', '2025-02-01 02:00 PM', '2025-02-01 08:30 PM', 8),
('Czech Airlines', 'PRG (Václav Havel Airport, Prague)', 'DEL (Indira Gandhi International Airport, New Delhi)', '2025-02-06 10:00 AM', '2025-02-06 11:30 PM', 8);
 
INSERT IGNORE INTO hotel (name, location, star_rating, check_in_time, check_out_time, package_id) VALUES
('Hotel Kings Court', 'Prague', 5, '03:00 PM', '12:00 PM', 8);
 
INSERT IGNORE INTO sightseeing (place_name, description, time, package_id) VALUES
('Prague Castle', 'Tour of Prague Castle and St. Vitus Cathedral', '2025-02-02 09:00 AM', 8),
('Charles Bridge', 'Walk across Charles Bridge with historical insights', '2025-02-03 10:00 AM', 8),
('Old Town Square', 'Explore the Astronomical Clock and historic buildings', '2025-02-04 11:00 AM', 8);
 
-- =========================
-- Package 9: Vienna Classical
-- =========================
INSERT IGNORE  INTO travel_package (package_id, title, description, duration, price, include_service, exclude_service, main_image, highlights, active, destination)
VALUES (9, 'Vienna Classical', 'Experience classical music and imperial history in Vienna with palace tours and concerts.', 7, 19200.00, 'Flights, Hotel, Palace Tour, Classical Concert', 'Lunch, Personal Expenses', 'https://images.unsplash.com/photo-1464983953574-0892a716854b?auto=format&amp;fit=crop&amp;w=800&amp;q=80', 'Schönbrunn Palace, Vienna State Opera, Belvedere, Hofburg Palace', TRUE, 'Vienna');
 
INSERT IGNORE INTO flight (airline, departure, arrival, departure_time, arrival_time, package_id) VALUES
('Austrian Airlines', 'DEL (Indira Gandhi International Airport, New Delhi)', 'VIE (Vienna International Airport, Vienna)', '2025-03-01 03:00 PM', '2025-03-01 09:30 PM', 9),
('Austrian Airlines', 'VIE (Vienna International Airport, Vienna)', 'DEL (Indira Gandhi International Airport, New Delhi)', '2025-03-07 11:00 AM', '2025-03-07 11:30 PM', 9);
 
INSERT IGNORE  INTO hotel (name, location, star_rating, check_in_time, check_out_time, package_id) VALUES
('Hotel Sacher Wien', 'Vienna', 5, '02:00 PM', '12:00 PM', 9);
 
INSERT IGNORE INTO sightseeing (place_name, description, time, package_id) VALUES
('Schönbrunn Palace', 'Tour of Schönbrunn Palace and gardens', '2025-03-02 09:00 AM', 9),
('Vienna State Opera', 'Attend a classical concert at the Opera House', '2025-03-03 07:00 PM', 9),
('Belvedere Palace', 'Visit the museum and gardens of Belvedere', '2025-03-04 10:00 AM', 9);
 
-- =========================
-- Package 10: Budapest Thermal Baths
-- =========================
INSERT IGNORE INTO travel_package (package_id, title, description, duration, price, include_service, exclude_service, main_image, highlights, active, destination)
VALUES (10, 'Budapest Thermal Baths', 'Relax in thermal baths and explore the historic architecture of Budapest.', 5, 18300.00, 'Flights, Hotel, Thermal Bath Pass, City Tour', 'Lunch, Personal Expenses', 'https://images.unsplash.com/photo-1465101178521-c1a9136a3b99?auto=format&amp;fit=crop&amp;w=800&amp;q=80', 'Széchenyi Baths, Buda Castle, Chain Bridge, Parliament Building', TRUE, 'Budapest');
 
INSERT IGNORE INTO flight (airline, departure, arrival, departure_time, arrival_time, package_id) VALUES
('Wizz Air', 'DEL (Indira Gandhi International Airport, New Delhi)', 'BUD (Budapest Ferenc Liszt International Airport, Budapest)', '2025-04-01 04:00 PM', '2025-04-01 10:00 PM', 10),
('Wizz Air', 'BUD (Budapest Ferenc Liszt International Airport, Budapest)', 'DEL (Indira Gandhi International Airport, New Delhi)', '2025-04-05 10:00 AM', '2025-04-05 11:30 PM', 10);
 
INSERT IGNORE INTO hotel (name, location, star_rating, check_in_time, check_out_time, package_id) VALUES
('Four Seasons Gresham Palace', 'Budapest', 5, '03:00 PM', '12:00 PM', 10);
 
INSERT IGNORE INTO sightseeing (place_name, description, time, package_id) VALUES
('Széchenyi Baths', 'Relax in the famous thermal baths with full-day access', '2025-04-02 09:00 AM', 10),
('Buda Castle', 'Tour of Buda Castle and historical museum', '2025-04-03 10:00 AM', 10),
('Chain Bridge', 'Evening walk across the illuminated Chain Bridge', '2025-04-04 07:00 PM', 10);
 
 
-- =========================
-- Package 11: Santorini Dream
-- =========================
INSERT IGNORE INTO travel_package (package_id, title, description, duration, price, include_service, exclude_service, main_image, highlights, active, destination)
VALUES (11, 'Santorini Dream', 'Discover the stunning beauty of Santorini with its iconic white buildings and blue domes.', 8, 10900.00, 'Flights, Hotel, Wine Tasting, Sunset Cruise', 'Lunch, Personal Expenses', 'https://images.unsplash.com/photo-1500534314209-a25ddb2bd429?auto=format&amp;fit=crop&amp;w=800&amp;q=80', 'Oia Sunset, Fira Town, Ancient Thera, Caldera Cruise', TRUE, 'Santorini');
 
INSERT IGNORE INTO flight (airline, departure, arrival, departure_time, arrival_time, package_id) VALUES
('Emirates', 'DEL (Indira Gandhi International Airport, New Delhi)', 'JTR (Santorini Airport, Greece)', '2025-05-01 10:00 AM', '2025-05-01 06:30 PM', 11),
('Emirates', 'JTR (Santorini Airport, Greece)', 'DEL (Indira Gandhi International Airport, New Delhi)', '2025-05-08 12:00 PM', '2025-05-08 09:00 PM', 11);
 
INSERT IGNORE INTO hotel (name, location, star_rating, check_in_time, check_out_time, package_id) VALUES
('Canaves Oia Suites', 'Santorini', 5, '02:00 PM', '12:00 PM', 11);
 
INSERT IGNORE INTO sightseeing (place_name, description, time, package_id) VALUES
('Oia', 'Watch the famous Santorini sunset from the cliffs', '2025-05-02 07:00 PM', 11),
('Ancient Thera', 'Explore the ruins of Ancient Thera with a guide', '2025-05-03 10:00 AM', 11),
('Caldera Cruise', 'Luxury sunset cruise around the Santorini caldera', '2025-05-04 05:00 PM', 11);
 
-- =========================
-- Package 12: Machu Picchu Trek
-- =========================
INSERT IGNORE INTO travel_package (package_id, title, description, duration, price, include_service, exclude_service, main_image, highlights, active, destination)
VALUES (12, 'Machu Picchu Trek', 'Embark on an unforgettable journey to the ancient Incan citadel of Machu Picchu.', 12, 16200.00, 'Flights, Hotels, Trek Guide, Train Tickets, Cultural Tours', 'Lunch, Personal Expenses', 'https://images.unsplash.com/photo-1502086223501-7ea6ecd79368?auto=format&amp;fit=crop&amp;w=800&amp;q=80', 'Machu Picchu, Sacred Valley, Cusco, Ollantaytambo', TRUE, 'Peru');
 
INSERT IGNORE INTO flight (airline, departure, arrival, departure_time, arrival_time, package_id) VALUES
('Qatar Airways', 'DEL (Indira Gandhi International Airport, New Delhi)', 'LIM (Jorge Chávez International Airport, Lima)', '2025-06-01 08:00 AM', '2025-06-01 11:00 PM', 12),
('Qatar Airways', 'LIM (Jorge Chávez International Airport, Lima)', 'DEL (Indira Gandhi International Airport, New Delhi)', '2025-06-12 05:00 PM', '2025-06-13 09:00 AM', 12);
 
INSERT IGNORE INTO hotel (name, location, star_rating, check_in_time, check_out_time, package_id) VALUES
('Belmond Sanctuary Lodge', 'Machu Picchu', 5, '02:00 PM', '12:00 PM', 12);
 
INSERT IGNORE INTO sightseeing (place_name, description, time, package_id) VALUES
('Machu Picchu', 'Guided tour of Machu Picchu with expert historian', '2025-06-03 09:00 AM', 12),
('Sacred Valley', 'Explore the Sacred Valley and Pisac ruins', '2025-06-04 10:00 AM', 12),
('Cusco City', 'Walking tour of Cusco’s colonial architecture', '2025-06-05 11:00 AM', 12);
 
-- =========================
-- Package 13: Sydney Coastal
-- =========================
INSERT IGNORE INTO travel_package (package_id, title, description, duration, price, include_service, exclude_service, main_image, highlights, active, destination)
VALUES (13, 'Sydney Coastal', 'Explore the stunning coastal beauty of Sydney.', 9, 14600.00, 'Flights, Hotel, Opera House Tour, Bridge Climb, Beach Tours', 'Lunch, Personal Expenses', 'https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&amp;fit=crop&amp;w=800&amp;q=80', 'Sydney Opera House, Harbour Bridge, Bondi Beach, Darling Harbour', TRUE, 'Sydney');
 
INSERT IGNORE INTO flight (airline, departure, arrival, departure_time, arrival_time, package_id) VALUES
('Qantas', 'DEL (Indira Gandhi International Airport, New Delhi)', 'SYD (Sydney Kingsford Smith Airport, Australia)', '2025-07-01 10:00 AM', '2025-07-02 06:00 AM', 13),
('Qantas', 'SYD (Sydney Kingsford Smith Airport, Australia)', 'DEL (Indira Gandhi International Airport, New Delhi)', '2025-07-10 08:00 PM', '2025-07-11 06:00 AM', 13);
 
INSERT IGNORE INTO hotel (name, location, star_rating, check_in_time, check_out_time, package_id) VALUES
('Shangri-La Hotel Sydney', 'Sydney', 5, '03:00 PM', '12:00 PM', 13);
 
INSERT IGNORE INTO sightseeing (place_name, description, time, package_id) VALUES
('Sydney Opera House', 'Tour of the Opera House with backstage access', '2025-07-02 10:00 AM', 13),
('Bondi Beach', 'Relax and surf at Bondi Beach', '2025-07-03 12:00 PM', 13),
('Harbour Bridge Climb', 'Climb the Sydney Harbour Bridge for panoramic views', '2025-07-04 09:00 AM', 13);
 
-- =========================
-- Package 14: Dubai Luxury
-- =========================
INSERT  IGNORE INTO travel_package (package_id, title, description, duration, price, include_service, exclude_service, main_image, highlights, active, destination)
VALUES (14, 'Dubai Luxury', 'Experience the ultimate luxury in Dubai.', 6, 13600.00, 'Flights, 5-Star Hotel, Desert Safari, Burj Khalifa Access, Shopping Tours', 'Lunch, Personal Expenses', 'https://images.unsplash.com/photo-1501594907352-04cda38ebc29?auto=format&amp;fit=crop&amp;w=800&amp;q=80', 'Burj Khalifa, Palm Jumeirah, Dubai Mall, Desert Safari', TRUE, 'Dubai');
 
INSERT IGNORE  INTO flight (airline, departure, arrival, departure_time, arrival_time, package_id) VALUES
('Emirates', 'DEL (Indira Gandhi International Airport, New Delhi)', 'DXB (Dubai International Airport, UAE)', '2025-08-01 09:00 AM', '2025-08-01 11:30 AM', 14),
('Emirates', 'DXB (Dubai International Airport, UAE)', 'DEL (Indira Gandhi International Airport, New Delhi)', '2025-08-07 08:00 PM', '2025-08-07 11:00 PM', 14);
 
INSERT IGNORE INTO hotel (name, location, star_rating, check_in_time, check_out_time, package_id) VALUES
('Burj Al Arab', 'Dubai', 5, '02:00 PM', '12:00 PM', 14);
 
INSERT IGNORE  INTO sightseeing (place_name, description, time, package_id) VALUES
('Burj Khalifa', 'Visit the world’s tallest building with observation deck access', '2025-08-02 10:00 AM', 14),
('Desert Safari', 'Enjoy a desert safari with dune bashing and dinner', '2025-08-03 04:00 PM', 14),
('Dubai Mall', 'Shopping and aquarium visit at Dubai Mall', '2025-08-04 11:00 AM', 14);
 
-- =========================
-- Package 15: Bali Paradise
-- =========================
INSERT IGNORE INTO travel_package (package_id, title, description, duration, price, include_service, exclude_service, main_image, highlights, active, destination)
VALUES (15, 'Bali Paradise', 'Escape to the tropical paradise of Bali.', 10, 23400.00, 'Flights, Resort, Airport Transfers, Spa Treatment, Cultural Tours', 'Lunch, Personal Expenses', 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&amp;fit=crop&amp;w=800&amp;q=80', 'Ubud Rice Terraces, Tanah Lot Temple, Nusa Penida, Seminyak Beach', TRUE, 'Bali');
 
INSERT IGNORE INTO flight (airline, departure, arrival, departure_time, arrival_time, package_id) VALUES
('Garuda Indonesia', 'DEL (Indira Gandhi International Airport, New Delhi)', 'DPS (Ngurah Rai International Airport, Bali)', '2025-09-01 08:00 AM', '2025-09-01 06:00 PM', 15),
('Garuda Indonesia', 'DPS (Ngurah Rai International Airport, Bali)', 'DEL (Indira Gandhi International Airport, New Delhi)', '2025-09-10 01:00 PM', '2025-09-10 11:00 PM', 15);
 
INSERT IGNORE INTO hotel (name, location, star_rating, check_in_time, check_out_time, package_id) VALUES
('Four Seasons Resort Bali at Sayan', 'Ubud', 5, '02:00 PM', '12:00 PM', 15);
 
INSERT IGNORE INTO sightseeing (place_name, description, time, package_id) VALUES
('Ubud Rice Terraces', 'Tour the lush rice terraces and local villages', '2025-09-02 09:00 AM', 15),
('Tanah Lot Temple', 'Visit the famous sea temple at sunset', '2025-09-03 05:00 PM', 15),
('Nusa Penida', 'Day trip to Nusa Penida island with snorkeling', '2025-09-04 08:00 AM', 15);
 
 
 