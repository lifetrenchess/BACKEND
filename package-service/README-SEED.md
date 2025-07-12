# Travel Packages Seed Script

This script populates the package-service with 9 beautiful travel packages including images and highlights.

## Prerequisites

1. **Package Service Running**: Ensure the package-service is running on port 9002
2. **Node.js**: Make sure Node.js is installed on your system
3. **MySQL Database**: Ensure the `travelbooking_db` database is created and accessible

## Setup Instructions

### 1. Install Dependencies
```bash
npm install
```

### 2. Start the Package Service
Make sure your package-service is running:
```bash
# In the package-service directory
mvn spring-boot:run
```

### 3. Run the Seed Script
```bash
npm run seed
```

Or directly:
```bash
node seed-packages.js
```

## What the Script Does

The script will create 9 travel packages with the following data:

1. **Paris Adventure** - City of Light experience
2. **Tokyo Discovery** - Traditional and modern Japan
3. **New York City Explorer** - The Big Apple
4. **Bali Paradise** - Tropical paradise
5. **London Royal Tour** - British grandeur
6. **Santorini Dream** - Greek island beauty
7. **Dubai Luxury** - Ultimate luxury experience
8. **Machu Picchu Trek** - Ancient Incan adventure
9. **Sydney Coastal** - Australian coastal beauty

Each package includes:
- ✅ Title and description
- ✅ Duration and price
- ✅ Included services
- ✅ **High-quality images** from Unsplash
- ✅ **Highlights** of key attractions

## Expected Output

```
🌱 Starting to seed travel packages...
📦 Creating package 1/9: Paris Adventure
✅ Successfully created: Paris Adventure (ID: 1)
📦 Creating package 2/9: Tokyo Discovery
✅ Successfully created: Tokyo Discovery (ID: 2)
...
🎉 Seeding completed!
📊 Summary:
   - Total packages: 9
   - Check the console above for individual results
```

## Verification

After running the script, you can verify the data by:

1. **Check the API**: `GET http://localhost:9002/api/packages`
2. **Check the Frontend**: Visit your React app to see the packages with images
3. **Check the Database**: Connect to MySQL and query the `travel_package` table

## Troubleshooting

### Common Issues:

1. **Connection Refused**: Make sure package-service is running on port 9002
2. **Database Errors**: Ensure MySQL is running and `travelbooking_db` exists
3. **Duplicate Data**: The script will create new packages each time it runs

### Error Messages:
- `❌ Failed to create [Package Name]`: Check the service logs for details
- `Connection timeout`: Verify the service is running and accessible

## Next Steps

After seeding:
1. Start your frontend application
2. Navigate to the packages section
3. You should see beautiful package cards with images and highlights
4. Test the booking flow with real package data

## API Endpoints

The seeded packages will be available at:
- `GET /api/packages` - List all packages
- `GET /api/packages/{id}` - Get specific package
- `POST /api/packages` - Create new package (used by seed script)

Happy Traveling! ✈️🌍 