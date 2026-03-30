package com.alfredhub.ui;

import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;

public class AddDeviceDialog extends JDialog {
    private JComboBox<String> deviceTypeCombo;
    private JComboBox<String> deviceBrandCombo;
    private JTextField nameField;
    private JPanel dynamicPanel;
    private BiConsumer<String, Object[]> onAdd;

    public AddDeviceDialog(Window owner, BiConsumer<String, Object[]> onAdd) {
        super(owner, "Agregar Dispositivo", ModalityType.APPLICATION_MODAL);
        this.onAdd = onAdd;

        setSize(500, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(ModernTheme.DARK);

        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(ModernTheme.DARK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("REGISTRAR NUEVO DISPOSITIVO");
        title.setFont(ModernTheme.HEADER_FONT);
        title.setForeground(ModernTheme.GOLD);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JLabel typeLabel = new JLabel("Tipo de dispositivo:");
        typeLabel.setForeground(ModernTheme.LIGHT);
        typeLabel.setFont(ModernTheme.NORMAL_FONT);
        mainPanel.add(typeLabel, gbc);

        gbc.gridx = 1;
        deviceTypeCombo = new JComboBox<>(new String[]{"Luz", "Camara"});
        deviceTypeCombo.setFont(ModernTheme.NORMAL_FONT);
        deviceTypeCombo.addActionListener(e -> updateDynamicFields());
        mainPanel.add(deviceTypeCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel brandLabel = new JLabel("Marca:");
        brandLabel.setForeground(ModernTheme.LIGHT);
        brandLabel.setFont(ModernTheme.NORMAL_FONT);
        mainPanel.add(brandLabel, gbc);

        gbc.gridx = 1;
        deviceBrandCombo = new JComboBox<>(new String[]{"Samsung", "Xiaomi"});
        deviceBrandCombo.setFont(ModernTheme.NORMAL_FONT);
        mainPanel.add(deviceBrandCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel nameLabel = new JLabel("Nombre del dispositivo:");
        nameLabel.setForeground(ModernTheme.LIGHT);
        nameLabel.setFont(ModernTheme.NORMAL_FONT);
        mainPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        nameField = new JTextField(15);
        nameField.setFont(ModernTheme.NORMAL_FONT);
        nameField.setBackground(ModernTheme.SECONDARY);
        nameField.setForeground(ModernTheme.WHITE);
        nameField.setCaretColor(ModernTheme.ACCENT);
        nameField.setBorder(BorderFactory.createLineBorder(ModernTheme.ACCENT));
        mainPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        dynamicPanel = new JPanel(new GridBagLayout());
        dynamicPanel.setBackground(ModernTheme.DARK);
        mainPanel.add(dynamicPanel, gbc);

        gbc.gridy = 5;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(ModernTheme.DARK);

        JButton cancelBtn = new JButton("CANCELAR");
        cancelBtn.setBackground(ModernTheme.DANGER);
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorderPainted(false);
        cancelBtn.addActionListener(e -> dispose());

        JButton addBtn = new JButton("AGREGAR");
        addBtn.setBackground(ModernTheme.GOLD);
        addBtn.setForeground(ModernTheme.DARK);
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.addActionListener(e -> addDevice());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(addBtn);
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        updateDynamicFields();
    }

    private void updateDynamicFields() {
        dynamicPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String type = (String) deviceTypeCombo.getSelectedItem();

        if ("Luz".equals(type)) {
            JLabel brightnessLabel = new JLabel("Brillo inicial (0-100):");
            brightnessLabel.setForeground(ModernTheme.LIGHT);
            brightnessLabel.setFont(ModernTheme.NORMAL_FONT);
            gbc.gridx = 0;
            gbc.gridy = 0;
            dynamicPanel.add(brightnessLabel, gbc);

            JSlider brightnessSlider = new JSlider(0, 100, 75);
            brightnessSlider.setMajorTickSpacing(25);
            brightnessSlider.setPaintTicks(true);
            brightnessSlider.setPaintLabels(true);
            brightnessSlider.setBackground(ModernTheme.DARK);
            brightnessSlider.setForeground(ModernTheme.LIGHT);
            gbc.gridx = 1;
            dynamicPanel.add(brightnessSlider, gbc);

            dynamicPanel.putClientProperty("brightnessSlider", brightnessSlider);
        } else if ("Camara".equals(type)) {
            JLabel resolutionLabel = new JLabel("Resolucion:");
            resolutionLabel.setForeground(ModernTheme.LIGHT);
            resolutionLabel.setFont(ModernTheme.NORMAL_FONT);
            gbc.gridx = 0;
            gbc.gridy = 0;
            dynamicPanel.add(resolutionLabel, gbc);

            JComboBox<String> resolutionCombo = new JComboBox<>(new String[]{"720p", "1080p", "4K"});
            resolutionCombo.setFont(ModernTheme.NORMAL_FONT);
            gbc.gridx = 1;
            dynamicPanel.add(resolutionCombo, gbc);

            JLabel fpsLabel = new JLabel("FPS:");
            fpsLabel.setForeground(ModernTheme.LIGHT);
            fpsLabel.setFont(ModernTheme.NORMAL_FONT);
            gbc.gridx = 0;
            gbc.gridy = 1;
            dynamicPanel.add(fpsLabel, gbc);

            JComboBox<String> fpsCombo = new JComboBox<>(new String[]{"15", "30", "60"});
            fpsCombo.setFont(ModernTheme.NORMAL_FONT);
            gbc.gridx = 1;
            dynamicPanel.add(fpsCombo, gbc);

            dynamicPanel.putClientProperty("resolutionCombo", resolutionCombo);
            dynamicPanel.putClientProperty("fpsCombo", fpsCombo);
        }

        dynamicPanel.revalidate();
        dynamicPanel.repaint();
    }

    private void addDevice() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa un nombre para el dispositivo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String type = (String) deviceTypeCombo.getSelectedItem();
        String brand = (String) deviceBrandCombo.getSelectedItem();
        Object[] params = new Object[2];

        if ("Luz".equals(type)) {
            JSlider slider = (JSlider) dynamicPanel.getClientProperty("brightnessSlider");
            params[0] = slider.getValue();
        } else {
            JComboBox<String> resCombo = (JComboBox<String>) dynamicPanel.getClientProperty("resolutionCombo");
            JComboBox<String> fpsCombo = (JComboBox<String>) dynamicPanel.getClientProperty("fpsCombo");
            params[0] = resCombo.getSelectedItem();
            params[1] = fpsCombo.getSelectedItem();
        }

        onAdd.accept(type, new Object[]{brand, name, params});
        dispose();
    }
}