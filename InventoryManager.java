import java.io.*;
import java.util.*;
public class InventoryManager implements DealerOperations {
    private final List<Vehicle> vehicles = new ArrayList<>();
    private static final String FILE_NAME = System.getProperty("user.dir") + File.separator + "inventory.txt";
    public InventoryManager() {
        loadFromFile();
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
                String[] parts = line.split(",", 6);
                if (parts.length == 6) {
                    String id = parts[0];
                    String make = parts[1];
                    String model = parts[2];
                    int year = Integer.parseInt(parts[3]);
                    double price = Double.parseDouble(parts[4]);
                    String type = parts[5];

                    Vehicle v;
                    switch (type) {
                        case "Sedan":
                            v = new Sedan(id, make, model, year, price); break;
                        case "Coupe":
                            v = new Coupe(id, make, model, year, price); break;
                        case "Motorcycle":
                            v = new Motorcycle(id, make, model, year, price); break;
                        case "Truck":
                            v = new Truck(id, make, model, year, price); break;
                        case "SUV":
                            v = new SUV(id, make, model, year, price); break;
                        default:
                            System.out.println("Skipping unknown type: " + type);
                            continue;
                    }

                    vehicles.add(v);
                }
            }
            System.out.println("Vehicles loaded from file: " + vehicles.size());
        } catch (Exception e) {
            System.err.println("Error loading vehicles: " + e.getMessage());
        }
    }

    @Override
    public void buyVehicle(Vehicle v) {
        vehicles.remove(v);
        saveToFile();
    }

    @Override
    public void sellVehicle(Vehicle v) {
        vehicles.remove(v);
        saveToFile();
    }

    @Override
    public void serviceVehicle(Vehicle v) {
        System.out.println("Serviced vehicle: " + v);
    }
}
