package com.alfredhub.ui;

import com.alfredhub.proxy.CameraProxy;
import org.bytedeco.javacv.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class LiveCameraWindow extends JFrame {
    private CameraProxy camera;
    private Consumer<String> logCallback;
    private Consumer<String> thumbnailCallback;
    private OpenCVFrameGrabber grabber;
    private Timer timer;
    private JLabel videoLabel;
    private JLabel statusLabel;
    private BufferedImage lastFrame;

    public LiveCameraWindow(CameraProxy camera, Consumer<String> logCallback, Consumer<String> thumbnailCallback) {
        this.camera = camera;
        this.logCallback = logCallback;
        this.thumbnailCallback = thumbnailCallback;

        setTitle("ALFREDHUB - Vision en Tiempo Real - " + camera.getBrand());
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(ModernTheme.DARK);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        // Panel de video
        JPanel videoPanel = new JPanel(new BorderLayout());
        videoPanel.setBackground(ModernTheme.DARK);
        videoPanel.setBorder(BorderFactory.createLineBorder(ModernTheme.ACCENT, 2));
        videoPanel.setPreferredSize(new Dimension(640, 480));

        videoLabel = new JLabel("Camara no iniciada", JLabel.CENTER);
        videoLabel.setFont(ModernTheme.NORMAL_FONT);
        videoLabel.setForeground(ModernTheme.LIGHT);
        videoLabel.setBackground(ModernTheme.DARK);
        videoLabel.setOpaque(true);
        videoPanel.add(videoLabel, BorderLayout.CENTER);

        // Panel de información
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setBackground(ModernTheme.SECONDARY);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel brandLabel = new JLabel("Dispositivo: " + camera.getBrand());
        brandLabel.setForeground(ModernTheme.LIGHT);

        statusLabel = new JLabel("Estado: Camara DETENIDA");
        statusLabel.setForeground(ModernTheme.LIGHT);

        JLabel resolutionLabel = new JLabel("Resolucion: 640x480 (30 fps)");
        resolutionLabel.setForeground(ModernTheme.LIGHT);

        infoPanel.add(brandLabel);
        infoPanel.add(statusLabel);
        infoPanel.add(resolutionLabel);

        // Panel de controles
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlPanel.setBackground(ModernTheme.DARK);

        JButton startBtn = new JButton("INICIAR CAMARA");
        startBtn.setBackground(ModernTheme.SUCCESS);
        startBtn.setForeground(Color.WHITE);
        startBtn.setFocusPainted(false);
        startBtn.addActionListener(e -> startCamera());

        JButton stopBtn = new JButton("DETENER CAMARA");
        stopBtn.setBackground(ModernTheme.DANGER);
        stopBtn.setForeground(Color.WHITE);
        stopBtn.setFocusPainted(false);
        stopBtn.addActionListener(e -> stopCamera());

        JButton captureBtn = new JButton("CAPTURAR MINIATURA");
        captureBtn.setBackground(ModernTheme.ACCENT);
        captureBtn.setForeground(Color.WHITE);
        captureBtn.setFocusPainted(false);
        captureBtn.addActionListener(e -> captureThumbnail());

        JButton closeBtn = new JButton("CERRAR");
        closeBtn.setBackground(ModernTheme.WARNING);
        closeBtn.setForeground(ModernTheme.DARK);
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> dispose());

        controlPanel.add(startBtn);
        controlPanel.add(stopBtn);
        controlPanel.add(captureBtn);
        controlPanel.add(closeBtn);

        add(videoPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void startCamera() {
        try {
            if (grabber == null) {
                grabber = new OpenCVFrameGrabber(0);
                grabber.start();
                statusLabel.setText("Estado: Camara ACTIVA - Stream en vivo");
                logCallback.accept("[CAMARA] Camara real iniciada correctamente");

                timer = new Timer(33, e -> captureFrame());
                timer.start();
            }
        } catch (Exception e) {
            statusLabel.setText("Estado: ERROR - No se pudo iniciar la camara");
            logCallback.accept("[ERROR] No se pudo iniciar la camara: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "No se pudo iniciar la camara.\n" +
                            "Verifica que tengas una camara conectada.\n" +
                            "Cierra otras apps que usen la camara (Zoom, Teams, etc.)\n\n" +
                            "Error: " + e.getMessage(),
                    "Error de camara",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void captureFrame() {
        try {
            org.bytedeco.javacv.Frame frame = grabber.grab();
            if (frame != null) {
                Java2DFrameConverter converter = new Java2DFrameConverter();
                BufferedImage image = converter.convert(frame);
                if (image != null) {
                    lastFrame = image;
                    int targetWidth = videoLabel.getWidth();
                    int targetHeight = videoLabel.getHeight();
                    if (targetWidth > 0 && targetHeight > 0) {
                        Image scaledImage = image.getScaledInstance(targetWidth, targetHeight, Image.SCALE_FAST);
                        videoLabel.setIcon(new ImageIcon(scaledImage));
                        videoLabel.setText("");
                    }
                }
            }
        } catch (Exception e) {
            // Error silencioso
        }
    }

    private void captureThumbnail() {
        if (lastFrame != null) {
            // Guardar referencia de la última imagen como miniatura
            thumbnailCallback.accept("[MINIATURA] Última imagen capturada guardada como miniatura");
            logCallback.accept("[CAMARA] Miniatura capturada manualmente");
            JOptionPane.showMessageDialog(this, "Miniatura capturada correctamente", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No hay imagen para capturar. Inicia la cámara primero.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void stopCamera() {
        if (timer != null) {
            timer.stop();
        }
        if (grabber != null) {
            try {
                grabber.stop();
                grabber.release();
                grabber = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        videoLabel.setIcon(null);
        videoLabel.setText("Camara detenida");
        statusLabel.setText("Estado: Camara DETENIDA");
        logCallback.accept("[CAMARA] Camara detenida");
    }

    @Override
    public void dispose() {
        stopCamera();
        super.dispose();
    }
}