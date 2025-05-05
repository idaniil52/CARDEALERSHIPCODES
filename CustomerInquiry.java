import java.io.Serializable;

public class CustomerInquiry implements Serializable {
    private final String name, contact, message;

    public CustomerInquiry(String name, String contact, String message) {
        this.name = name;
        this.contact = contact;
        this.message = message;
    }

    public String getName() { return name; }
    public String getContact() { return contact; }
    public String getMessage() { return message; }

    @Override
    public String toString() {
        return name + "," + contact + "," + message;
    }
}
