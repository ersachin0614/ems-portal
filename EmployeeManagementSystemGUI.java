import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class Employee {
    private String name;
    private int id;
    private double baseSalary;
    private int attendedDays;
    private double performanceRating;

    public Employee(String name, int id, double baseSalary) {
        this.name = name;
        this.id = id;
        this.baseSalary = baseSalary;
        this.attendedDays = 0;
        this.performanceRating = 0.0;
    }

    // using get method
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getAttendedDays() {
        return attendedDays;
    }

    public double getPerformanceRating() {
        return performanceRating;
    }

    public void markAttendance() {
        attendedDays++;
    }

    public void setPerformanceRating(double rating) {
        if (rating >= 0 && rating <= 5) {
            this.performanceRating = rating;
        }
    }

    public double calculateSalary() {
        double perDaySalary = baseSalary / 30;
        double totalSalary = perDaySalary * attendedDays;
        double performanceBonus = 0;

        if (performanceRating >= 4.5) {
            performanceBonus = 0.2 * baseSalary;
        } else if (performanceRating >= 3.5) {
            performanceBonus = 0.1 * baseSalary;
        }

        return totalSalary + performanceBonus;
    }

    public String getDetails() {
        return String.format(
                "ID: %d\nName: %s\nBase Salary: $%.2f\nAttended Days: %d\nPerformance Rating: %.2f\nCalculated Salary: $%.2f",
                id, name, baseSalary, attendedDays, performanceRating, calculateSalary());
    }
}

public class EmployeeManagementSystemGUI {
    private JFrame frame;
    private ArrayList<Employee> employees;
    private int nextEmployeeId = 1; // Employee ID generation starts at 1

    public EmployeeManagementSystemGUI() {
        employees = new ArrayList<>();
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("WhimsiCraft Creations - Employee Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        // Company Name
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 39, 46));
        JLabel companyLabel = new JLabel("WhimsiCraft Creations", JLabel.CENTER);
        companyLabel.setFont(new Font("Arial", Font.BOLD, 30));
        companyLabel.setForeground(Color.WHITE);
        JLabel titleLabel = new JLabel("Employee Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        titleLabel.setForeground(Color.LIGHT_GRAY);
        headerPanel.add(companyLabel, BorderLayout.NORTH);
        headerPanel.add(titleLabel, BorderLayout.SOUTH);
        frame.add(headerPanel, BorderLayout.NORTH);

        // Main Button Panel with Attractive Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(7, 1, 10, 10));
        buttonPanel.setBackground(new Color(255, 255, 255));

        JButton addEmployeeButton = createStyledButton("Add New Employee");
        JButton markAttendanceButton = createStyledButton("Mark Attendance");
        JButton setRatingButton = createStyledButton("Set Performance Rating");
        JButton calculateSalaryButton = createStyledButton("Calculate Salary");
        JButton totalEmployeesButton = createStyledButton("Total Employees");
        JButton displayAllEmployeeDetailsButton = createStyledButton("Display All Employee Details");
        JButton exportDataButton = createStyledButton("Export Employee Data");

        addEmployeeButton.addActionListener(e -> addEmployee());
        markAttendanceButton.addActionListener(e -> markAttendance());
        setRatingButton.addActionListener(e -> setPerformanceRating());
        calculateSalaryButton.addActionListener(e -> calculateSalary());
        totalEmployeesButton.addActionListener(e -> showTotalEmployees());
        displayAllEmployeeDetailsButton.addActionListener(e -> displayAllEmployeeDetails());
        exportDataButton.addActionListener(e -> exportEmployeeData());

        buttonPanel.add(addEmployeeButton);
        buttonPanel.add(markAttendanceButton);
        buttonPanel.add(setRatingButton);
        buttonPanel.add(calculateSalaryButton);
        buttonPanel.add(totalEmployeesButton);
        buttonPanel.add(displayAllEmployeeDetailsButton);
        buttonPanel.add(exportDataButton);

        frame.add(buttonPanel, BorderLayout.WEST);
        frame.setVisible(true);
    }

    private Object exportEmployeeData() {
        throw new UnsupportedOperationException("Unimplemented method 'exportEmployeeData'");
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(new Color(34, 49, 63));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(44, 59, 74));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(34, 49, 63));
            }
        });
        return button;
    }

    private void addEmployee() {
        JTextField nameField = new JTextField();
        JTextField salaryField = new JTextField();

        Object[] message = {
                "Name (A-Z, a-z, space only):", nameField,
                "Base Salary (digits only):", salaryField,
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Add New Employee", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String salaryStr = salaryField.getText().trim();

            // (only A-Z, a-z, and spaces allowed)
            if (name.isEmpty() || !name.matches("[a-zA-Z\\s]+")) {
                JOptionPane.showMessageDialog(frame, "Name must only contain letters and spaces!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            double salary = 0.0;
            try {
                salary = Double.parseDouble(salaryStr);
                if (salary <= 0) {
                    JOptionPane.showMessageDialog(frame, "Salary must be a positive number!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Salary must be a valid number!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Automatically generate employee ID
            int employeeId = nextEmployeeId++;

            Employee employee = new Employee(name, employeeId, salary);
            employees.add(employee);
            JOptionPane.showMessageDialog(frame, "Employee added successfully!\nEmployee ID: " + employeeId, "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void markAttendance() {
        String idStr = JOptionPane.showInputDialog(frame, "Enter Employee ID:");
        int id = Integer.parseInt(idStr);

        Employee employee = findEmployeeById(id);
        if (employee != null) {
            employee.markAttendance();
            JOptionPane.showMessageDialog(frame, "Attendance marked for " + employee.getName(), "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Employee not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setPerformanceRating() {
        String idStr = JOptionPane.showInputDialog(frame, "Enter Employee ID:");
        int id = Integer.parseInt(idStr);

        Employee employee = findEmployeeById(id);
        if (employee != null) {
            String ratingStr = JOptionPane.showInputDialog(frame, "Enter Performance Rating (0-5):");
            double rating = Double.parseDouble(ratingStr);

            if (rating >= 0 && rating <= 5) {
                employee.setPerformanceRating(rating);
                JOptionPane.showMessageDialog(frame, "Rating updated for " + employee.getName(), "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid rating!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Employee not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateSalary() {
        String idStr = JOptionPane.showInputDialog(frame, "Enter Employee ID:");
        int id = Integer.parseInt(idStr);

        Employee employee = findEmployeeById(id);
        if (employee != null) {
            double salary = employee.calculateSalary();
            JOptionPane.showMessageDialog(frame, "Calculated Salary for " + employee.getName() + ": $" + salary,
                    "Salary", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Employee not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showTotalEmployees() {
        int totalEmployees = employees.size();
        JOptionPane.showMessageDialog(frame, "Total Employees in the Company: " + totalEmployees, "Total Employees",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void displayAllEmployeeDetails() {
        if (employees.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No employees to display!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Table create
        String[] columnNames = { "Employee ID", "Name", "Attended Days", "Performance Rating", "Salary" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (Employee employee : employees) {
            tableModel.addRow(new Object[] {
                    employee.getId(),
                    employee.getName(),
                    employee.getAttendedDays(),
                    employee.getPerformanceRating(),
                    employee.calculateSalary()
            });
        }

        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setSelectionBackground(new Color(44, 59, 74));
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setGridColor(Color.GRAY);
        table.setShowGrid(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        // add export button
        JButton exportButton = new JButton("Export Data");
        exportButton.setFont(new Font("Arial", Font.PLAIN, 14));
        exportButton.setBackground(new Color(34, 49, 63));
        exportButton.setForeground(Color.WHITE);
        exportButton.setFocusPainted(false);
        exportButton.setPreferredSize(new Dimension(120, 40));
        exportButton.addActionListener(e -> exportTableData(tableModel));

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(exportButton, BorderLayout.SOUTH);

        // Display the employee details in a dialog
        JOptionPane.showMessageDialog(frame, panel, "All Employee Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportTableData(DefaultTableModel tableModel) {
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            data.append(tableModel.getColumnName(i)).append("\t");
        }
        data.append("\n");

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                data.append(tableModel.getValueAt(i, j)).append("\t");
            }
            data.append("\n");
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Employee Data");

        int userSelection = fileChooser.showSaveDialog(frame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile())) {
                writer.write(data.toString());
                JOptionPane.showMessageDialog(frame, "Employee data exported successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error exporting data: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Employee findEmployeeById(int id) {
        for (Employee employee : employees) {
            if (employee.getId() == id) {
                return employee;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeeManagementSystemGUI::new);
    }
}
