package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    public static String get(String key) {
        Properties properties = new Properties(); // Use java.util.Properties
        try (InputStream input = PropertiesUtil.class.getClassLoader()
                .getResourceAsStream("secrets.properties")) {
            if (input == null) {
                System.out.println("Unable to find secrets.properties");
                throw new RuntimeException("Unable to find secrets.properties");
            }
            properties.load(input);

            return properties.getProperty(key);

        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error loading properties file", ex);
        }
    }

    public static String getBotToken() {
        return get("bot.token");
    }

    public static String getBotUsername() {
        return get("bot.username");
    }

    public static String getGameUrl() {
        return get("game.url");
    }
}