import javax.swing.*;
import java.awt.*;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.FlowLayout;

public class MainFrame extends JFrame {
    private JButton inventoryButton;
    private JButton customerServiceButton;

    public MainFrame() {
        super("Doxey's Car Dealership Employee View");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        this.setBounds(600, 200, 342, 300);
        setLocationRelativeTo(null);
        JLabel promptLabel = new JLabel("Welcome User, Please Select Any Action!", SwingConstants.CENTER);
        add(promptLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        JButton inventoryButton = new JButton("Manage Inventory");
        JButton inquiryButton = new JButton("Customer Service");

        inquiryButton.addActionListener(e -> {
            new InquiryFrame().setVisible(true);
            dispose();
        });
        inventoryButton.addActionListener(e -> {
            new InventoryFrame().setVisible(true);
            dispose();
        });


        panel.add(inventoryButton);
        panel.add(inquiryButton);
        add(panel, BorderLayout.CENTER);

        // Sign Out button at bottom center
        JButton signOutButton = new JButton("Sign Out");
        signOutButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(signOutButton);
        add(bottomPanel, BorderLayout.SOUTH);

    }
}
