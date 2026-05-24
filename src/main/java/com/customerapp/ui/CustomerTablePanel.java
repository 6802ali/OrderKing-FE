package com.customerapp.ui;

import com.customerapp.api.ApiClient;
import com.customerapp.model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class CustomerTablePanel extends JPanel {

    private final JFrame parent;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel pageLabel;
    private int currentPage = 0;
    private int totalPages = 1;

    private static final Color PRIMARY    = new Color(99, 102, 241);
    private static final Color SUCCESS    = new Color(34, 197, 94);
    private static final Color WARNING    = new Color(234, 179, 8);
    private static final Color DANGER     = new Color(239, 68, 68);
    private static final Color BG         = new Color(240, 242, 245);
    private static final Color CARD_BG    = Color.WHITE;
    private static final Color TEXT_DARK  = new Color(30, 30, 30);
    private static final Color TEXT_LIGHT = new Color(120, 120, 120);

    public CustomerTablePanel(JFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout(10, 10));
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        buildUI();
        loadCustomers();
    }

    private void buildUI() {

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(BG);
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        JLabel title = new JLabel("Customers");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(TEXT_DARK);
        topBar.add(title, BorderLayout.WEST);

        JButton addBtn = styledButton("+ Add Customer", SUCCESS);
        addBtn.addActionListener(e -> openForm(null));
        topBar.add(addBtn, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Email", "Phone", "Created At"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(36);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setForeground(TEXT_DARK);
        table.setBackground(CARD_BG);
        table.setSelectionBackground(new Color(224, 231, 255));
        table.setSelectionForeground(TEXT_DARK);
        table.setGridColor(new Color(230, 230, 230));
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setMaxWidth(60);

        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column
            ) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column
                );
                label.setBackground(PRIMARY);
                label.setForeground(Color.WHITE);
                label.setFont(new Font("SansSerif", Font.BOLD, 13));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(79, 82, 221)));
                label.setOpaque(true);
                return label;
            }
        });
        header.setPreferredSize(new Dimension(0, 40));
        header.setReorderingAllowed(false);

        // Center align all cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columns.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(CARD_BG);
        add(scrollPane, BorderLayout.CENTER);

        // ── Bottom bar ────────────────────────────────────
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBackground(BG);
        bottomBar.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        // Pagination — left side
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        paginationPanel.setBackground(BG);

        JButton prevBtn = styledButton("← Previous", PRIMARY);
        prevBtn.addActionListener(e -> {
            if (currentPage > 0) { currentPage--; loadCustomers(); }
        });

        pageLabel = new JLabel("Page 1 of 1");
        pageLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        pageLabel.setForeground(TEXT_LIGHT);

        JButton nextBtn = styledButton("Next →", PRIMARY);
        nextBtn.addActionListener(e -> {
            if (currentPage < totalPages - 1) { currentPage++; loadCustomers(); }
        });

        paginationPanel.add(prevBtn);
        paginationPanel.add(pageLabel);
        paginationPanel.add(nextBtn);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionsPanel.setBackground(BG);

        JButton editBtn = styledButton("✎  Edit", WARNING);
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this,
                        "Please select a customer first.", "No Selection",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int id = (int) tableModel.getValueAt(row, 0);
            openFormById(id);
        });

        JButton deleteBtn = styledButton("✕  Delete", DANGER);
        deleteBtn.addActionListener(e -> deleteSelected());

        actionsPanel.add(editBtn);
        actionsPanel.add(deleteBtn);

        bottomBar.add(paginationPanel, BorderLayout.WEST);
        bottomBar.add(actionsPanel, BorderLayout.EAST);
        add(bottomBar, BorderLayout.SOUTH);
    }

    public void loadCustomers() {
        SwingWorker<List<Customer>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Customer> doInBackground() throws Exception {
                totalPages = ApiClient.getTotalPages(currentPage);
                return ApiClient.getAll(currentPage);
            }

            @Override
            protected void done() {
                try {
                    List<Customer> customers = get();
                    tableModel.setRowCount(0);
                    for (Customer c : customers) {
                        tableModel.addRow(new Object[]{
                            c.getId(), c.getName(), c.getEmail(),
                            c.getPhone(), c.getCreatedAt()
                        });
                    }
                    pageLabel.setText("Page " + (currentPage + 1) + " of " + totalPages);
                } catch (Exception ex) {
                    showError(ex);
                }
            }
        };
        worker.execute();
    }

    private void openForm(Customer customer) {
        CustomerFormDialog dialog = new CustomerFormDialog(parent, customer, this);
        dialog.setVisible(true);
    }

    private void openFormById(int id) {
        SwingWorker<Customer, Void> worker = new SwingWorker<>() {
            @Override protected Customer doInBackground() throws Exception {
                return ApiClient.getById(id);
            }
            @Override protected void done() {
                try { openForm(get()); } catch (Exception ex) { showError(ex); }
            }
        };
        worker.execute();
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a customer first.", "No Selection",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int id       = (int)    tableModel.getValueAt(row, 0);
        String name  = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete \"" + name + "\"?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override protected Void doInBackground() throws Exception {
                ApiClient.delete(id); return null;
            }
            @Override protected void done() {
                try { get(); loadCustomers(); }
                catch (Exception ex) { showError(ex); }
            }
        };
        worker.execute();
    }

    private JButton styledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(130, 36));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    private void showError(Exception ex) {
        Throwable cause = ex;
        while (cause.getCause() != null) cause = cause.getCause();
        JOptionPane.showMessageDialog(this,
                cause.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}