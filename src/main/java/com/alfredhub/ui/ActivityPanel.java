package com.alfredhub.ui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ActivityPanel extends JPanel {
    private JTextArea logArea;
    private JButton clearBtn;

    public ActivityPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(ModernTheme.DARK);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initUI();
    }

    private void initUI() {
        // Panel superior con título y botón
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel title = new JLabel("REGISTRO DE ACTIVIDAD");
        title.setFont(ModernTheme.HEADER_FONT);
        title.setForeground(ModernTheme.GOLD);

        clearBtn = new JButton("LIMPIAR REGISTRO");
        clearBtn.setBackground(ModernTheme.DANGER);
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setFocusPainted(false);
        clearBtn.setBorderPainted(false);
        clearBtn.setFont(ModernTheme.NORMAL_FONT);
        clearBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearBtn.addActionListener(e -> confirmClearLog());

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(clearBtn, BorderLayout.EAST);

        // Área de logs
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(ModernTheme.MONOSPACED);
        logArea.setBackground(ModernTheme.SECONDARY);
        logArea.setForeground(ModernTheme.LIGHT);
        logArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ModernTheme.ACCENT_DARK, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(ModernTheme.SECONDARY);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void confirmClearLog() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que deseas eliminar todo el registro de actividad?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            logArea.setText("");
            addLog("[SISTEMA] Registro de actividad limpiado");
        }
    }

    public void addLog(String message) {
        String timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        logArea.append("[" + timestamp + "] " + message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
}