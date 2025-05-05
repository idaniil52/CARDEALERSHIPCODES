public class SUV extends Vehicle {
    public SUV(String id, String make, String model, int year, double price) {
        super(id, make, model, year, price);
    }

    @Override
    public String getType() {
        return "SUV";
    }
}
