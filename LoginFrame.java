import javax.swing.*;
import java.awt.*;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.io.IOException;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        super("Please log in");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        // 1) Photo at top
        JLabel photo = new JLabel(new ImageIcon("logo.jpg"));
        photo.setHorizontalAlignment(SwingConstants.CENTER);
        add(photo, BorderLayout.NORTH);

        // 2) Form in center
        JPanel form = new JPanel(new GridLayout(2,2,5,5));
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        form.add(new JLabel("Username:"));
        form.add(userField);
        form.add(new JLabel("Password:"));
        form.add(passField);
        add(form, BorderLayout.CENTER);

        // 3) Buttons at bottom
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton loginBtn    = new JButton("Login");
        JButton registerBtn = new JButton("Register");
        buttons.add(loginBtn);
        buttons.add(registerBtn);
        add(buttons, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);

        // 4) Wire it up
        UserManager um;
        try {
            um = new UserManager("users.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        loginBtn.addActionListener(e -> {
            try {
                if (um.authenticate(userField.getText(), new String(passField.getPassword()))) {
                    new MainFrame().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        registerBtn.addActionListener(e -> {
            try {
                boolean ok = um.register(userField.getText(), new String(passField.getPassword()));
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Registration successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Username already exists");
                }
            } catch (IllegalArgumentException ex) {
                String reqMsg = "Registration failed. Please ensure:\n"
                    + "- Username is at least 3 characters long.\n"
                    + "- Password contains at least one uppercase letter and one special character (!@#$%^&*).";
                JOptionPane.showMessageDialog(this, reqMsg, "Invalid Input", JOptionPane.WARNING_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "An error occurred saving user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
