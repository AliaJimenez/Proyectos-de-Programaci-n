package com.interfazmaestro;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class AsistenciaApp extends JFrame {
    
    private JTable tablaAsistencia;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> cmbGrado;
    private JComboBox<String> cmbSeccion;
    private JLabel lblFechaActual;
    
    // Colores del sistema
    private static final Color PRIMARY_COLOR = new Color(25, 25, 112);
    private static final Color SUCCESS_COLOR = new Color(60, 179, 113);
    private static final Color LIGHT_BG = new Color(248, 249, 250);
    
    public AsistenciaApp() {
        setTitle("ClassConnect");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 700));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Panel principal con gradiente
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(240, 242, 245);
                Color color2 = new Color(255, 255, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        
        // HEADER
        JPanel headerPanel = crearHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // CONTENIDO
        JPanel contentPanel = crearContenido();
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
        setVisible(true);
    }
    
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(70, 130, 180)),
            new EmptyBorder(25, 40, 25, 40)
        ));
        
        // Título con icono
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titlePanel.setBackground(Color.WHITE);
        
        JLabel lblIcono = new JLabel("📅");
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 35));
        
        JLabel lblTitulo = new JLabel("Registro de Asistencia");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(PRIMARY_COLOR);
        
        titlePanel.add(lblIcono);
        titlePanel.add(lblTitulo);
        
        // Panel de botones derecho
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        botonesPanel.setBackground(Color.WHITE);
        
        // Botón Guardar
        JButton btnGuardar = new JButton("Guardar Asistencia");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setBackground(SUCCESS_COLOR);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setPreferredSize(new Dimension(240, 45));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnGuardar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnGuardar.setBackground(SUCCESS_COLOR.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnGuardar.setBackground(SUCCESS_COLOR);
            }
        });
        
        btnGuardar.addActionListener(e -> guardarAsistencia());
        
        // Botón Volver
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
        
        botonesPanel.add(btnGuardar);
        botonesPanel.add(btnVolver);
        
        header.add(titlePanel, BorderLayout.WEST);
        header.add(botonesPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel crearContenido() {
        JPanel content = new JPanel(new BorderLayout(0, 25));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(30, 40, 40, 40));
        
        // Panel de controles superiores
        JPanel controlesPanel = crearPanelControles();
        content.add(controlesPanel, BorderLayout.NORTH);
        
        // Panel de la tabla
        JPanel tablaPanel = crearTablaAsistencia();
        content.add(tablaPanel, BorderLayout.CENTER);
        
        return content;
    }
    
    private JPanel crearPanelControles() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 0));
        panel.setOpaque(false);
        
        // Fecha actual
        JPanel fechaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        fechaPanel.setOpaque(false);
        
        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblFecha.setForeground(new Color(70, 70, 70));
        
        lblFechaActual = new JLabel(obtenerFechaActual());
        lblFechaActual.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblFechaActual.setForeground(PRIMARY_COLOR);
        lblFechaActual.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true),
            new EmptyBorder(10, 20, 10, 20)
        ));
        lblFechaActual.setBackground(Color.WHITE);
        lblFechaActual.setOpaque(true);
        
        fechaPanel.add(lblFecha);
        fechaPanel.add(lblFechaActual);
        
        // Filtro por grado
        JPanel gradoPanel = new JPanel(new BorderLayout(10, 0));
        gradoPanel.setOpaque(false);
        
        JLabel lblGrado = new JLabel("Grado:");
        lblGrado.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblGrado.setForeground(new Color(70, 70, 70));
        
        String[] grados = {"Seleccionar grado", "1ro Primaria", "2do Primaria", "3ro Primaria", 
                          "4to Primaria", "5to Primaria", "6to Primaria"};
        cmbGrado = new JComboBox<>(grados);
        cmbGrado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbGrado.setPreferredSize(new Dimension(170, 45));
        cmbGrado.setBackground(Color.WHITE);
        cmbGrado.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true));
        cmbGrado.addActionListener(e -> cargarEstudiantes());
        
        gradoPanel.add(lblGrado, BorderLayout.WEST);
        gradoPanel.add(cmbGrado, BorderLayout.CENTER);
        
        // Filtro por sección
        JPanel seccionPanel = new JPanel(new BorderLayout(10, 0));
        seccionPanel.setOpaque(false);
        
        JLabel lblSeccion = new JLabel("Sección:");
        lblSeccion.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSeccion.setForeground(new Color(70, 70, 70));
        
        String[] secciones = {"Todas", "Sección A", "Sección B", "Sección C"};
        cmbSeccion = new JComboBox<>(secciones);
        cmbSeccion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbSeccion.setPreferredSize(new Dimension(140, 45));
        cmbSeccion.setBackground(Color.WHITE);
        cmbSeccion.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true));
        cmbSeccion.addActionListener(e -> cargarEstudiantes());
        
        seccionPanel.add(lblSeccion, BorderLayout.WEST);
        seccionPanel.add(cmbSeccion, BorderLayout.CENTER);
        
        panel.add(fechaPanel);
        panel.add(gradoPanel);
        panel.add(seccionPanel);
        
        return panel;
    }
    
    private JPanel crearTablaAsistencia() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2, true));
        
        // Modelo de tabla
        String[] columnas = {"Matrícula", "Nombre Completo", "Grado", "Sección", "Asistencia", "Observaciones"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5;
            }
        };
        
        // Crear tabla
        tablaAsistencia = new JTable(modeloTabla);
        tablaAsistencia.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaAsistencia.setRowHeight(55);
        tablaAsistencia.setSelectionBackground(new Color(230, 240, 255));
        tablaAsistencia.setSelectionForeground(Color.BLACK);
        tablaAsistencia.setShowVerticalLines(false);
        tablaAsistencia.setIntercellSpacing(new Dimension(0, 1));
        tablaAsistencia.setFillsViewportHeight(true);
        tablaAsistencia.setGridColor(new Color(230, 230, 230));
        
        // *** HEADER VISIBLE - CORRECCIÓN CRÍTICA ***
        JTableHeader header = tablaAsistencia.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 50));
        header.setOpaque(true);
        header.setReorderingAllowed(false);
        
        // Renderer personalizado forzado para el header
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
                        setBackground(LIGHT_BG);
                    }
                }
                
                return c;
            }
        };
        
        tablaAsistencia.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tablaAsistencia.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tablaAsistencia.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tablaAsistencia.getColumnModel().getColumn(4).setCellRenderer(new AsistenciaRenderer());
        tablaAsistencia.getColumnModel().getColumn(4).setCellEditor(new AsistenciaEditor());
        
        // Anchos de columna
        tablaAsistencia.getColumnModel().getColumn(0).setPreferredWidth(110);
        tablaAsistencia.getColumnModel().getColumn(1).setPreferredWidth(280);
        tablaAsistencia.getColumnModel().getColumn(2).setPreferredWidth(130);
        tablaAsistencia.getColumnModel().getColumn(3).setPreferredWidth(100);
        tablaAsistencia.getColumnModel().getColumn(4).setPreferredWidth(250);
        tablaAsistencia.getColumnModel().getColumn(5).setPreferredWidth(280);
        
        JScrollPane scrollPane = new JScrollPane(tablaAsistencia);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private String obtenerFechaActual() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy");
        return sdf.format(new Date());
    }
    
    private void cargarEstudiantes() {
        modeloTabla.setRowCount(0);
        
        if (cmbGrado.getSelectedIndex() == 0) {
            return;
        }
        
        // Datos de ejemplo
        Object[][] estudiantes = {
            {"2024001", "María García López", "3ro Primaria", "A", "", ""},
            {"2024002", "Carlos Rodríguez Pérez", "3ro Primaria", "A", "", ""},
            {"2024003", "Ana Martínez Díaz", "2do Primaria", "B", "", ""},
            {"2024004", "Luis Fernández Santos", "3ro Primaria", "A", "", ""},
            {"2024005", "Sofía Ramírez Cruz", "1ro Primaria", "C", "", ""},
            {"2024006", "Diego Morales Ortiz", "2do Primaria", "A", "", ""},
            {"2024007", "Valentina Torres Ruiz", "3ro Primaria", "B", "", ""},
            {"2024008", "Mateo Sánchez Herrera", "1ro Primaria", "A", "", ""}
        };
        
        for (Object[] estudiante : estudiantes) {
            modeloTabla.addRow(estudiante);
        }
    }
    
    private void guardarAsistencia() {
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "No hay estudiantes para guardar asistencia.\nSelecciona un grado primero.",
                "Sin Datos",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int estudiantesRegistrados = 0;
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String asistencia = modeloTabla.getValueAt(i, 4).toString();
            if (!asistencia.isEmpty()) {
                estudiantesRegistrados++;
            }
        }
        
        JOptionPane.showMessageDialog(this,
            "✅ Asistencia guardada exitosamente:\n\n" +
            "Fecha: " + lblFechaActual.getText() + "\n" +
            "Grado: " + cmbGrado.getSelectedItem() + "\n" +
            "Estudiantes registrados: " + estudiantesRegistrados + "/" + modeloTabla.getRowCount(),
            "Asistencia Guardada",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Renderer para la columna de asistencia
    class AsistenciaRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private ButtonGroup grupoBotones;
        private JRadioButton btnPresente, btnAusente, btnTardanza;
        
        public AsistenciaRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
            setOpaque(true);
            
            grupoBotones = new ButtonGroup();
            
            btnPresente = crearRadioButton("Presente", SUCCESS_COLOR);
            btnAusente = crearRadioButton("Ausente", new Color(220, 53, 69));
            btnTardanza = crearRadioButton("Tardanza", new Color(255, 193, 7));
            
            grupoBotones.add(btnPresente);
            grupoBotones.add(btnAusente);
            grupoBotones.add(btnTardanza);
            
            add(btnPresente);
            add(btnAusente);
            add(btnTardanza);
        }
        
        private JRadioButton crearRadioButton(String texto, Color color) {
            JRadioButton btn = new JRadioButton(texto);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setForeground(color);
            btn.setBackground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return btn;
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            btnPresente.setSelected(false);
            btnAusente.setSelected(false);
            btnTardanza.setSelected(false);
            
            String estado = value != null ? value.toString() : "";
            switch (estado) {
                case "Presente":
                    btnPresente.setSelected(true);
                    break;
                case "Ausente":
                    btnAusente.setSelected(true);
                    break;
                case "Tardanza":
                    btnTardanza.setSelected(true);
                    break;
            }
            
            if (row % 2 == 0) {
                setBackground(Color.WHITE);
            } else {
                setBackground(LIGHT_BG);
            }
            
            return this;
        }
    }
    
    // Editor para la columna de asistencia
    class AsistenciaEditor extends DefaultCellEditor {
        private JPanel panel;
        private ButtonGroup grupoBotones;
        private JRadioButton btnPresente, btnAusente, btnTardanza;
        private String estadoActual;
        
        public AsistenciaEditor() {
            super(new JCheckBox());
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            panel.setBackground(Color.WHITE);
            
            grupoBotones = new ButtonGroup();
            estadoActual = "";
            
            btnPresente = crearRadioButton("Presente", SUCCESS_COLOR);
            btnAusente = crearRadioButton("Ausente", new Color(220, 53, 69));
            btnTardanza = crearRadioButton("Tardanza", new Color(255, 193, 7));
            
            btnPresente.addActionListener(e -> estadoActual = "Presente");
            btnAusente.addActionListener(e -> estadoActual = "Ausente");
            btnTardanza.addActionListener(e -> estadoActual = "Tardanza");
            
            grupoBotones.add(btnPresente);
            grupoBotones.add(btnAusente);
            grupoBotones.add(btnTardanza);
            
            panel.add(btnPresente);
            panel.add(btnAusente);
            panel.add(btnTardanza);
        }
        
        private JRadioButton crearRadioButton(String texto, Color color) {
            JRadioButton btn = new JRadioButton(texto);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setForeground(color);
            btn.setBackground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return btn;
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            estadoActual = value != null ? value.toString() : "";
            
            btnPresente.setSelected("Presente".equals(estadoActual));
            btnAusente.setSelected("Ausente".equals(estadoActual));
            btnTardanza.setSelected("Tardanza".equals(estadoActual));
            
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return estadoActual;
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new AsistenciaApp());
    }
}