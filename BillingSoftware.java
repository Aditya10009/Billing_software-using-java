import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Random;

public class BillingSoftware {

    private JTextArea billTextArea; // To display the final bill
    private HashMap<String, JTextField> itemFields = new HashMap<>();
    private HashMap<String, Integer> itemPrices = new HashMap<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BillingSoftware().createGUI());
    }

    private void createGUI() {
        JFrame frame = new JFrame("Billing Software");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1350, 700);
        frame.getContentPane().setBackground(new Color(91, 44, 111));
        frame.setLayout(null);

        // Title
        JLabel title = new JLabel("Billing System", JLabel.CENTER);
        title.setFont(new Font("Arial Black", Font.BOLD, 20));
        title.setOpaque(true);
        title.setBackground(new Color(165, 105, 189));
        title.setForeground(Color.WHITE);
        title.setBorder(new LineBorder(Color.BLACK, 2));
        title.setBounds(0, 0, 1350, 50);
        frame.add(title);

        // Customer Details Panel
        JPanel detailsPanel = createCustomerDetailsPanel();
        frame.add(detailsPanel);

        // Snacks Panel
        String[] snacksItems = {
            "Nutella Choco Spread", "Noodles (1 Pack)", "Lays (10Rs)",
            "Oreo (20Rs)", "Chocolate Muffin", "Dairy Milk Silk (60Rs)", "Namkeen (15Rs)"
        };
        int[] snacksPrices = {200, 50, 10, 20, 40, 60, 15};
        JPanel snacksPanel = createItemPanel("Snacks", snacksItems, snacksPrices, 10, 180);
        frame.add(snacksPanel);

        // Grocery Panel
        String[] groceryItems = {
            "Aashirvaad Atta (1kg)", "Pasta (1kg)", "Basmati Rice (1kg)",
            "Sunflower Oil (1ltr)", "Refined Sugar (1kg)", "Dal (1kg)"
        };
        int[] groceryPrices = {50, 70, 90, 120, 45, 60};
        JPanel groceryPanel = createItemPanel("Grocery", groceryItems, groceryPrices, 350, 180);
        frame.add(groceryPanel);

        // Bill Area
        billTextArea = new JTextArea();
        billTextArea.setBounds(700, 180, 600, 400);
        billTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        billTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        frame.add(billTextArea);

        // Calculate and Print Button
        JButton calculateButton = new JButton("Calculate & Print Bill");
        calculateButton.setBounds(950, 600, 200, 40);
        calculateButton.setFont(new Font("Arial Black", Font.BOLD, 14));
        calculateButton.setBackground(new Color(165, 105, 189));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateBill();
            }
        });
        frame.add(calculateButton);

        frame.setVisible(true);
    }

    private JPanel createCustomerDetailsPanel() {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                "Customer Details", 0, 0,
                new Font("Arial Black", Font.BOLD, 12), Color.WHITE
        ));
        detailsPanel.setBackground(new Color(165, 105, 189));
        detailsPanel.setBounds(0, 60, 1350, 100);
        detailsPanel.setLayout(null);

        JLabel custNameLabel = new JLabel("Customer Name");
        custNameLabel.setFont(new Font("Arial Black", Font.PLAIN, 14));
        custNameLabel.setForeground(Color.WHITE);
        custNameLabel.setBounds(20, 30, 150, 30);
        detailsPanel.add(custNameLabel);

        JTextField custNameField = new JTextField();
        custNameField.setBounds(170, 30, 200, 30);
        detailsPanel.add(custNameField);

        JLabel contactLabel = new JLabel("Contact No.");
        contactLabel.setFont(new Font("Arial Black", Font.PLAIN, 14));
        contactLabel.setForeground(Color.WHITE);
        contactLabel.setBounds(400, 30, 120, 30);
        detailsPanel.add(contactLabel);

        JTextField contactField = new JTextField();
        contactField.setBounds(530, 30, 200, 30);
        detailsPanel.add(contactField);

        JLabel billNoLabel = new JLabel("Bill No.");
        billNoLabel.setFont(new Font("Arial Black", Font.PLAIN, 14));
        billNoLabel.setForeground(Color.WHITE);
        billNoLabel.setBounds(770, 30, 80, 30);
        detailsPanel.add(billNoLabel);

        JTextField billNoField = new JTextField(generateBillNo());
        billNoField.setEditable(false);
        billNoField.setBounds(860, 30, 200, 30);
        detailsPanel.add(billNoField);

        return detailsPanel;
    }

    private JPanel createItemPanel(String panelName, String[] items, int[] prices, int x, int y) {
        JPanel itemPanel = new JPanel();
        itemPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                panelName, 0, 0,
                new Font("Arial Black", Font.BOLD, 12), new Color(108, 52, 131)
        ));
        itemPanel.setBackground(new Color(229, 180, 243));
        itemPanel.setBounds(x, y, 325, 380);
        itemPanel.setLayout(null);

        for (int i = 0; i < items.length; i++) {
            String itemName = items[i];
            int price = prices[i];
            itemPrices.put(itemName, price);

            JLabel itemLabel = new JLabel(itemName + " (" + price + " Rs)");
            itemLabel.setFont(new Font("Arial Black", Font.PLAIN, 11));
            itemLabel.setForeground(new Color(108, 52, 131));
            itemLabel.setBounds(10, (i * 50) + 20, 200, 30);
            itemPanel.add(itemLabel);

            JTextField itemField = new JTextField("0");
            itemField.setBounds(220, (i * 50) + 20, 80, 30);
            itemFields.put(itemName, itemField);
            itemPanel.add(itemField);
        }

        return itemPanel;
    }

    private void calculateBill() {
        int total = 0;
        StringBuilder bill = new StringBuilder();
        bill.append("********** Final Bill **********\n");

        for (String item : itemFields.keySet()) {
            int quantity = 0;
            try {
                quantity = Integer.parseInt(itemFields.get(item).getText());
            } catch (NumberFormatException e) {
                // If invalid input is provided (non-integer), treat it as 0
                quantity = 0;
            }
            if (quantity > 0) {
                int price = itemPrices.get(item) * quantity;
                total += price;
                bill.append(item).append(" x ").append(quantity).append(" = ").append(price).append(" Rs\n");
            }
        }

        bill.append("\nTotal Amount: ").append(total).append(" Rs");
        billTextArea.setText(bill.toString());
    }

    private String generateBillNo() {
        Random rand = new Random();
        return String.valueOf(rand.nextInt(9000) + 1000);
    }
}
