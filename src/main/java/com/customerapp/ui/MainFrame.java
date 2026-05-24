package com.customerapp.ui;

import com.customerapp.api.ApiClient;
import com.google.gson.*;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainFrame() {
        setTitle("Customer Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(buildLoginPanel(), "LOGIN");
        contentPanel.add(buildRegisterPanel(), "REGISTER");
        cardLayout.show(contentPanel, "LOGIN");

        add(contentPanel);
    }

    private JPanel buildLoginPanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(new Color(240, 242, 245));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Welcome Back", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(30, 30, 30));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(title, gbc);

        JLabel subtitle = new JLabel("Sign in to your account", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(new Color(120, 120, 120));
        gbc.gridy = 1;
        card.add(subtitle, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 2;
        card.add(styledLabel("Email"), gbc);
        JTextField emailField = styledTextField();
        gbc.gridx = 1;
        card.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        card.add(styledLabel("Password"), gbc);
        JPasswordField passwordField = new JPasswordField(20);
        styleTextField(passwordField);
        gbc.gridx = 1;
        card.add(passwordField, gbc);

        JLabel errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(new Color(220, 50, 50));
        errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        card.add(errorLabel, gbc);

        JButton loginBtn = new JButton("Sign In");
        loginBtn.setBackground(new Color(99, 102, 241));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginBtn.setPreferredSize(new Dimension(200, 38));
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginBtn.setBorderPainted(false);
        gbc.gridy = 5;
        card.add(loginBtn, gbc);

        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        linkPanel.setBackground(Color.WHITE);
        JLabel noAccount = new JLabel("Don't have an account?");
        noAccount.setFont(new Font("SansSerif", Font.PLAIN, 12));
        noAccount.setForeground(new Color(100, 100, 100));
        JButton registerLink = new JButton("Register");
        registerLink.setBorderPainted(false);
        registerLink.setContentAreaFilled(false);
        registerLink.setFocusPainted(false);
        registerLink.setForeground(new Color(99, 102, 241));
        registerLink.setFont(new Font("SansSerif", Font.BOLD, 12));
        registerLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        linkPanel.add(noAccount);
        linkPanel.add(registerLink);
        gbc.gridy = 6;
        card.add(linkPanel, gbc);

        loginBtn.addActionListener(e -> {
            String email    = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please enter email and password");
                return;
            }

            loginBtn.setEnabled(false);
            loginBtn.setText("Signing in...");
            errorLabel.setText("");

            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                String errorMsg = null;

                @Override
                protected Void doInBackground() {
                    try {
                        ApiClient.login(email, password);
                    } catch (Exception ex) {
                        errorMsg = getRootCause(ex);
                    }
                    return null;
                }

                @Override
                protected void done() {
                    if (errorMsg != null) {
                        errorLabel.setText(errorMsg);
                        loginBtn.setEnabled(true);
                        loginBtn.setText("Sign In");
                    } else {
                        CustomerTablePanel tablePanel = new CustomerTablePanel(MainFrame.this);
                        contentPanel.add(tablePanel, "CUSTOMERS");
                        cardLayout.show(contentPanel, "CUSTOMERS");
                    }
                }
            };
            worker.execute();
        });

        registerLink.addActionListener(e -> cardLayout.show(contentPanel, "REGISTER"));

        wrapper.add(card);
        return wrapper;
    }

    private JPanel buildRegisterPanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(new Color(240, 242, 245));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Create Account", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(30, 30, 30));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(title, gbc);

        JLabel subtitle = new JLabel("Fill in your details to register", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(new Color(120, 120, 120));
        gbc.gridy = 1;
        card.add(subtitle, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 2;
        card.add(styledLabel("Name"), gbc);
        JTextField nameField = styledTextField();
        gbc.gridx = 1; card.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        card.add(styledLabel("Email"), gbc);
        JTextField emailField = styledTextField();
        gbc.gridx = 1; card.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        card.add(styledLabel("Phone"), gbc);
        JTextField phoneField = styledTextField();
        gbc.gridx = 1; card.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        card.add(styledLabel("Password"), gbc);
        JPasswordField passwordField = new JPasswordField(20);
        styleTextField(passwordField);
        gbc.gridx = 1; card.add(passwordField, gbc);

        JLabel errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(new Color(220, 50, 50));
        errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        card.add(errorLabel, gbc);

        JLabel successLabel = new JLabel("", SwingConstants.CENTER);
        successLabel.setForeground(new Color(34, 197, 94));
        successLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        gbc.gridy = 7;
        card.add(successLabel, gbc);

        JButton registerBtn = new JButton("Create Account");
        registerBtn.setBackground(new Color(99, 102, 241));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        registerBtn.setPreferredSize(new Dimension(200, 38));
        registerBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerBtn.setBorderPainted(false);
        gbc.gridy = 8;
        card.add(registerBtn, gbc);

        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        linkPanel.setBackground(Color.WHITE);
        JLabel hasAccount = new JLabel("Already have an account?");
        hasAccount.setFont(new Font("SansSerif", Font.PLAIN, 12));
        hasAccount.setForeground(new Color(100, 100, 100));
        JButton loginLink = new JButton("Sign In");
        loginLink.setBorderPainted(false);
        loginLink.setContentAreaFilled(false);
        loginLink.setFocusPainted(false);
        loginLink.setForeground(new Color(99, 102, 241));
        loginLink.setFont(new Font("SansSerif", Font.BOLD, 12));
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        linkPanel.add(hasAccount);
        linkPanel.add(loginLink);
        gbc.gridy = 9;
        card.add(linkPanel, gbc);

        registerBtn.addActionListener(e -> {
            String name     = nameField.getText().trim();
            String email    = emailField.getText().trim();
            String phone    = phoneField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                errorLabel.setText("All fields are required");
                successLabel.setText("");
                return;
            }

            registerBtn.setEnabled(false);
            registerBtn.setText("Creating...");
            errorLabel.setText("");
            successLabel.setText("");

            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                String errorMsg   = null;
                String successMsg = null;

                @Override
                protected Void doInBackground() {
                    try {
                        ApiClient.register(name, email, phone, password);
                        successMsg = "Registered successfully! You can now sign in.";
                    } catch (Exception ex) {
                        errorMsg = getRootCause(ex);
                    }
                    return null;
                }

                @Override
                protected void done() {
                    registerBtn.setEnabled(true);
                    registerBtn.setText("Create Account");
                    if (errorMsg != null) {
                        errorLabel.setText(errorMsg);
                    } else {
                        successLabel.setText(successMsg);
                        nameField.setText("");
                        emailField.setText("");
                        phoneField.setText("");
                        passwordField.setText("");
                    }
                }
            };
            worker.execute();
        });

        loginLink.addActionListener(e -> cardLayout.show(contentPanel, "LOGIN"));

        wrapper.add(card);
        return wrapper;
    }

    private JLabel styledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(new Color(60, 60, 60));
        return label;
    }

    private JTextField styledTextField() {
        JTextField field = new JTextField(20);
        styleTextField(field);
        return field;
    }

    private void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(200, 32));
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
    }

    private String getRootCause(Exception ex) {
        Throwable cause = ex;
        while (cause.getCause() != null) cause = cause.getCause();
        return cause.getMessage() != null ? cause.getMessage() : "Unknown error";
    }
}