package com.interfazmaestro;

import java.awt.*;
import static java.awt.Color.LIGHT_GRAY;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

public class AvisosApp extends JFrame {
    
    private JTable tablaAvisos;
    private DefaultTableModel modeloTabla;
    private JTextArea txtMensaje;
    private JComboBox<String> cmbDestinatario, cmbUrgencia;
    
    // Colores del sistema - CONSTANTES CORREGIDAS
    private static final Color PRIMARY_COLOR = new Color(25, 25, 112);
    private static final Color SECONDARY_COLOR = new Color(70, 130, 180);
    private static final Color SUCCESS_COLOR = new Color(60, 179, 113);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color INFO_COLOR = new Color(147, 112, 219);
    private static final Color LIGHT_BG = new Color(248, 249, 250);
    private static final Color CARD_BG = Color.WHITE; // CONSTANTE AÑADIDA
    private static final Color ACCENT_COLOR = new Color(37, 99, 235); // CONSTANTE AÑADIDA
    
    public AvisosApp() {
        setTitle("ClassConnect");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1400, 800));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_GRAY);
        
        JPanel headerPanel = crearHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JScrollPane scrollContent = new JScrollPane(crearContenido());
        scrollContent.setBorder(null);
        scrollContent.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollContent, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
        setVisible(true);
    }
    
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            new EmptyBorder(20, 40, 20, 40)
        ));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        leftPanel.setOpaque(false);

        JPanel titleGroup = new JPanel();
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));
        titleGroup.setOpaque(false);
        
        JLabel lblTitulo = new JLabel("Sistema de Avisos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(17, 24, 39));
        
        JLabel lblSubtitulo = new JLabel("Comunicación con padres y tutores");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(107, 114, 128));
        
        titleGroup.setBackground(Color.WHITE);
        titleGroup.add(lblTitulo);
        titleGroup.add(Box.createRigidArea(new Dimension(0, 5)));
        titleGroup.add(lblSubtitulo);
        
        leftPanel.add(titleGroup);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        JButton btnVolver = new JButton("← Volver");
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVolver.setBackground(new Color(150, 150, 150));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setPreferredSize(new Dimension(120, 45));
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnVolver.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnVolver.setBackground(new Color(120, 120, 120));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnVolver.setBackground(new Color(150, 150, 150));
            }
        });
        
        btnVolver.addActionListener(e -> {
            new DashboardProfesorApp();
            this.dispose();
        });
        
        leftPanel.add(titleGroup);
        rightPanel.add(btnVolver);

        header.add(leftPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel crearContenido() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(LIGHT_GRAY);
        content.setBorder(new EmptyBorder(25, 40, 40, 40));
        
        JPanel composicionPanel = crearPanelComposicion();
        composicionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 320));
        content.add(composicionPanel);
        
        content.add(Box.createRigidArea(new Dimension(0, 25)));
        
        JPanel historialPanel = crearPanelHistorial();
        historialPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 450));
        content.add(historialPanel);
        
        return content;
    }
    
    private JPanel crearPanelComposicion() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(CARD_BG);
        container.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        JLabel lblTitulo = new JLabel("Redactar Nuevo Aviso");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(17, 24, 39));
        
        container.add(lblTitulo, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        JPanel controlesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        controlesPanel.setBackground(CARD_BG);
        
        JLabel lblPara = new JLabel("Para:");
        lblPara.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        String[] destinatarios = {"Todos los padres", "Padres de 3ro Primaria", 
                                 "Padres de María García", "Padres de Carlos Rodríguez"};
        cmbDestinatario = new JComboBox<>(destinatarios);
        cmbDestinatario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbDestinatario.setPreferredSize(new Dimension(250, 38));
        
        JLabel lblUrgencia = new JLabel("Urgencia:");
        lblUrgencia.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        String[] urgencias = {"Normal", "Importante", "Urgente"};
        cmbUrgencia = new JComboBox<>(urgencias);
        cmbUrgencia.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbUrgencia.setPreferredSize(new Dimension(150, 38));
        
        controlesPanel.add(lblPara);
        controlesPanel.add(cmbDestinatario);
        controlesPanel.add(lblUrgencia);
        controlesPanel.add(cmbUrgencia);
        
        formPanel.add(controlesPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JLabel lblMensaje = new JLabel("Mensaje:");
        lblMensaje.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMensaje.setForeground(new Color(75, 85, 99));
        lblMensaje.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtMensaje = new JTextArea(4, 80);
        txtMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtMensaje.setLineWrap(true);
        txtMensaje.setWrapStyleWord(true);
        txtMensaje.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(12, 15, 12, 15)
        ));
        
        JScrollPane scrollMensaje = new JScrollPane(txtMensaje);
        scrollMensaje.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        scrollMensaje.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        formPanel.add(lblMensaje);
        formPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        formPanel.add(scrollMensaje);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // BOTÓN ENVIAR CORREGIDO
        JButton btnEnviar = new JButton("Enviar Aviso");
        btnEnviar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setBackground(SECONDARY_COLOR);
        btnEnviar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btnEnviar.setFocusPainted(false);
        btnEnviar.setBorderPainted(false);
        btnEnviar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnEnviar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnEnviar.setBackground(SECONDARY_COLOR.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnEnviar.setBackground(SECONDARY_COLOR);
            }
        });
        
        btnEnviar.addActionListener(e -> enviarAviso());
        
        formPanel.add(btnEnviar);
        
        container.add(formPanel, BorderLayout.CENTER);
        
        return container;
    }
    
    private JPanel crearPanelHistorial() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(CARD_BG);
        container.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        JLabel lblTitulo = new JLabel("Historial de Avisos Enviados");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(17, 24, 39));
        
        container.add(lblTitulo, BorderLayout.NORTH);
        
        String[] columnas = {"Fecha", "Destinatario", "Mensaje", "Urgencia", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        cargarAvisosEjemplo();
        
        tablaAvisos = new JTable(modeloTabla);
        tablaAvisos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaAvisos.setRowHeight(55);
        tablaAvisos.setSelectionBackground(new Color(224, 242, 254));
        tablaAvisos.setShowGrid(false);
        tablaAvisos.setIntercellSpacing(new Dimension(10, 1));
        
        JTableHeader header = tablaAvisos.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 50));
        header.setOpaque(true);
        header.setReorderingAllowed(false);
        

        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString());
                label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                label.setForeground(Color.WHITE);
                label.setBackground(PRIMARY_COLOR);
                label.setOpaque(true);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
                return label;
            }
        });

        // Renderers personalizados
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, 
                                                                 isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                
                if (!isSelected) {
                    if (row % 2 == 0) {
                        setBackground(Color.WHITE);
                    } else {
                        setBackground(new Color(250, 250, 250)); // Gris muy claro
                    }
                }
                
                return c;
            }
        };

        for (int i = 1; i < tablaAvisos.getColumnCount(); i++) {
            tablaAvisos.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        tablaAvisos.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tablaAvisos.getColumnModel().getColumn(3).setCellRenderer(new UrgenciaRenderer());
        tablaAvisos.getColumnModel().getColumn(4).setCellRenderer(new EstadoRenderer());
        
        tablaAvisos.getColumnModel().getColumn(0).setPreferredWidth(130);
        tablaAvisos.getColumnModel().getColumn(1).setPreferredWidth(250);
        tablaAvisos.getColumnModel().getColumn(2).setPreferredWidth(450);
        tablaAvisos.getColumnModel().getColumn(3).setPreferredWidth(110);
        tablaAvisos.getColumnModel().getColumn(4).setPreferredWidth(110);
        
        JScrollPane scrollPane = new JScrollPane(tablaAvisos);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(CARD_BG);
        tableWrapper.setBorder(new EmptyBorder(15, 0, 0, 0));
        tableWrapper.add(scrollPane, BorderLayout.CENTER);
        
        container.add(tableWrapper, BorderLayout.CENTER);
        
        return container;
    }
    
    private void cargarAvisosEjemplo() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        modeloTabla.addRow(new Object[]{
            sdf.format(new Date()),
            "Padres de 3ro Primaria",
            "Recordatorio: Reunión de padres este viernes a las 4:00 PM",
            "Normal",
            "Enviado"
        });
        
        modeloTabla.addRow(new Object[]{
            "18/11/2024 10:30",
            "Padres de María García",
            "Felicidades por el excelente rendimiento en Matemáticas",
            "Importante",
            "Leído"
        });
        
        modeloTabla.addRow(new Object[]{
            "15/11/2024 08:15",
            "Todos los padres",
            "Información sobre el paseo escolar del próximo mes",
            "Normal",
            "Enviado"
        });
    }
    
    class UrgenciaRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, 
                                                             isSelected, hasFocus, row, column);
            setHorizontalAlignment(JLabel.CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setOpaque(true);
            
            if (value != null) {
                String urgencia = value.toString();
                if (urgencia.equals("Urgente")) {
                    setForeground(Color.WHITE);
                    setBackground(DANGER_COLOR);
                } else if (urgencia.equals("Importante")) {
                    setForeground(Color.WHITE);
                    setBackground(WARNING_COLOR);
                } else {
                    setForeground(Color.WHITE);
                    setBackground(SUCCESS_COLOR);
                }
            }
            
            return c;
        }
    }
    
    class EstadoRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, 
                                                             isSelected, hasFocus, row, column);
            setHorizontalAlignment(JLabel.CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setOpaque(true);
            
            if (value != null) {
                String estado = value.toString();
                if (estado.equals("Leído")) {
                    setForeground(SUCCESS_COLOR);
                    setBackground(new Color(220, 252, 231));
                } else {
                    setForeground(ACCENT_COLOR);
                    setBackground(new Color(224, 242, 254));
                }
            }
            
            return c;
        }
    }
    
    private void enviarAviso() {
        String destinatario = cmbDestinatario.getSelectedItem().toString();
        String urgencia = cmbUrgencia.getSelectedItem().toString();
        String mensaje = txtMensaje.getText().trim();
        
        if (mensaje.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor escribe un mensaje antes de enviar",
                "Mensaje Vacío",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        modeloTabla.insertRow(0, new Object[]{
            sdf.format(new Date()),
            destinatario,
            mensaje,
            urgencia,
            "Enviado"
        });
        
        txtMensaje.setText("");
        
        JOptionPane.showMessageDialog(this,
            "Aviso enviado exitosamente\n\n" +
            "Destinatario: " + destinatario + "\n" +
            "Urgencia: " + urgencia + "\n\n" +
            "El mensaje ha sido enviado a los padres.",
            "Aviso Enviado",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new AvisosApp());
    }
}