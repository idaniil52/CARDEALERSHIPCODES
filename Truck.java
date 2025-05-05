public class Truck extends Vehicle {
    public Truck(String id, String make, String model, int year, double price) {
        super(id, make, model, year, price);
    }

    @Override
    public String getType() {
        return "Truck";
    }
}
