import java.io.Serializable;

public abstract class Vehicle implements Serializable {
    private final String id, make, model;
    private final int year;
    private final double price;

    public Vehicle(String id, String make, String model, int year, double price) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
    }

    public String getId() { return id; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getPrice() { return price; }

    public abstract String getType();

    @Override
    public String toString() {
        return id + "," + make + "," + model + "," + year + "," + price + "," + getType();
    }}
