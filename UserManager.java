import java.io.*;
import java.util.*;

public class UserManager {
    private final File userFile;

    public UserManager(String filename) throws IOException {
        userFile = new File(filename);
        if (!userFile.exists()) userFile.createNewFile();
    }

    // Validate username length
    private boolean isValidUsername(String username) {
        return username != null && username.length() >= 3;
    }

    // Validate password: at least one uppercase and one special character !@#$%^&*
    private boolean isValidPassword(String password) {
        if (password == null) return false;
        boolean hasUpper = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if ("!@#$%^&*".indexOf(c) >= 0) hasSpecial = true;
        }
        return hasUpper && hasSpecial;
    }

    public boolean authenticate(String username, String password) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean register(String username, String password) throws IOException {
        // strict username and password rules for low safety
        if (!isValidUsername(username)) {
            throw new IllegalArgumentException("Username must be at least 3 characters long.");
        }
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter and one special character (!@#$%^&*).");
        }
        // no duplicates
        if (authenticate(username, password)) return false;
        try (PrintWriter out = new PrintWriter(new FileWriter(userFile, true))) {
            out.println(username + ":" + password);
        }
        return true;
    }
}
