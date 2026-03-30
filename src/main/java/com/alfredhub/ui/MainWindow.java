package com.alfredhub.ui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private LightPanel lightPanel;
    private CameraPanel cameraPanel;
    private ActivityPanel activityPanel;

    public MainWindow() {
        setTitle("ALFREDHUB - Asistente Inteligente del Hogar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 850);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ModernTheme.DARK);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        // Panel superior - Título (común para todas las pestañas)
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);

        // Crear paneles
        lightPanel = new LightPanel(this::addLogToActivity);
        cameraPanel = new CameraPanel(this::addLogToActivity);
        activityPanel = new ActivityPanel();

        // Crear pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(ModernTheme.NORMAL_FONT);
        tabbedPane.setBackground(ModernTheme.DARK);
        tabbedPane.setForeground(ModernTheme.LIGHT);

        // Pestaña 1: Cámaras (inicial)
        tabbedPane.addTab("📹 CÁMARAS", cameraPanel);
        // Pestaña 2: Luces
        tabbedPane.addTab("💡 LUCES", lightPanel);
        // Pestaña 3: Actividad
        tabbedPane.addTab("📋 ACTIVIDAD", activityPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Panel inferior - Estado
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ModernTheme.PRIMARY);
        panel.setPreferredSize(new Dimension(1400, 90));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);

        JLabel title = new JLabel("ALFREDHUB", JLabel.CENTER);
        title.setFont(ModernTheme.TITLE_FONT);
        title.setForeground(ModernTheme.GOLD);

        JLabel subtitle = new JLabel("Tu mayordomo digital - Vigilancia y Control Inteligente", JLabel.CENTER);
        subtitle.setFont(ModernTheme.SMALL_FONT);
        subtitle.setForeground(ModernTheme.LIGHT);

        textPanel.add(title);
        textPanel.add(subtitle);

        panel.add(textPanel, BorderLayout.CENTER);

        /*JLabel batLogo = new JLabel("BAT", JLabel.RIGHT);
        batLogo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        batLogo.setForeground(ModernTheme.ACCENT);
        panel.add(batLogo, BorderLayout.EAST);*/

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(ModernTheme.PRIMARY);
        panel.setPreferredSize(new Dimension(1400, 30));

        JLabel status = new JLabel("VERSION DE PRUEBA 1.0 | Patrones: Abstract Factory + Proxy ");
        status.setFont(ModernTheme.SMALL_FONT);
        status.setForeground(ModernTheme.LIGHT);

        panel.add(status);
        return panel;
    }

    private void addLogToActivity(String message) {
        if (activityPanel != null) {
            activityPanel.addLog(message);
        }
    }
}