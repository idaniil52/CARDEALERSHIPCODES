public class Coupe extends Vehicle {
    public Coupe(String id, String make, String model, int year, double price) {
        super(id, make, model, year, price);
    }

    @Override
    public String getType() {
        return "Coupe";
    }
}
