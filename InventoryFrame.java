import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class InventoryFrame extends JFrame {
    private final InventoryManager manager = new InventoryManager();
    private final JTable table;
    private final DefaultTableModel model;
    private JPanel panel1;
    private JTextField idField;
    private JTextField makeField;
    private JTextField modelField;
    private JTextField yearField;
    private JTextField priceField;
    private JComboBox comboBox1;
    private JButton viewSoldButton;
    private JButton viewRemovedButton;
    private JButton viewInventoryButton;

    public InventoryFrame() {
        super("Inventory Management");
        this.setResizable(true);
        this.setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        JButton addButton = new JButton("Add Vehicle");
        JButton searchButton = new JButton("Search by ID");
        JButton buyButton = new JButton("Buy Vehicle");
        JButton removeButton = new JButton("Remove Vehicle");
        JButton backButton = new JButton("Back to Home");
        JButton sellButton = new JButton("Sell Vehicle");

        // Top toolbar with action buttons
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.add(backButton);
        toolbar.addSeparator();
        toolbar.add(addButton);
        toolbar.add(searchButton);
        toolbar.add(buyButton);
        toolbar.add(removeButton);
        toolbar.add(sellButton);
        viewInventoryButton = new JButton("View Inventory");
        toolbar.add(viewInventoryButton);
        toolbar.addSeparator();
        viewSoldButton = new JButton("View Sold Cars");
        viewRemovedButton = new JButton("View Removed Cars");
        toolbar.add(viewSoldButton);
        toolbar.add(viewRemovedButton);
        add(toolbar, BorderLayout.NORTH);

        String[] columns = {"ID", "Make", "Model", "Year", "Price", "Type"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        loadTable();

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

        // Pack form and table into center
        JPanel center = new JPanel(new BorderLayout(5,5));
        center.add(form, BorderLayout.NORTH);
        center.add(new JScrollPane(table), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

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
                model.addRow(new Object[]{
                    v.getId(),
                    v.getMake(),
                    v.getModel(),
                    v.getYear(),
                    String.format("%,.2f", v.getPrice()),
                    v.getType()
                });
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        searchButton.addActionListener(a -> {
            String id = JOptionPane.showInputDialog("Enter Vehicle ID to search:");
            if (id != null) {
                for (Vehicle v : manager.listVehicles()) {
                    if (v.getId().equals(id)) {
                        // JOptionPane.showMessageDialog(this, v.toString());
                        StringBuilder sb = new StringBuilder();
                        sb.append("ID: ").append(v.getId()).append("\n")
                          .append("Make: ").append(v.getMake()).append("\n")
                          .append("Model: ").append(v.getModel()).append("\n")
                          .append("Year: ").append(v.getYear()).append("\n")
                          .append("Price: ").append(String.format("%.2f", v.getPrice())).append("\n")
                          .append("Type: ").append(v.getType());
                        JOptionPane.showMessageDialog(this, sb.toString(), "Vehicle Details", JOptionPane.INFORMATION_MESSAGE);
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
                // use buyVehicle to record removals
                manager.buyVehicle(toRemove);
                JOptionPane.showMessageDialog(this, "Vehicle removed.");
                // refresh table to current inventory
                populateTable(manager.listVehicles());
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

        viewSoldButton.addActionListener(e -> {
            populateTable(manager.listSoldVehicles());
        });

        viewRemovedButton.addActionListener(e -> {
            populateTable(manager.listRemovedVehicles());
        });

        viewInventoryButton.addActionListener(e -> {
            populateTable(manager.listVehicles());
        });
    }

    private void loadTable() {
        for (Vehicle v : manager.listVehicles()) {
            model.addRow(new Object[]{
                v.getId(),
                v.getMake(),
                v.getModel(),
                v.getYear(),
                String.format("%,.2f", v.getPrice()),
                v.getType()
            });
        }
    }

    private void populateTable(List<Vehicle> list) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Vehicle v : list) {
            model.addRow(new Object[]{
                v.getId(), v.getMake(), v.getModel(), v.getYear(), String.format("%,.2f", v.getPrice()), v.getType()
            });
        }
    }
}
