const axios = require('axios');

const packages = [
  {
    title: "Paris Adventure",
    description: "Experience the magic of the City of Light with our comprehensive Paris tour. Visit iconic landmarks like the Eiffel Tower, Louvre Museum, and Notre-Dame Cathedral. Enjoy romantic Seine River cruises and explore charming neighborhoods like Montmartre.",
    duration: 7,
    price: 1299.99,
    includeService: "Flights, Hotel, Guided Tours, Museum Passes, Transportation",
    image: "https://images.unsplash.com/photo-1502602898536-47ad22581b52?w=800&h=600&fit=crop",
    highlights: "Eiffel Tower, Louvre Museum, Seine River Cruise, Montmartre, Champs-Élysées"
  },
  {
    title: "Tokyo Discovery",
    description: "Immerse yourself in the fascinating blend of tradition and innovation in Tokyo. From ancient temples to cutting-edge technology, experience the best of Japanese culture. Visit Senso-ji Temple, explore Akihabara, and enjoy authentic sushi experiences.",
    duration: 8,
    price: 1899.99,
    includeService: "Flights, Hotel, Metro Pass, Guided Tours, Traditional Tea Ceremony",
    image: "https://images.unsplash.com/photo-1540959733332-eab4deabeeaf?w=800&h=600&fit=crop",
    highlights: "Senso-ji Temple, Akihabara, Tsukiji Market, Shibuya Crossing, Mount Fuji Day Trip"
  },
  {
    title: "New York City Explorer",
    description: "Discover the Big Apple with our comprehensive NYC tour. From Times Square to Central Park, experience the energy of Manhattan. Visit the Statue of Liberty, explore world-class museums, and enjoy Broadway shows.",
    duration: 6,
    price: 1499.99,
    includeService: "Flights, Hotel, MetroCard, Museum Passes, Broadway Show Ticket",
    image: "https://images.unsplash.com/photo-1496442226666-8d4d0e62e6e9?w=800&h=600&fit=crop",
    highlights: "Times Square, Central Park, Statue of Liberty, Empire State Building, Broadway"
  },
  {
    title: "Bali Paradise",
    description: "Escape to the tropical paradise of Bali. Experience stunning beaches, lush rice terraces, and spiritual temples. Enjoy traditional Balinese massages, explore Ubud's cultural heart, and witness breathtaking sunsets.",
    duration: 10,
    price: 1699.99,
    includeService: "Flights, Resort, Airport Transfers, Spa Treatment, Cultural Tours",
    image: "https://images.unsplash.com/photo-1537953773345-d172ccf13cf1?w=800&h=600&fit=crop",
    highlights: "Ubud Rice Terraces, Tanah Lot Temple, Nusa Penida, Sacred Monkey Forest, Beach Clubs"
  },
  {
    title: "London Royal Tour",
    description: "Experience the grandeur of London with visits to Buckingham Palace, Tower of London, and Westminster Abbey. Enjoy traditional afternoon tea, explore the British Museum, and take a Thames River cruise.",
    duration: 7,
    price: 1399.99,
    includeService: "Flights, Hotel, Oyster Card, Royal Palace Tour, Afternoon Tea",
    image: "https://images.unsplash.com/photo-1513635269975-59663e0ac1ad?w=800&h=600&fit=crop",
    highlights: "Buckingham Palace, Tower of London, Big Ben, British Museum, Thames Cruise"
  },
  {
    title: "Santorini Dream",
    description: "Discover the stunning beauty of Santorini with its iconic white buildings and blue domes. Watch spectacular sunsets from Oia, explore ancient ruins, and enjoy Mediterranean cuisine. Experience the magic of the Greek islands.",
    duration: 8,
    price: 1799.99,
    includeService: "Flights, Boutique Hotel, Airport Transfers, Wine Tasting, Sunset Cruise",
    image: "https://images.unsplash.com/photo-1570077188670-e3a8d69ac5ff?w=800&h=600&fit=crop",
    highlights: "Oia Sunset, Fira Town, Ancient Thera, Red Beach, Wine Villages"
  },
  {
    title: "Dubai Luxury",
    description: "Experience the ultimate luxury in Dubai. Visit the world's tallest building, Burj Khalifa, shop in extravagant malls, and enjoy desert safaris. Experience the perfect blend of modern luxury and traditional Arabian culture.",
    duration: 6,
    price: 1999.99,
    includeService: "Flights, 5-Star Hotel, Desert Safari, Burj Khalifa Access, Shopping Tours",
    image: "https://images.unsplash.com/photo-1512453979798-5ea266f8880c?w=800&h=600&fit=crop",
    highlights: "Burj Khalifa, Palm Jumeirah, Dubai Mall, Desert Safari, Burj Al Arab"
  },
  {
    title: "Machu Picchu Trek",
    description: "Embark on an unforgettable journey to the ancient Incan citadel of Machu Picchu. Trek through the Sacred Valley, explore Cusco's colonial architecture, and experience the rich culture of Peru. A once-in-a-lifetime adventure.",
    duration: 12,
    price: 2499.99,
    includeService: "Flights, Hotels, Trek Guide, Train Tickets, Cultural Tours",
    image: "https://images.unsplash.com/photo-1587595431973-160d0d94add1?w=800&h=600&fit=crop",
    highlights: "Machu Picchu, Sacred Valley, Cusco, Inca Trail, Rainbow Mountain"
  },
  {
    title: "Sydney Coastal",
    description: "Explore the stunning coastal beauty of Sydney. Visit the iconic Sydney Opera House, climb the Harbour Bridge, and relax on beautiful beaches. Experience the laid-back Australian lifestyle and vibrant city culture.",
    duration: 9,
    price: 1899.99,
    includeService: "Flights, Hotel, Opera House Tour, Bridge Climb, Beach Tours",
    image: "https://images.unsplash.com/photo-1506973035872-a4ec16b8e8d9?w=800&h=600&fit=crop",
    highlights: "Sydney Opera House, Harbour Bridge, Bondi Beach, Blue Mountains, Darling Harbour"
  }
];

async function seedPackages() {
  const baseURL = 'http://localhost:9002/api/packages';
  
  console.log('🌱 Starting to seed travel packages...');
  
  for (let i = 0; i < packages.length; i++) {
    const packageData = packages[i];
    
    try {
      console.log(`📦 Creating package ${i + 1}/9: ${packageData.title}`);
      
      const response = await axios.post(baseURL, packageData, {
        headers: {
          'Content-Type': 'application/json'
        }
      });
      
      console.log(`✅ Successfully created: ${packageData.title} (ID: ${response.data.packageId})`);
      
    } catch (error) {
      console.error(`❌ Failed to create ${packageData.title}:`, error.response?.data || error.message);
    }
    
    // Add a small delay between requests
    await new Promise(resolve => setTimeout(resolve, 500));
  }
  
  console.log('🎉 Seeding completed!');
  console.log('📊 Summary:');
  console.log(`   - Total packages: ${packages.length}`);
  console.log('   - Check the console above for individual results');
}

// Run the seeding
seedPackages().catch(console.error); 