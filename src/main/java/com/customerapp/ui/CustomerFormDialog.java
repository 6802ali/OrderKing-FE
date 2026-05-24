package com.customerapp.ui;

import com.customerapp.api.ApiClient;
import com.customerapp.model.Customer;

import javax.swing.*;
import java.awt.*;

public class CustomerFormDialog extends JDialog {

    private final Customer existing;
    private final CustomerTablePanel tablePanel;

    private JTextField nameField, emailField, phoneField;
    private JPasswordField passwordField;
    private JLabel errorLabel;

    private static final Color PRIMARY   = new Color(99, 102, 241);
    private static final Color TEXT_DARK = new Color(30, 30, 30);
    private static final Color BG        = new Color(240, 242, 245);

    public CustomerFormDialog(JFrame parent, Customer existing, CustomerTablePanel tablePanel) {
        super(parent, existing == null ? "Add Customer" : "Edit Customer", true);
        this.existing   = existing;
        this.tablePanel = tablePanel;
        setSize(420, 420);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(BG);
        buildUI();
    }

    private void buildUI() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel(
                existing == null ? "Add New Customer" : "Edit Customer",
                SwingConstants.CENTER
        );
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(TEXT_DARK);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(title, gbc);

        gbc.gridwidth = 1;

        nameField     = styledTextField();
        emailField    = styledTextField();
        phoneField    = styledTextField();
        passwordField = new JPasswordField(20);
        styleField(passwordField);

        if (existing != null) {
            nameField.setText(existing.getName());
            emailField.setText(existing.getEmail());
            phoneField.setText(existing.getPhone());
        }

        addRow(card, gbc, 1, "Name",     nameField);
        addRow(card, gbc, 2, "Email",    emailField);
        addRow(card, gbc, 3, "Phone",    phoneField);
        addRow(card, gbc, 4, "Password", passwordField);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        JLabel hint = new JLabel("All fields are required", SwingConstants.CENTER);
        hint.setFont(new Font("SansSerif", Font.ITALIC, 11));
        hint.setForeground(new Color(150, 150, 150));
        card.add(hint, gbc);

        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(new Color(220, 50, 50));
        errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        gbc.gridy = 6;
        card.add(errorLabel, gbc);

        JButton saveBtn = new JButton(existing == null ? "Create Customer" : "Update Customer");
        saveBtn.setBackground(PRIMARY);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveBtn.setPreferredSize(new Dimension(200, 38));
        saveBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveBtn.setOpaque(true);
        gbc.gridy = 7;
        card.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> submit(saveBtn));

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG);
        wrapper.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        wrapper.add(card);
        add(wrapper);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridwidth = 1;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lbl.setForeground(new Color(60, 60, 60));
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(lbl, gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private JTextField styledTextField() {
        JTextField field = new JTextField(20);
        styleField(field);
        return field;
    }

    private void styleField(JTextField field) {
        field.setPreferredSize(new Dimension(200, 32));
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
    }

    private void submit(JButton saveBtn) {
        String name     = nameField.getText().trim();
        String email    = emailField.getText().trim();
        String phone    = phoneField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // ── Validation: all fields required for both create and edit ──
        if (name.isEmpty())     { errorLabel.setText("Name is required");     return; }
        if (email.isEmpty())    { errorLabel.setText("Email is required");    return; }
        if (phone.isEmpty())    { errorLabel.setText("Phone is required");    return; }
        if (password.isEmpty()) { errorLabel.setText("Password is required"); return; }

        saveBtn.setEnabled(false);
        saveBtn.setText("Saving...");
        errorLabel.setText("");

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            String errorMsg = null;

            @Override
            protected Void doInBackground() {
                try {
                    if (existing == null) {
                        ApiClient.create(name, email, phone, password);
                    } else {
                        // Always send all four fields including password
                        ApiClient.update(existing.getId(), name, email, phone, password);
                    }
                } catch (Exception ex) {
                    Throwable cause = ex;
                    while (cause.getCause() != null) cause = cause.getCause();
                    errorMsg = cause.getMessage();
                }
                return null;
            }

            @Override
            protected void done() {
                if (errorMsg != null) {
                    errorLabel.setText(errorMsg);
                    saveBtn.setEnabled(true);
                    saveBtn.setText(existing == null ? "Create Customer" : "Update Customer");
                } else {
                    tablePanel.loadCustomers();
                    dispose();
                }
            }
        };
        worker.execute();
    }
}