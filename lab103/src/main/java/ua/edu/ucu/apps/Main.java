package ua.edu.ucu.apps;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        Document sd = new SmartDocument("gs://cv-examples/wiki.png");
        System.out.println(sd.parse());
        Document timedDocument = new TimedDocument(sd);
        System.out.println(timedDocument.parse());
        String url = "jdbc:sqlite:your-database-file.db";

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {

            // Your SQL query to insert a record
            // Create the table
            String createTableQuery = "CREATE TABLE IF NOT EXISTS texts (" +
                    "gsc_path TEXT, " +
                    "text TEXT)";

            statement.executeUpdate(createTableQuery);
            System.out.println("Table created successfully!");

            String gcsPath = "gs://cv-examples/wiki.png";
            Document cachedDocument = new CachedDocument(gcsPath, connection);
            String cachedText = cachedDocument.parse();
            if (!cachedText.isEmpty()) {
                System.out.println("Document found in cache:");
                System.out.println(cachedText);
            } else {
                // If not in cache, parse the document and update the cache
                Document smartDocument = new SmartDocument(gcsPath);
                String parsedText = smartDocument.parse();
                System.out.println("Document parsed:");
                System.out.println(parsedText);

                // Add the parsed text to the cache
                Document documentToCache = new CachedDocument(gcsPath, connection);
                documentToCache.parse();
                System.out.println("Added successfully!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
