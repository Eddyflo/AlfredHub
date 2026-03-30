package com.alfredhub.ui;

import com.alfredhub.devices.*;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class LightPanel extends JPanel {
    private Map<String, Light> lights;
    private Map<String, DeviceCard> deviceCards;
    private JPanel devicesGridPanel;
    private Consumer<String> logCallback;
    private int deviceCounter;

    public LightPanel(Consumer<String> logCallback) {
        this.logCallback = logCallback;
        this.lights = new HashMap<>();
        this.deviceCards = new HashMap<>();
        this.deviceCounter = 1;

        setLayout(new BorderLayout(20, 20));
        setBackground(ModernTheme.DARK);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initUI();
    }

    private void initUI() {
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        devicesGridPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        devicesGridPanel.setBackground(ModernTheme.DARK);

        JScrollPane scrollPane = new JScrollPane(devicesGridPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(ModernTheme.DARK);
        scrollPane.setOpaque(false);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBackground(ModernTheme.SECONDARY);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton addDeviceBtn = createStyledButton("+ AGREGAR LUZ", ModernTheme.GOLD);
        addDeviceBtn.addActionListener(e -> showAddLightDialog());

        JButton refreshBtn = createStyledButton("REFRESCAR", ModernTheme.ACCENT);
        refreshBtn.addActionListener(e -> refreshDevices());

        panel.add(addDeviceBtn);
        panel.add(refreshBtn);

        return panel;
    }

    private void showAddLightDialog() {
        AddDeviceDialog dialog = new AddDeviceDialog(SwingUtilities.getWindowAncestor(this), (type, data) -> {
            if ("Luz".equals(type)) {
                Object[] arr = (Object[]) data;
                String brand = (String) arr[0];
                String name = (String) arr[1];
                Object[] params = (Object[]) arr[2];
                int brightness = (int) params[0];

                Light light;
                if ("Samsung".equals(brand)) {
                    light = new SamsungLight(name);
                } else {
                    light = new XiaomiLight(name);
                }
                light.setBrightness(brightness);

                String deviceId = "light_" + deviceCounter++;
                lights.put(deviceId, light);
                logCallback.accept("[CU-01] " + brand + " " + name + " (Luz) agregado - Brillo: " + brightness + "%");
                refreshDevices();
            }
        });
        dialog.setVisible(true);
    }

    private void refreshDevices() {
        devicesGridPanel.removeAll();
        deviceCards.clear();

        for (Map.Entry<String, Light> entry : lights.entrySet()) {
            String id = entry.getKey();
            Light light = entry.getValue();
            DeviceCard card = new DeviceCard(id, light.getName(), "Luz", light.getBrand(), light.getStatus());
            card.getActionButton().setText("CONTROLAR");
            card.getActionButton().addActionListener(e -> showLightControlDialog(id, card));
            card.setOnDelete(() -> confirmDeleteLight(id, light.getName()));
            devicesGridPanel.add(card);
            deviceCards.put(id, card);
        }

        devicesGridPanel.revalidate();
        devicesGridPanel.repaint();
        logCallback.accept("[CU-03] Panel de luces actualizado");
    }

    private void confirmDeleteLight(String id, String name) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la luz \"" + name + "\"?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            lights.remove(id);
            deviceCards.remove(id);
            logCallback.accept("[INFO] Luz eliminada: " + name);
            refreshDevices();
        }
    }

    private void showLightControlDialog(String deviceId, DeviceCard card) {
        Light light = lights.get(deviceId);
        if (light == null) return;

        String[] options = {"ENCENDER", "APAGAR", "AJUSTAR BRILLO", "CANCELAR"};
        int choice = JOptionPane.showOptionDialog(this,
                "Control de " + light.getName() + "\nEstado actual: " + light.getStatus(),
                "Controlar Luz - " + light.getBrand(),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0:
                light.turnOn();
                logCallback.accept("[CU-02] " + light.getName() + " ENCENDIDA");
                break;
            case 1:
                light.turnOff();
                logCallback.accept("[CU-02] " + light.getName() + " APAGADA");
                break;
            case 2:
                String input = JOptionPane.showInputDialog(this, "Brillo (0-100):", "75");
                if (input != null) {
                    try {
                        int brightness = Integer.parseInt(input);
                        light.setBrightness(brightness);
                        logCallback.accept("[CU-02] " + light.getName() + " brillo ajustado a " + brightness + "%");
                    } catch (NumberFormatException ex) {
                        logCallback.accept("[ERROR] Valor inválido para brillo");
                    }
                }
                break;
        }

        card.setStatus(light.getStatus());
        refreshDevices();
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(ModernTheme.DARK);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(ModernTheme.NORMAL_FONT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}