package com.alfredhub.ui;

import com.alfredhub.devices.*;
import com.alfredhub.proxy.CameraProxy;
import org.bytedeco.javacv.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CameraPanel extends JPanel {
    private Map<String, CameraDevice> cameras;
    private JPanel camerasGridPanel;
    private Consumer<String> logCallback;
    private int deviceCounter;

    private static class CameraDevice {
        String id;
        String name;
        String brand;
        Camera camera;
        CameraProxy proxy;
        JLabel previewLabel;
        JButton liveBtn;
        JButton thumbnailBtn;
        JLabel statusLabel;
        OpenCVFrameGrabber grabber;
        Timer timer;
        boolean isLiveActive;
        BufferedImage lastFrame;
        JPanel cardPanel;

        CameraDevice(String id, String name, String brand, Camera camera) {
            this.id = id;
            this.name = name;
            this.brand = brand;
            this.camera = camera;
            this.proxy = null;
            this.isLiveActive = false;
        }
    }

    public CameraPanel(Consumer<String> logCallback) {
        this.logCallback = logCallback;
        this.cameras = new HashMap<>();
        this.deviceCounter = 1;

        setLayout(new BorderLayout(20, 20));
        setBackground(ModernTheme.DARK);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initUI();
    }

    private void initUI() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBackground(ModernTheme.SECONDARY);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton addDeviceBtn = new JButton("+ AGREGAR CAMARA");
        addDeviceBtn.setBackground(ModernTheme.GOLD);
        addDeviceBtn.setForeground(ModernTheme.DARK);
        addDeviceBtn.setFocusPainted(false);
        addDeviceBtn.setFont(ModernTheme.NORMAL_FONT);
        addDeviceBtn.addActionListener(e -> showAddCameraDialog());

        JButton refreshBtn = new JButton("REFRESCAR");
        refreshBtn.setBackground(ModernTheme.ACCENT);
        refreshBtn.setForeground(ModernTheme.DARK);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setFont(ModernTheme.NORMAL_FONT);
        refreshBtn.addActionListener(e -> refreshCameras());

        topPanel.add(addDeviceBtn);
        topPanel.add(refreshBtn);
        add(topPanel, BorderLayout.NORTH);

        camerasGridPanel = new JPanel();
        camerasGridPanel.setLayout(new BoxLayout(camerasGridPanel, BoxLayout.Y_AXIS));
        camerasGridPanel.setBackground(ModernTheme.DARK);

        JScrollPane scrollPane = new JScrollPane(camerasGridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void showAddCameraDialog() {
        AddDeviceDialog dialog = new AddDeviceDialog(SwingUtilities.getWindowAncestor(this), (type, data) -> {
            if ("Camara".equals(type)) {
                Object[] arr = (Object[]) data;
                String brand = (String) arr[0];
                String name = (String) arr[1];

                Camera camera;
                if ("Samsung".equals(brand)) {
                    camera = new SamsungCamera(name);
                } else {
                    camera = new XiaomiCamera(name);
                }

                String deviceId = "cam_" + deviceCounter++;
                CameraDevice camDevice = new CameraDevice(deviceId, name, brand, camera);
                cameras.put(deviceId, camDevice);

                logCallback.accept("[CU-01] " + brand + " " + name + " (Camara) agregado");
                refreshCameras();
            }
        });
        dialog.setVisible(true);
    }

    private void refreshCameras() {
        camerasGridPanel.removeAll();

        for (CameraDevice cam : cameras.values()) {
            createCameraCard(cam);
            camerasGridPanel.add(cam.cardPanel);
            camerasGridPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        camerasGridPanel.revalidate();
        camerasGridPanel.repaint();
        logCallback.accept("[CU-03] Panel de cámaras actualizado (" + cameras.size() + " cámaras)");
    }

    private void createCameraCard(CameraDevice cam) {
        // Panel principal de la tarjeta
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(ModernTheme.CARD_BG);
        card.setBorder(BorderFactory.createLineBorder(ModernTheme.ACCENT, 2));
        card.setMaximumSize(new Dimension(600, 280));
        card.setPreferredSize(new Dimension(600, 280));

        // ========== PANEL SUPERIOR ==========
        JPanel topCardPanel = new JPanel(new BorderLayout());
        topCardPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(cam.name + " (" + cam.brand + ")");
        nameLabel.setFont(ModernTheme.HEADER_FONT);
        nameLabel.setForeground(ModernTheme.GOLD);
        topCardPanel.add(nameLabel, BorderLayout.WEST);

        // Estado en la misma línea
        cam.statusLabel = new JLabel("INACTIVO");
        cam.statusLabel.setFont(ModernTheme.SMALL_FONT);
        cam.statusLabel.setForeground(ModernTheme.WARNING);
        topCardPanel.add(cam.statusLabel, BorderLayout.EAST);

        // ========== PANEL CENTRAL ==========
        JPanel centerPanel = new JPanel(new BorderLayout(15, 10));
        centerPanel.setOpaque(false);

        // Vista previa (izquierda)
        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBackground(ModernTheme.DARK);
        previewPanel.setBorder(BorderFactory.createLineBorder(ModernTheme.ACCENT_DARK, 1));
        previewPanel.setPreferredSize(new Dimension(320, 180));

        cam.previewLabel = new JLabel("VISTA PREVIA", JLabel.CENTER);
        cam.previewLabel.setFont(ModernTheme.NORMAL_FONT);
        cam.previewLabel.setForeground(ModernTheme.LIGHT);
        cam.previewLabel.setBackground(ModernTheme.DARK);
        cam.previewLabel.setOpaque(true);
        previewPanel.add(cam.previewLabel, BorderLayout.CENTER);

        // Botones (derecha) - EN UN PANEL CON FLOWLAYOUT VISIBLE
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton initProxyBtn = new JButton("INICIAR PROXY");
        initProxyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        initProxyBtn.setMaximumSize(new Dimension(180, 35));
        initProxyBtn.setBackground(ModernTheme.ACCENT);
        initProxyBtn.setForeground(Color.WHITE);
        initProxyBtn.setFont(ModernTheme.SMALL_FONT);

        cam.liveBtn = new JButton("VER EN TIEMPO REAL");
        cam.liveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        cam.liveBtn.setMaximumSize(new Dimension(180, 35));
        cam.liveBtn.setBackground(ModernTheme.GOLD);
        cam.liveBtn.setForeground(ModernTheme.DARK);
        cam.liveBtn.setFont(ModernTheme.SMALL_FONT);
        cam.liveBtn.setEnabled(false);

        cam.thumbnailBtn = new JButton("ACTUALIZAR MINIATURA");
        cam.thumbnailBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        cam.thumbnailBtn.setMaximumSize(new Dimension(180, 35));
        cam.thumbnailBtn.setBackground(ModernTheme.SUCCESS);
        cam.thumbnailBtn.setForeground(ModernTheme.DARK);
        cam.thumbnailBtn.setFont(ModernTheme.SMALL_FONT);
        cam.thumbnailBtn.setEnabled(false);

        JButton deleteBtn = new JButton("ELIMINAR");
        deleteBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteBtn.setMaximumSize(new Dimension(180, 35));
        deleteBtn.setBackground(ModernTheme.DANGER);
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFont(ModernTheme.SMALL_FONT);

        rightPanel.add(initProxyBtn);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(cam.liveBtn);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(cam.thumbnailBtn);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(deleteBtn);

        centerPanel.add(previewPanel, BorderLayout.CENTER);
        centerPanel.add(rightPanel, BorderLayout.EAST);

        // ========== ACCIONES ==========
        initProxyBtn.addActionListener(e -> {
            cam.proxy = new CameraProxy(cam.brand, cam.name);
            cam.statusLabel.setText("ACTIVO");
            cam.statusLabel.setForeground(ModernTheme.SUCCESS);
            cam.previewLabel.setText("Proxy listo");
            cam.liveBtn.setEnabled(true);
            cam.thumbnailBtn.setEnabled(true);
            logCallback.accept("[PROXY] " + cam.name + " - Proxy inicializado");
        });

        cam.liveBtn.addActionListener(e -> {
            if (cam.isLiveActive) {
                stopLiveCamera(cam);
                cam.liveBtn.setText("VER EN TIEMPO REAL");
                cam.liveBtn.setBackground(ModernTheme.GOLD);
                cam.previewLabel.setText("Proxy listo");
            } else {
                startLiveCamera(cam);
                cam.liveBtn.setText("DETENER CAMARA");
                cam.liveBtn.setBackground(ModernTheme.DANGER);
            }
        });

        cam.thumbnailBtn.addActionListener(e -> {
            if (cam.lastFrame != null) {
                updateThumbnail(cam);
                logCallback.accept("[CU-05] " + cam.name + " - Miniatura actualizada");
                JOptionPane.showMessageDialog(card, "Miniatura guardada", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(card, "Inicia cámara primero", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        deleteBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(card,
                    "¿Eliminar \"" + cam.name + "\"?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                stopLiveCamera(cam);
                cameras.remove(cam.id);
                logCallback.accept("[INFO] Cámara eliminada: " + cam.name);
                refreshCameras();
            }
        });

        card.add(topCardPanel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);

        cam.cardPanel = card;
    }

    private void startLiveCamera(CameraDevice cam) {
        try {
            if (cam.grabber == null) {
                cam.grabber = new OpenCVFrameGrabber(0);
                cam.grabber.start();
                cam.isLiveActive = true;
                cam.statusLabel.setText("VIDEO");
                cam.statusLabel.setForeground(ModernTheme.SUCCESS);
                logCallback.accept("[CAMARA] " + cam.name + " - Cámara iniciada");

                cam.timer = new Timer(33, e -> captureFrame(cam));
                cam.timer.start();
            }
        } catch (Exception e) {
            cam.statusLabel.setText("ERROR");
            cam.statusLabel.setForeground(ModernTheme.DANGER);
            logCallback.accept("[ERROR] " + cam.name + " - " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "No se pudo iniciar la cámara.\nVerifica que esté conectada.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void captureFrame(CameraDevice cam) {
        try {
            org.bytedeco.javacv.Frame frame = cam.grabber.grab();
            if (frame != null) {
                Java2DFrameConverter converter = new Java2DFrameConverter();
                BufferedImage image = converter.convert(frame);
                if (image != null) {
                    cam.lastFrame = image;
                    Image scaledImage = image.getScaledInstance(320, 180, Image.SCALE_FAST);
                    cam.previewLabel.setIcon(new ImageIcon(scaledImage));
                    cam.previewLabel.setText("");
                }
            }
        } catch (Exception e) {
            // Error silencioso
        }
    }

    private void stopLiveCamera(CameraDevice cam) {
        if (cam.timer != null) {
            cam.timer.stop();
            cam.timer = null;
        }
        if (cam.grabber != null) {
            try {
                cam.grabber.stop();
                cam.grabber.release();
                cam.grabber = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cam.isLiveActive = false;
        cam.statusLabel.setText("ACTIVO");
        cam.statusLabel.setForeground(ModernTheme.SUCCESS);
        logCallback.accept("[CAMARA] " + cam.name + " - Cámara detenida");
    }

    private void updateThumbnail(CameraDevice cam) {
        if (cam.lastFrame != null) {
            Image scaledImage = cam.lastFrame.getScaledInstance(320, 180, Image.SCALE_FAST);
            cam.previewLabel.setIcon(new ImageIcon(scaledImage));
            cam.previewLabel.setText("");
            cam.statusLabel.setText("MINI");
            cam.statusLabel.setForeground(ModernTheme.SUCCESS);
        }
    }
}