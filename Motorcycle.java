public class Motorcycle extends Vehicle {
    public Motorcycle(String id, String make, String model, int year, double price) {
        super(id, make, model, year, price);
    }

    @Override
    public String getType() {
        return "Motorcycle";
    }
}
