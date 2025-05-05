import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class InventoryFrame extends JFrame {
    private final InventoryManager manager = new InventoryManager();
    private final DefaultTableModel model;
    private JPanel panel1;
    private JTextField idField;
    private JTextField makeField;
    private JTextField modelField;
    private JTextField yearField;
    private JTextField priceField;
    private JComboBox comboBox1;

    public InventoryFrame() {
        super("Inventory Management");
        this.setResizable(true);
        this.setBounds(600, 200, 342, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        String[] columns = {"ID", "Make", "Model", "Year", "Price", "Type"};
        model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        loadTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JTextField idF = new JTextField(), makeF = new JTextField(), modelF = new JTextField(),
                yearF = new JTextField(), priceF = new JTextField();
        JComboBox<String> typeCB = new JComboBox<>(new String[]{"Sedan", "Coupe", "Motorcycle", "Truck", "SUV"});

        JPanel form = new JPanel(new GridLayout(6, 2, 5, 5));
        form.add(new JLabel("ID:")); form.add(idF);
        form.add(new JLabel("Make:")); form.add(makeF);
        form.add(new JLabel("Model:")); form.add(modelF);
        form.add(new JLabel("Year:")); form.add(yearF);
        form.add(new JLabel("Price:")); form.add(priceF);
        form.add(new JLabel("Type:")); form.add(typeCB);
        add(form, BorderLayout.NORTH);

        JButton addButton = new JButton("Add Vehicle");
        JButton searchButton = new JButton("Search by ID");
        JButton buyButton = new JButton("Buy Vehicle");
        JButton removeButton = new JButton("Remove Vehicle");
        JButton backButton = new JButton("Back to Home");
        JButton sellButton = new JButton("Sell Vehicle");

        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        buttonPanel.add(addButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(buyButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(backButton);
        buttonPanel.add(sellButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            try {
                Vehicle v;
                String type = (String) typeCB.getSelectedItem();
                if ("Sedan".equals(type)) {
                    v = new Sedan(idF.getText(), makeF.getText(), modelF.getText(), Integer.parseInt(yearF.getText()), Double.parseDouble(priceF.getText()));
                } else if ("Coupe".equals(type)) {
                    v = new Coupe(idF.getText(), makeF.getText(), modelF.getText(), Integer.parseInt(yearF.getText()), Double.parseDouble(priceF.getText()));
                } else if ("Motorcycle".equals(type)) {
                    v = new Motorcycle(idF.getText(), makeF.getText(), modelF.getText(), Integer.parseInt(yearF.getText()), Double.parseDouble(priceF.getText()));
                } else if ("Truck".equals(type)) {
                    v = new Truck(idF.getText(), makeF.getText(), modelF.getText(), Integer.parseInt(yearF.getText()), Double.parseDouble(priceF.getText()));
                } else {
                    v = new SUV(idF.getText(), makeF.getText(), modelF.getText(), Integer.parseInt(yearF.getText()), Double.parseDouble(priceF.getText()));
                }

                manager.addVehicle(v);
                manager.saveToFile();
                model.addRow(new Object[]{v.getId(), v.getMake(), v.getModel(), v.getYear(), v.getPrice(), v.getType()});
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        searchButton.addActionListener(a -> {
            String id = JOptionPane.showInputDialog("Enter Vehicle ID to search:");
            if (id != null) {
                for (Vehicle v : manager.listVehicles()) {
                    if (v.getId().equals(id)) {
                        JOptionPane.showMessageDialog(this, v.toString());
                        return;
                    }
                }
                JOptionPane.showMessageDialog(this, "Vehicle not found.");
            }
        });

        buyButton.addActionListener(a -> {
            String id = JOptionPane.showInputDialog("Enter Vehicle ID to buy:");
            Vehicle toBuy = null;
            for (Vehicle v : manager.listVehicles()) {
                if (v.getId().equals(id)) {
                    toBuy = v;
                    break;
                }
            }
            if (toBuy != null) {
                manager.buyVehicle(toBuy);
                JOptionPane.showMessageDialog(this, "Vehicle bought: " + toBuy.getId());
            } else {
                JOptionPane.showMessageDialog(this, "Vehicle not found.");
            }
        });

        removeButton.addActionListener(actionEvent -> {
            String id = JOptionPane.showInputDialog("Enter Vehicle ID to remove:");
            Vehicle toRemove = null;
            for (Vehicle v : manager.listVehicles()) {
                if (v.getId().equals(id)) {
                    toRemove = v;
                    break;
                }
            }
            if (toRemove != null) {
                ((ArrayList<Vehicle>) manager.listVehicles()).remove(toRemove);
                manager.saveToFile();
                model.setRowCount(0);
                loadTable();
                JOptionPane.showMessageDialog(this, "Vehicle removed.");
            } else {
                JOptionPane.showMessageDialog(this, "Vehicle not found.");
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            new MainFrame().setVisible(true);
        });

        sellButton.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter Vehicle ID to sell:");
            Vehicle toSell = null;
            for (Vehicle v : manager.listVehicles()) {
                if (v.getId().equals(id)) {
                    toSell = v;
                    break;
                }
            }
            if (toSell != null) {
                manager.sellVehicle(toSell);
                JOptionPane.showMessageDialog(this, "Vehicle sold: " + toSell.getId());
                model.setRowCount(0);
                loadTable();
            } else {
                JOptionPane.showMessageDialog(this, "Vehicle not found.");
            }
        });
    }

    private void loadTable() {
        for (Vehicle v : manager.listVehicles()) {
            model.addRow(new Object[]{v.getId(), v.getMake(), v.getModel(), v.getYear(), v.getPrice(), v.getType()});
        }
    }
}
