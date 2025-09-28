# Alternative MongoDB Configuration for Render
# Use this if network access fix doesn't work

# Option 1: Try with different SSL settings
spring.data.mongodb.uri=${SPRING_DATA_MONGODB_URI}&ssl=true&sslInvalidHostNameAllowed=false&connectTimeoutMS=60000&socketTimeoutMS=60000&serverSelectionTimeoutMS=60000&retryWrites=true&w=majority

# Option 2: Disable SSL temporarily for testing (NOT recommended for production)
# spring.data.mongodb.option.ssl=false
# spring.data.mongodb.option.ssl-invalid-host-name-allowed=true

# Option 3: Use different connection string format
# mongodb+srv://username:password@cluster.mongodb.net/database?retryWrites=true&w=majority&ssl=true&sslInvalidHostNameAllowed=false&connectTimeoutMS=60000&socketTimeoutMS=60000&serverSelectionTimeoutMS=60000
