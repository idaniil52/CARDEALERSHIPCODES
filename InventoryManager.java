import java.io.*;
import java.util.*;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
import java.util.List;
public class InventoryManager implements DealerOperations {
    private final List<Vehicle> vehicles = new ArrayList<>();
    private final List<Vehicle> soldVehicles = new ArrayList<>();
    private final List<Vehicle> removedVehicles = new ArrayList<>();

    private static final String FILE_NAME = System.getProperty("user.dir") + File.separator + "inventory.txt";
    private static final String SOLD_FILE_NAME = System.getProperty("user.dir")
        + File.separator + "sold_vehicles.txt";
    private static final String REMOVED_FILE_NAME = System.getProperty("user.dir")
        + File.separator + "removed_vehicles.txt";

    public InventoryManager() {
        loadFromFile();
        loadSoldFromFile();
        loadRemovedFromFile();
    }

    public void addVehicle(Vehicle v) {
        vehicles.add(v);
    }

    public List<Vehicle> listVehicles() {
        return vehicles;
    }

    public void saveToFile() {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Vehicle v : vehicles) {
                out.write(v.toString());
                out.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile() {
        vehicles.clear();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No inventory file found.");
            return;
        }

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = in.readLine()) != null) {
                Vehicle v = parseLineToVehicle(line);
                if (v != null) vehicles.add(v);
            }
            System.out.println("Vehicles loaded from file: " + vehicles.size());
        } catch (Exception e) {
            System.err.println("Error loading vehicles: " + e.getMessage());
        }
    }

    public void saveSoldToFile() {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(SOLD_FILE_NAME))) {
            for (Vehicle v : soldVehicles) {
                out.write(v.toString());
                out.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveRemovedToFile() {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(REMOVED_FILE_NAME))) {
            for (Vehicle v : removedVehicles) {
                out.write(v.toString());
                out.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSoldFromFile() {
        soldVehicles.clear();
        File file = new File(SOLD_FILE_NAME);
        if (!file.exists()) return;
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = in.readLine()) != null) {
                Vehicle v = parseLineToVehicle(line);
                if (v != null) soldVehicles.add(v);
            }
        } catch (Exception e) {
            System.err.println("Error loading sold vehicles: " + e.getMessage());
        }
    }

    public void loadRemovedFromFile() {
        removedVehicles.clear();
        File file = new File(REMOVED_FILE_NAME);
        if (!file.exists()) return;
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = in.readLine()) != null) {
                Vehicle v = parseLineToVehicle(line);
                if (v != null) removedVehicles.add(v);
            }
        } catch (Exception e) {
            System.err.println("Error loading removed vehicles: " + e.getMessage());
        }
    }

    private Vehicle parseLineToVehicle(String line) {
        String[] parts = line.split(",", 6);
        if (parts.length != 6) return null;
        String id = parts[0], make = parts[1], model = parts[2];
        int year = Integer.parseInt(parts[3]);
        double price = Double.parseDouble(parts[4]);
        String type = parts[5];
        switch (type) {
            case "Sedan":      return new Sedan(id, make, model, year, price);
            case "Coupe":      return new Coupe(id, make, model, year, price);
            case "Motorcycle": return new Motorcycle(id, make, model, year, price);
            case "Truck":      return new Truck(id, make, model, year, price);
            case "SUV":        return new SUV(id, make, model, year, price);
            default:           return null;
        }
    }

    @Override
    public void buyVehicle(Vehicle v) {
        vehicles.remove(v);
        removedVehicles.add(v);
        saveToFile();
        saveRemovedToFile();
    }

    @Override
    public void sellVehicle(Vehicle v) {
        vehicles.remove(v);
        soldVehicles.add(v);
        saveToFile();
        saveSoldToFile();
    }

    @Override
    public void serviceVehicle(Vehicle v) {
        System.out.println("Serviced vehicle: " + v);
    }

    public List<Vehicle> listSoldVehicles() {
        return soldVehicles;
    }

    public List<Vehicle> listRemovedVehicles() {
        return removedVehicles;
    }
}
