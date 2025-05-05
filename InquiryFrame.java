import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InquiryFrame extends JFrame {
    private final CustomerManager customerManager = new CustomerManager();
    private final InventoryManager inventoryManager = new InventoryManager();
    private final DefaultTableModel inquiryModel;
    private final DefaultTableModel vehicleModel;
    private JPanel panel1;
    private JTextField nameF;
    private JTextField contactF;
    private JTextArea messageA;
    private JTable inquiryTable;
    private JTable vehicleTable;
    private JButton addButton;
    private JButton serviceButton;
    private JButton backButton;
    private JButton removeInquiryButton;


    public InquiryFrame() {
        super("Customer Inquiries & Vehicles");
        this.setBounds(600, 200, 800, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        String[] inquiryCols = {"Name", "Contact", "Message"};
        inquiryModel = new DefaultTableModel(inquiryCols, 0);
        inquiryTable = new JTable(inquiryModel);
        loadInquiries();

        String[] vehicleCols = {"ID", "Make", "Model", "Year", "Price", "Type"};
        vehicleModel = new DefaultTableModel(vehicleCols, 0);
        vehicleTable = new JTable(vehicleModel);
        loadVehicles();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(inquiryTable), new JScrollPane(vehicleTable));
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);

        JTextField nameF = new JTextField();
        JTextField contactF = new JTextField();
        JTextArea messageA = new JTextArea(3, 20);

        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.add(new JLabel("Name:")); form.add(nameF);
        form.add(new JLabel("Contact:")); form.add(contactF);
        form.add(new JLabel("Message:")); form.add(new JScrollPane(messageA));
        add(form, BorderLayout.NORTH);

        JButton addButton = new JButton("Add Inquiry");
        JButton serviceButton = new JButton("Service Vehicle");
        JButton backButton = new JButton("Back to Home");
        JButton removeInquiryButton = new JButton("Remove Inquiry");

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(serviceButton);
        buttonPanel.add(removeInquiryButton); // add this line
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            if (nameF.getText().isEmpty() || contactF.getText().isEmpty() || messageA.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields required", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            CustomerInquiry inquiry = new CustomerInquiry(nameF.getText(), contactF.getText(), messageA.getText());
            customerManager.addInquiry(inquiry);
            customerManager.saveToFile();
            inquiryModel.addRow(new Object[]{inquiry.getName(), inquiry.getContact(), inquiry.getMessage()});
        });

        serviceButton.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter Vehicle ID to service:");
            if (id != null && !id.trim().isEmpty()) {
                CustomerInquiry serviceInquiry = new CustomerInquiry("Service Request", "N/A", "Service logged for vehicle ID: " + id);
                customerManager.addInquiry(serviceInquiry);
                customerManager.saveToFile();
                inquiryModel.addRow(new Object[]{serviceInquiry.getName(), serviceInquiry.getContact(), serviceInquiry.getMessage()});
                JOptionPane.showMessageDialog(this, "Service request logged.");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid ID entered.");
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            new MainFrame().setVisible(true);
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        serviceButton.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter Vehicle ID to service:");
            if (id != null && !id.trim().isEmpty()) {
                Vehicle toService = null;
                for (Vehicle v : inventoryManager.listVehicles()) {
                    if (v.getId().equals(id)) {
                        toService = v;
                        break;
                    }
                }

                if (toService != null) {
                    inventoryManager.serviceVehicle(toService);
                    CustomerInquiry serviceInquiry = new CustomerInquiry("Service Request", "N/A", "Serviced vehicle ID: " + id);
                    customerManager.addInquiry(serviceInquiry);
                    customerManager.saveToFile();
                    inquiryModel.addRow(new Object[]{serviceInquiry.getName(), serviceInquiry.getContact(), serviceInquiry.getMessage()});
                    JOptionPane.showMessageDialog(this, "Service request logged.");
                } else {
                    JOptionPane.showMessageDialog(this, "Vehicle not found.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid ID entered.");
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        removeInquiryButton.addActionListener(e -> {
            int selectedRow = inquiryTable.getSelectedRow();
            if (selectedRow >= 0) {
                ((DefaultTableModel) inquiryTable.getModel()).removeRow(selectedRow);
                customerManager.removeInquiryAtIndex(selectedRow);
                customerManager.saveToFile();
                JOptionPane.showMessageDialog(this, "Inquiry removed.");
            } else {
                JOptionPane.showMessageDialog(this, "Please select an inquiry to remove.");
            }
        });




    }

    private void loadInquiries() {
        for (CustomerInquiry i : customerManager.listInquiries()) {
            inquiryModel.addRow(new Object[]{i.getName(), i.getContact(), i.getMessage()});
        }
    }

    private void loadVehicles() {
        for (Vehicle v : inventoryManager.listVehicles()) {
            vehicleModel.addRow(new Object[]{v.getId(), v.getMake(), v.getModel(), v.getYear(), v.getPrice(), v.getType()});
        }
    }
}
