import java.io.*;
import java.util.*;

public class CustomerManager {
    private final List<CustomerInquiry> inquiries = new ArrayList<>();
    private static final String FILE_NAME = "inquiries.txt";

    public CustomerManager() {
        loadFromFile();
    }
    public void addInquiry(CustomerInquiry inq) {
        inquiries.add(inq);
    }

    public List<CustomerInquiry> listInquiries() {
        return Collections.unmodifiableList(inquiries);
    }
    public void removeInquiryAtIndex(int index) {
        if (index >= 0 && index < inquiries.size()) {
            inquiries.remove(index);
        }
    }

    public void saveToFile() {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (CustomerInquiry i : inquiries) {
                out.write(i.toString());
                out.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length == 3)
                    inquiries.add(new CustomerInquiry(parts[0], parts[1], parts[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
