public class Sedan extends Vehicle {
    public Sedan(String id, String make, String model, int year, double price) {
        super(id, make, model, year, price);
    }

    @Override
    public String getType() {
        return "Sedan";
    }
}
