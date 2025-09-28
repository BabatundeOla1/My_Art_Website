# Render Deployment Configuration
# This file contains instructions for deploying to Render

## Steps to Deploy on Render:

### 1. Create a New Web Service
- Go to Render Dashboard
- Click "New +" → "Web Service"
- Connect your GitHub repository

### 2. Configure Build Settings
- **Build Command**: `mvn clean package -DskipTests`
- **Dockerfile Path**: `Dockerfile.render`
- **Docker Context**: `.` (root directory)

### 3. Configure Runtime Settings
- **Start Command**: (Leave empty - Dockerfile handles this)
- **Port**: (Leave empty - Render will auto-detect)

### 4. Environment Variables
Make sure these are set in Render's Environment Variables section:
```
SPRING_DATA_MONGODB_URI=mongodb+srv://username:password@cluster.mongodb.net/database?retryWrites=true&w=majority
SPRING_DATA_MONGODB_DATABASE=your_database_name
CLOUDINARY_CLOUD_NAME=your_cloudinary_cloud_name
CLOUDINARY_API_KEY=your_cloudinary_api_key
CLOUDINARY_API_SECRET=your_cloudinary_api_secret
ADMIN_EMAIL=your_admin_email
ADMIN_PASSWORD=your_admin_password
JWT_SECRET=your_jwt_secret_key
PORT=10000
```

### 5. MongoDB Atlas Network Access
- Go to MongoDB Atlas Dashboard
- Navigate to "Network Access"
- Click "Add IP Address"
- Select "Allow access from anywhere" (0.0.0.0/0)
- Or add Render's specific IP ranges if available

### 6. Health Check
- Render will automatically detect the port
- Health check endpoint: `/api/admin/viewAllArtworks`

## Troubleshooting:

### If MongoDB connection still fails:
1. Check MongoDB Atlas network access settings
2. Verify environment variables are correctly set
3. Check MongoDB Atlas cluster status
4. Review Render logs for specific error messages

### If build fails:
1. Ensure Dockerfile.render is in the root directory
2. Check that all dependencies are in pom.xml
3. Verify Maven build works locally

### If SSL errors persist:
1. The Dockerfile.render includes SSL certificate updates
2. MongoDB connection settings are optimized for cloud deployment
3. JVM settings are configured for containerized environment
