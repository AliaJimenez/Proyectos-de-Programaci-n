package com.interfazmaestro;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class ReportesApp extends JFrame {

    private JTable tablaReportes;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> cmbEstudiante, cmbTipoReporte, cmbPeriodo;
    private JTextArea txtPreview;
    
    private static final Color PRIMARY_COLOR = new Color(25, 25, 112);
    private static final Color ACCENT_COLOR = new Color(42, 82, 152);
    private static final Color BG_COLOR = new Color(240, 242, 245);
    private static final Color STATUS_SUCCESS = new Color(39, 174, 96); 
    private static final Color STATUS_PENDING = new Color(243, 156, 18); 
    private static final Color STATUS_ERROR = new Color(231, 76, 60); 

    public ReportesApp() {
        setTitle("ClassConnect");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1280, 800));
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);

        mainPanel.add(crearHeaderPrincipal(), BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(crearCuerpo());
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        mainPanel.add(scroll, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel crearHeaderPrincipal() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new MatteBorder(0, 0, 4, 0, PRIMARY_COLOR)); 

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(20, 40, 20, 40));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        JLabel lblTitulo = new JLabel("Generación de Reportes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(PRIMARY_COLOR); 

        JLabel lblSub = new JLabel("Sistema de Gestión Académica - Módulo de Reportes");
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSub.setForeground(Color.GRAY);

        titlePanel.add(lblTitulo);
        titlePanel.add(lblSub);

        JButton btnVolver = new JButton("← Volver");
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVolver.setBackground(new Color(150, 150, 150));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setPreferredSize(new Dimension(120, 45));
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnVolver.addActionListener(e -> {
            new DashboardProfesorApp();
            this.dispose();
        });

        content.add(titlePanel, BorderLayout.WEST);
        content.add(btnVolver, BorderLayout.EAST);
        
        header.add(content, BorderLayout.CENTER);
        return header;
    }

    private JPanel crearCuerpo() {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(BG_COLOR);
        body.setBorder(new EmptyBorder(30, 40, 40, 40));

        body.add(crearTarjetaConfiguracion());
        body.add(Box.createRigidArea(new Dimension(0, 25)));

        // TARJETA 2: VISTA PREVIA (Con Header Gris Oscuro)
        body.add(crearTarjetaVistaPrevia());
        body.add(Box.createRigidArea(new Dimension(0, 25)));

        // TARJETA 3: HISTORIAL (Tabla con colores)
        body.add(crearTarjetaHistorial());

        return body;
    }

    // --- SECCIONES CON "SAZÓN" (HEADERS DE COLOR) ---

    private JPanel crearTarjetaConfiguracion() {
        JPanel card = crearPanelBase();
        
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR); // Fondo Azul
        header.setBorder(new EmptyBorder(15, 25, 15, 25));
        
        JLabel lblTitle = new JLabel("Configuración del Documento");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE); // Texto Blanco
        header.add(lblTitle, BorderLayout.WEST);
        
        card.add(header, BorderLayout.NORTH);

        // Contenido del formulario
        JPanel content = new JPanel(new GridLayout(1, 3, 20, 0));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(25, 25, 10, 25));

        cmbEstudiante = crearCombo(new String[]{"-- Seleccionar --", "María García López", "Carlos Rodríguez", "Ana Martínez"});
        content.add(crearInputGroup("Estudiante:", cmbEstudiante));

        cmbTipoReporte = crearCombo(new String[]{"-- Seleccionar --", "Boletín de Notas", "Certificado de Asistencia", "Informe de Conducta"});
        content.add(crearInputGroup("Tipo de Reporte:", cmbTipoReporte));

        cmbPeriodo = crearCombo(new String[]{"Todo el año", "Trimestre 1", "Trimestre 2", "Trimestre 3"});
        content.add(crearInputGroup("Período:", cmbPeriodo));

        card.add(content, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 15));
        footer.setBackground(Color.WHITE);
        JButton btnGenerar = crearBoton("Generar Vista Previa", ACCENT_COLOR);
        btnGenerar.addActionListener(e -> generarVistaPrevia());
        footer.add(btnGenerar);
        
        card.add(footer, BorderLayout.SOUTH);

        return card;
    }

    private JPanel crearTarjetaVistaPrevia() {
        JPanel card = crearPanelBase();

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 73, 94)); // Un gris azulado oscuro
        header.setBorder(new EmptyBorder(12, 25, 12, 25));
        
        JLabel lblTitle = new JLabel("Vista Previa");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);
        
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        actions.add(crearBotonPeque("Imprimir", new Color(255, 255, 255), Color.BLACK));
        
        JButton btnGuardar = crearBotonPeque("Guardar PDF", STATUS_SUCCESS, Color.WHITE);
        btnGuardar.addActionListener(e -> JOptionPane.showMessageDialog(this, "¡Archivo guardado con éxito!"));
        actions.add(btnGuardar);

        header.add(lblTitle, BorderLayout.WEST);
        header.add(actions, BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);

        // Area de Texto
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(0, 0, 0, 0));

        txtPreview = new JTextArea();
        txtPreview.setText("\nEsperando configuración para generar reporte...");
        txtPreview.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtPreview.setForeground(new Color(60, 60, 60));
        txtPreview.setBackground(new Color(250, 250, 250));
        txtPreview.setEditable(false);
        txtPreview.setLineWrap(true);
        txtPreview.setBorder(new EmptyBorder(20, 25, 70, 25));

        card.add(new JScrollPane(txtPreview), BorderLayout.CENTER);

        return card;
    }

    private JPanel crearTarjetaHistorial() {
        JPanel card = crearPanelBase();

        // Header simple pero con línea de color
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 2, 0, new Color(230, 230, 230)),
            new EmptyBorder(20, 25, 20, 25)
        ));
        
        JLabel lblTitle = new JLabel("Historial de Documentos");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(PRIMARY_COLOR);
        header.add(lblTitle, BorderLayout.WEST);
        card.add(header, BorderLayout.NORTH);

        // TABLA ESTILIZADA
        String[] col = {"Fecha", "Documento", "Estudiante", "Estado", "Acciones"};
        modeloTabla = new DefaultTableModel(col, 0);
        modeloTabla.addRow(new Object[]{"28/11/2025", "Boletín Notas", "María García", "Completado", ""});
        modeloTabla.addRow(new Object[]{"27/11/2025", "Cert. Asistencia", "Carlos Rdz", "Pendiente", ""});
        modeloTabla.addRow(new Object[]{"27/11/2025", "Informe Conducta", "Ana Martínez", "Error", ""});

        tablaReportes = new JTable(modeloTabla);
        estilizarTabla(tablaReportes);
        
        // Renderers especiales para darle color a la tabla
        tablaReportes.getColumnModel().getColumn(3).setCellRenderer(new EstadoRenderer());
        tablaReportes.getColumnModel().getColumn(4).setCellRenderer(new BotonTablaRenderer());
        
        JScrollPane scroll = new JScrollPane(tablaReportes);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }


    private void generarVistaPrevia() {
        if (cmbEstudiante.getSelectedIndex() == 0) return;
        txtPreview.setText("");
        txtPreview.append("====================================================\n");
        txtPreview.append("           INSTITUCIÓN EDUCATIVA MODELO\n");
        txtPreview.append("====================================================\n\n");
        txtPreview.append(" DOCUMENTO: " + cmbTipoReporte.getSelectedItem() + "\n");
        txtPreview.append(" ALUMNO:    " + cmbEstudiante.getSelectedItem() + "\n");
        txtPreview.append(" PERIODO:   " + cmbPeriodo.getSelectedItem() + "\n");
        txtPreview.append(" FECHA:     " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + "\n\n");
        txtPreview.append(" DETALLE:\n El alumno ha cumplido satisfactoriamente con los objetivos...");
    }

    private JPanel crearPanelBase() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        // Sombra suave simulada con borde
        p.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(0, 0, 0, 0)
        ));
        return p;
    }

    private JPanel crearInputGroup(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(Color.GRAY);
        p.add(l, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private JComboBox<String> crearCombo(String[] items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setBackground(Color.WHITE);
        c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return c;
    }

    private JButton crearBoton(String texto, Color bg) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bg.darker() : bg);
                g2.fillRoundRect(0,0,getWidth(),getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(200, 40));
        return btn;
    }

    private JButton crearBotonPeque(String texto, Color bg, Color fg) {
        JButton btn = crearBoton(texto, bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setPreferredSize(new Dimension(130, 30));
        return btn;
    }

    // --- ESTILOS DE TABLA "PRO" ---

    private void estilizarTabla(JTable table) {
        table.setRowHeight(45);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));
        
        // Cabecera CON COLOR
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                l.setBackground(PRIMARY_COLOR); // Cabecera Azul
                l.setForeground(Color.WHITE); 
                l.setFont(new Font("Segoe UI", Font.BOLD, 13));
                l.setBorder(new EmptyBorder(0, 10, 0, 0));
                return l;
            }
        });
        
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 252));
                }
                setBorder(new EmptyBorder(0, 10, 0, 0));
                return c;
            }
        });
    }

    class EstadoRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            String estado = (String) value;
            JLabel label = new JLabel(estado, SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 11));
            label.setOpaque(false); // Importante para ver el dibujo de fondo
            
            Color colorFondo;
            if (estado.equals("Completado")) colorFondo = STATUS_SUCCESS;
            else if (estado.equals("Pendiente")) colorFondo = STATUS_PENDING;
            else colorFondo = STATUS_ERROR;

            JPanel p = new JPanel(new GridBagLayout()) {
                @Override protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (!isSelected) setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 252));
                    else setBackground(table.getSelectionBackground());
                    
                    Graphics2D g2 = (Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(colorFondo);
                    // Dibuja rectángulo redondeado
                    g2.fillRoundRect(10, 10, getWidth()-20, getHeight()-20, 15, 15);
                    g2.dispose();
                }
            };
            label.setForeground(Color.WHITE);
            p.add(label);
            return p;
        }
    }
    
    class BotonTablaRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
            p.setOpaque(false);
            
            JLabel icon = new JLabel("🔍");
            icon.setCursor(new Cursor(Cursor.HAND_CURSOR));
            icon.setToolTipText("Ver Detalles");
            p.add(icon);
            return p;
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(ReportesApp::new);
    }
}