
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JButton inventoryButton;
    private JButton customerServiceButton;

    public MainFrame() {
        super("Welcome To Doxey's Car dealership");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setBounds(600, 200, 342, 300);
        setLocationRelativeTo(null);

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
        add(panel);

    }
}
