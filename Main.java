package sdalabassignment1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

class FeeCalculator {
    private double totalFee = 60000;
    public double getTotalFee() {
        return totalFee;
    }
}

class Payment {
    private String studentId;
    private double amountPaid;
    private double totalFee;
    private String status;
    private String method;

    public Payment(String studentId, double amountPaid, double totalFee, String method) {
        this.studentId = studentId;
        this.amountPaid = amountPaid;
        this.totalFee = totalFee;
        this.method = method;
        this.status = calculateStatus();
    }

    private String calculateStatus() {
        double tolerance = totalFee * 0.01;
        if (amountPaid >= totalFee - tolerance) return "Paid";
        else if (amountPaid > 0) return "Partially Paid";
        else return "Invalid Payment";
    }

    public double getRemainingAmount() {
        return totalFee - amountPaid;
    }

    public String getStatusWithDueDate() {
        if (status.equals("Paid")) {
            return "Paid";
        } else if (status.equals("Partially Paid")) {
            return "Partially Paid (Due Date: " + LocalDate.now().plusDays(7) + ")";
        } else {
            return "Invalid Payment";
        }
    }

    public String getStudentId() { return studentId; }
    public double getAmountPaid() { return amountPaid; }
    public double getTotalFee() { return totalFee; }
    public String getStatus() { return status; }
    public String getMethod() { return method; }
}

class PaymentProcessor {
    private NotificationService notifier = new NotificationService();

    public boolean receivePayment(Payment payment) {
        System.out.println("Payment received from " + payment.getStudentId());
        return true;
    }
}

class NotificationService {}

class Student {
    private String studentId;
    private String name;
    private String email;
    private Payment payment;

    public Student(String studentId, String name, String email, Payment payment) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.payment = payment;
    }

    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Payment getPayment() { return payment; }
}

class VoucherGenerator {
    public String generateVoucher(String studentId) {
        return "VCHR-" + studentId + "-" + System.currentTimeMillis();
    }
}

class StudentFeeController {
    private static Map<String, Student> studentDatabase = new HashMap<>();

    public String processStudentFee(String studentId, String name, String email, double amountPaid, String method) {
        FeeCalculator calculator = new FeeCalculator();
        double totalFee = calculator.getTotalFee();

        Payment payment = new Payment(studentId, amountPaid, totalFee, method);
        PaymentProcessor processor = new PaymentProcessor();

        if (payment.getStatus().equals("Invalid Payment")) {
            return "Payment failed. Please check the amount and try again.";
        }

        processor.receivePayment(payment);
        VoucherGenerator voucherGen = new VoucherGenerator();
        String voucher = voucherGen.generateVoucher(studentId);

        Student student = new Student(studentId, name, email, payment);
        studentDatabase.put(studentId, student);

        return "Student: " + name +
                "\nEmail: " + email +
                "\nTotal Fee: " + totalFee +
                "\nAmount Paid: " + payment.getAmountPaid() +
                "\nPayment Method: " + method +
                "\nRemaining: " + payment.getRemainingAmount() +
                "\nPayment Status: " + payment.getStatusWithDueDate() +
                "\nVoucher: " + voucher;
    }

    public String searchStudentById(String studentId) {
        if (!studentDatabase.containsKey(studentId)) {
            return "Student not found.";
        }
        Student student = studentDatabase.get(studentId);
        Payment payment = student.getPayment();

        return "Student: " + student.getName() +
                "\nEmail: " + student.getEmail() +
                "\nTotal Fee: " + payment.getTotalFee() +
                "\nAmount Paid: " + payment.getAmountPaid() +
                "\nPayment Method: " + payment.getMethod() +
                "\nRemaining: " + payment.getRemainingAmount() +
                "\nPayment Status: " + payment.getStatusWithDueDate();
    }
}

public class Main {
    public static void main(String[] args) {
        showLoginPage();
    }

    public static void showLoginPage() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setSize(300, 200);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        loginFrame.add(panel);
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(100, 20, 160, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 60, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 60, 160, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(100, 100, 80, 25);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());
                if (username.equals("sudais") && password.equals("123")) {
                    loginFrame.dispose();
                    showMainPage();
                } else {
                    JOptionPane.showMessageDialog(panel, "Invalid login");
                }
            }
        });

        loginFrame.setVisible(true);
    }

    public static void showMainPage() {
        JFrame frame = new JFrame("Student Fee Payment");
        frame.setSize(500, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setBounds(10, 20, 100, 25);
        panel.add(studentIdLabel);

        JTextField studentIdText = new JTextField(20);
        studentIdText.setBounds(150, 20, 250, 25);
        panel.add(studentIdText);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(10, 60, 100, 25);
        panel.add(nameLabel);

        JTextField nameText = new JTextField(20);
        nameText.setBounds(150, 60, 250, 25);
        panel.add(nameText);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(10, 100, 100, 25);
        panel.add(emailLabel);

        JTextField emailText = new JTextField(20);
        emailText.setBounds(150, 100, 250, 25);
        panel.add(emailText);

        JLabel paidLabel = new JLabel("Amount Paid:");
        paidLabel.setBounds(10, 140, 100, 25);
        panel.add(paidLabel);

        JTextField paidText = new JTextField(20);
        paidText.setBounds(150, 140, 250, 25);
        panel.add(paidText);

        JLabel methodLabel = new JLabel("Payment Method:");
        methodLabel.setBounds(10, 180, 120, 25);
        panel.add(methodLabel);

        String[] methods = {"Bank", "Cash", "Online"};
        JComboBox<String> methodComboBox = new JComboBox<>(methods);
        methodComboBox.setBounds(150, 180, 250, 25);
        panel.add(methodComboBox);

        JButton payButton = new JButton("Submit Payment");
        payButton.setBounds(150, 220, 150, 30);
        panel.add(payButton);

        JTextArea outputArea = new JTextArea();
        outputArea.setBounds(10, 270, 460, 200);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        panel.add(outputArea);

        JButton printButton = new JButton("Print Receipt");
        printButton.setBounds(150, 480, 150, 30);
        panel.add(printButton);

        JButton downloadButton = new JButton("Download Receipt");
        downloadButton.setBounds(150, 520, 150, 30);
        panel.add(downloadButton);

        JButton searchButton = new JButton("Search by ID");
        searchButton.setBounds(150, 560, 150, 30);
        panel.add(searchButton);

        StudentFeeController controller = new StudentFeeController();

        payButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String studentId = studentIdText.getText();
                String name = nameText.getText();
                String email = emailText.getText();
                String method = methodComboBox.getSelectedItem().toString();
                double amountPaid = 0;

                try {
                    amountPaid = Double.parseDouble(paidText.getText());
                } catch (NumberFormatException ex) {
                    outputArea.setText("Please enter a valid number for amount paid.");
                    return;
                }

                String result = controller.processStudentFee(studentId, name, email, amountPaid, method);
                outputArea.setText(result);
            }
        });

        printButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean done = outputArea.print();
                    if (done) {
                        JOptionPane.showMessageDialog(panel, "Printed successfully!");
                    } else {
                        JOptionPane.showMessageDialog(panel, "Printing canceled.");
                    }
                } catch (PrinterException ex) {
                    JOptionPane.showMessageDialog(panel, "Print error: " + ex.getMessage());
                }
            }
        });

        downloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setSelectedFile(new java.io.File("receipt.txt"));
                int option = fileChooser.showSaveDialog(panel);
                if (option == JFileChooser.APPROVE_OPTION) {
                    try {
                        FileWriter writer = new FileWriter(fileChooser.getSelectedFile());
                        writer.write(outputArea.getText());
                        writer.close();
                        JOptionPane.showMessageDialog(panel, "Receipt downloaded successfully.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel, "Error saving file: " + ex.getMessage());
                    }
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String studentId = studentIdText.getText();
                String result = controller.searchStudentById(studentId);
                outputArea.setText(result);
            }
        });
    }
}
