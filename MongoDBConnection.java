public class MongoDBConnection {
    public static void main(String[] args) {
        String host = "197.164.84.213"; // Your public IPv4 address
        int port = 27017; // MongoDB is exposed on port 27017
        String databaseName = "school"; // Replace with the actual database name
        String username = ""; // Optional: replace with actual username if authentication is enabled
        String password = ""; // Optional: replace with actual password if authentication is enabled

        // Construct the connection string
        String connectionString = "mongodb://";

        // If authentication is enabled, append username and password
        if (!username.isEmpty() && !password.isEmpty()) {
            connectionString += username + ":" + password + "@";
        }

        // Append host and port
        connectionString += host + ":" + port;

        // Append database name (optional)
        if (!databaseName.isEmpty()) {
            connectionString += "/" + databaseName;
        }

        // Print or use the connection string
        System.out.println("MongoDB connection string: " + connectionString);}
}