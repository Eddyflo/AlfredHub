package com.alfredhub.ui;

import javax.swing.*;
import java.awt.*;

public class DeviceCard extends JPanel {
    private String deviceId;
    private String deviceName;
    private String deviceType;
    private String deviceBrand;
    private String status;
    private JLabel statusLabel;
    private JButton actionButton;
    private JButton deleteButton;
    private Runnable onDelete;

    public DeviceCard(String id, String name, String type, String brand, String initialStatus) {
        this.deviceId = id;
        this.deviceName = name;
        this.deviceType = type;
        this.deviceBrand = brand;
        this.status = initialStatus;

        setLayout(new BorderLayout(10, 10));
        setBackground(ModernTheme.CARD_BG);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ModernTheme.ACCENT_DARK, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(deviceName);
        nameLabel.setFont(ModernTheme.HEADER_FONT);
        nameLabel.setForeground(ModernTheme.GOLD);

        JLabel brandLabel = new JLabel(deviceBrand);
        brandLabel.setFont(ModernTheme.SMALL_FONT);
        brandLabel.setForeground(ModernTheme.ACCENT);

        topPanel.add(nameLabel, BorderLayout.WEST);
        topPanel.add(brandLabel, BorderLayout.EAST);

        JPanel midPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        midPanel.setOpaque(false);

        JLabel typeLabel = new JLabel(deviceType);
        typeLabel.setFont(ModernTheme.SMALL_FONT);
        typeLabel.setForeground(ModernTheme.LIGHT);

        statusLabel = new JLabel(status);
        statusLabel.setFont(ModernTheme.NORMAL_FONT);
        statusLabel.setForeground(ModernTheme.LIGHT);

        midPanel.add(typeLabel);
        midPanel.add(statusLabel);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);

        actionButton = new JButton("CONTROLAR");
        actionButton.setBackground(ModernTheme.ACCENT);
        actionButton.setForeground(Color.WHITE);
        actionButton.setFocusPainted(false);
        actionButton.setBorderPainted(false);
        actionButton.setFont(ModernTheme.SMALL_FONT);
        actionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        deleteButton = new JButton("X");
        deleteButton.setBackground(ModernTheme.DANGER);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorderPainted(false);
        deleteButton.setFont(ModernTheme.SMALL_FONT);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        actionPanel.add(deleteButton);
        actionPanel.add(actionButton);

        add(topPanel, BorderLayout.NORTH);
        add(midPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }

    public void setStatus(String newStatus) {
        this.status = newStatus;
        statusLabel.setText(newStatus);
    }

    public JButton getActionButton() {
        return actionButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public void setOnDelete(Runnable onDelete) {
        this.onDelete = onDelete;
        deleteButton.addActionListener(e -> onDelete.run());
    }

    public String getDeviceId() {
        return deviceId;
    }
}