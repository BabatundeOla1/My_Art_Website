# Docker Troubleshooting Guide for TheezyArt Portfolio

## MongoDB Atlas Connection Issues

### 1. SSL Certificate Problems
The main issue you're facing is SSL handshake failures with MongoDB Atlas. Here are the fixes applied:

**Dockerfile Changes:**
- Added `ca-certificates` package installation
- Added `update-ca-certificates` command
- Added JVM security settings for containerized environment

**Application Properties Changes:**
- Added explicit SSL configuration
- Added connection timeout settings
- Added server selection timeout

### 2. Environment Variables Checklist

Make sure your `.env` file contains all required variables:

```bash
# MongoDB Atlas Configuration
SPRING_DATA_MONGODB_URI=mongodb+srv://username:password@cluster.mongodb.net/database?retryWrites=true&w=majority&ssl=true
SPRING_DATA_MONGODB_DATABASE=your_database_name

# Server Configuration
PORT=8087

# Cloudinary Configuration
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret

# Admin Configuration
ADMIN_EMAIL=your_admin_email@example.com
ADMIN_PASSWORD=your_admin_password

# JWT Configuration (Base64 encoded 256-bit key)
JWT_SECRET=your_base64_encoded_jwt_secret_key_here
```

### 3. MongoDB Atlas Network Access

Ensure your MongoDB Atlas cluster allows connections from:
- `0.0.0.0/0` (for Docker containers)
- Or your specific Docker host IP

### 4. Common Issues and Solutions

**Issue: SSL Handshake Errors**
- Solution: The updated Dockerfile now includes proper SSL certificates

**Issue: Connection Timeouts**
- Solution: Added timeout configurations in application.properties

**Issue: Environment Variables Not Loading**
- Solution: Docker Compose now properly loads `.env` file

### 5. Testing Your Setup

1. Build and run with Docker Compose:
```bash
docker-compose up --build
```

2. Check container logs:
```bash
docker-compose logs theezy-art-app
```

3. Test health check:
```bash
curl http://localhost:8087/api/admin/viewAllArtworks
```

### 6. Additional Debugging

If issues persist, check:
- MongoDB Atlas cluster status
- Network connectivity from container
- Environment variable values
- JWT secret format (must be base64 encoded)
