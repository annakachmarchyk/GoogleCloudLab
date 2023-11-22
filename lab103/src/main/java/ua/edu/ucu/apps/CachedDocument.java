package ua.edu.ucu.apps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CachedDocument extends SmartDocument {
    private Connection connection;

    public CachedDocument(String gcsPath, Connection connection) {
        super(gcsPath);
        this.connection = connection;
    }

    @Override
    public String parse() {
        String textFromCache = retrieveFromCache();
        if (textFromCache != null) {
            return textFromCache;
        }

        // If not in cache, parse using the original SmartDocument
        String parsedText = super.parse();

        // Add the parsed text to the cache
        addToCache(parsedText);

        return parsedText;
    }

    private String retrieveFromCache() {
        String selectQuery = "SELECT text FROM texts WHERE gsc_path = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setString(1, gcsPath);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("text");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void addToCache(String parsedText) {
        String insertQuery = "INSERT INTO texts (gsc_path, text) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, gcsPath);
            preparedStatement.setString(2, parsedText);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
