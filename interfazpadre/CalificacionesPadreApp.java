package com.interfazpadre;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class CalificacionesPadreApp extends JFrame {
    
    private JTable tablaCalificaciones;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> cmbPeriodo;
    private JComboBox<String> cmbMateria;
    private JLabel lblPromedioGeneral, lblMejorMateria, lblPeorMateria, lblEstadoGeneral;
    
    // Colores del sistema
    private static final Color PRIMARY_COLOR = new Color(25, 25, 112);
    private static final Color SECONDARY_COLOR = new Color(70, 130, 180);
    private static final Color SUCCESS_COLOR = new Color(60, 179, 113);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color WARNING_COLOR = new Color(255, 193, 50);
    private static final Color INFO_COLOR = new Color(147, 112, 219);
    private static final Color LIGHT_BG = new Color(248, 249, 250);
    
    public CalificacionesPadreApp() {
        setTitle("ClassConnect");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 700));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Panel principal
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
            BorderFactory.createMatteBorder(0, 0, 3, 0, SECONDARY_COLOR),
            new EmptyBorder(25, 40, 25, 40)
        ));
        
        // Título
        JLabel lblTitulo = new JLabel("Calificaciones de María García López");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(PRIMARY_COLOR);
        
        // Panel de botones derecho
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        botonesPanel.setBackground(Color.WHITE);
        
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
            new DashboardPadreApp();
            this.dispose();
        });
        
        botonesPanel.add(btnVolver);
        
        header.add(lblTitulo, BorderLayout.WEST);
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
        
        // Panel dividido (estadísticas y tabla)
        JSplitPane splitPane = crearSplitPane();
        content.add(splitPane, BorderLayout.CENTER);
        
        return content;
    }
    
    private JPanel crearPanelControles() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 0));
        panel.setOpaque(false);
        
        // Selector de Período
        JPanel periodoPanel = new JPanel(new BorderLayout(10, 0));
        periodoPanel.setOpaque(false);
        
        JLabel lblPeriodo = new JLabel("Período Académico:");
        lblPeriodo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblPeriodo.setForeground(new Color(70, 70, 70));
        
        String[] periodos = {
            "Todo el año escolar",
            "Primer Trimestre",
            "Segundo Trimestre", 
            "Tercer Trimestre",
            "Último mes"
        };
        cmbPeriodo = new JComboBox<>(periodos);
        cmbPeriodo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbPeriodo.setPreferredSize(new Dimension(200, 45));
        cmbPeriodo.setBackground(Color.WHITE);
        cmbPeriodo.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true));
        cmbPeriodo.addActionListener(e -> actualizarCalificaciones());
        
        periodoPanel.add(lblPeriodo, BorderLayout.WEST);
        periodoPanel.add(cmbPeriodo, BorderLayout.CENTER);
        
        // Selector de Materia
        JPanel materiaPanel = new JPanel(new BorderLayout(10, 0));
        materiaPanel.setOpaque(false);
        
        JLabel lblMateria = new JLabel("Filtrar por materia:");
        lblMateria.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblMateria.setForeground(new Color(70, 70, 70));
        
        String[] materias = {
            "Todas las materias",
            "Matemáticas",
            "Lengua Española", 
            "Ciencias Naturales",
            "Ciencias Sociales",
            "Inglés",
            "Educación Física",
            "Arte y Cultura"
        };
        cmbMateria = new JComboBox<>(materias);
        cmbMateria.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbMateria.setPreferredSize(new Dimension(180, 45));
        cmbMateria.setBackground(Color.WHITE);
        cmbMateria.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true));
        cmbMateria.addActionListener(e -> actualizarCalificaciones());
        
        materiaPanel.add(lblMateria, BorderLayout.WEST);
        materiaPanel.add(cmbMateria, BorderLayout.CENTER);
        
        panel.add(periodoPanel);
        panel.add(materiaPanel);
        
        return panel;
    }
    
    private JSplitPane crearSplitPane() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(200);
        splitPane.setDividerSize(5);
        splitPane.setBorder(null);
        
        // Panel superior: Estadísticas
        JPanel panelStats = crearPanelEstadisticas();
        splitPane.setTopComponent(panelStats);
        
        // Panel inferior: Tabla de calificaciones
        JPanel panelTabla = crearPanelTabla();
        splitPane.setBottomComponent(panelTabla);
        
        return splitPane;
    }
    
    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 20, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 25, 0));
        
        Object[] promedioData = crearStatCard("Promedio General", "88.5/100", SECONDARY_COLOR, "Excelente");
        JPanel promedioPanel = (JPanel) promedioData[0];
        lblPromedioGeneral = (JLabel) promedioData[1];
        
        Object[] mejorData = crearStatCard("Mejor Materia", "Matemáticas", SUCCESS_COLOR, "92.0");
        JPanel mejorPanel = (JPanel) mejorData[0];
        lblMejorMateria = (JLabel) mejorData[1];
        
        Object[] peorData = crearStatCard("Necesita Mejorar", "Lengua Española", WARNING_COLOR, "85.5");
        JPanel peorPanel = (JPanel) peorData[0];
        lblPeorMateria = (JLabel) peorData[1];
        
        Object[] estadoData = crearStatCard("Estado General", "Excelente", INFO_COLOR, "7/7 Aprobadas");
        JPanel estadoPanel = (JPanel) estadoData[0];
        lblEstadoGeneral = (JLabel) estadoData[1];
        
        panel.add(promedioPanel);
        panel.add(mejorPanel);
        panel.add(peorPanel);
        panel.add(estadoPanel);
        
        return panel;
    }
    
    private Object[] crearStatCard(String titulo, String valor, Color color, String subtitulo) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(color.brighter().brighter());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2, true),
            new EmptyBorder(20, 25, 20, 25)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(80, 80, 80));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValor.setForeground(color.darker().darker());
        lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSubtitulo = new JLabel(subtitulo);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(100, 100, 100));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(lblTitulo);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(lblValor);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(lblSubtitulo);
        
        return new Object[]{card, lblValor};
    }
    
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel("Detalle de Calificaciones por Materia");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(PRIMARY_COLOR);
        lblTitulo.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        panel.add(lblTitulo, BorderLayout.NORTH);
        
        JPanel tablaPanel = crearTablaCalificaciones();
        panel.add(tablaPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearTablaCalificaciones() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2, true));

        // Modelo de tabla
        String[] columnas = {"Materia", "Nota 1", "Nota 2", "Nota 3", "Nota 4", "Promedio", "Estado", "Comentarios"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        cargarCalificacionesEjemplo();

        // Crear tabla
        tablaCalificaciones = new JTable(modeloTabla);
        tablaCalificaciones.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaCalificaciones.setRowHeight(50);
        tablaCalificaciones.setSelectionBackground(new Color(230, 240, 255));
        tablaCalificaciones.setSelectionForeground(Color.BLACK);
        tablaCalificaciones.setShowVerticalLines(false);
        tablaCalificaciones.setIntercellSpacing(new Dimension(0, 1));
        tablaCalificaciones.setFillsViewportHeight(true);
        tablaCalificaciones.setGridColor(new Color(230, 230, 230));

        JTableHeader header = tablaCalificaciones.getTableHeader();
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

        // Renderers personalizados con alternancia de colores
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, 
                                                                 isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                
                // Alternar colores de fila
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

        // Aplicar renderers a todas las columnas excepto Materia y Comentarios
        for (int i = 0; i < tablaCalificaciones.getColumnCount(); i++) {
            if (i != 0 && i != 7) {
                tablaCalificaciones.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        
        // Renderer especial para promedios y estado
        tablaCalificaciones.getColumnModel().getColumn(5).setCellRenderer(new PromedioRenderer());
        tablaCalificaciones.getColumnModel().getColumn(6).setCellRenderer(new EstadoRenderer());

        // Anchos de columna
        tablaCalificaciones.getColumnModel().getColumn(0).setPreferredWidth(150);
        tablaCalificaciones.getColumnModel().getColumn(1).setPreferredWidth(80);
        tablaCalificaciones.getColumnModel().getColumn(2).setPreferredWidth(80);
        tablaCalificaciones.getColumnModel().getColumn(3).setPreferredWidth(80);
        tablaCalificaciones.getColumnModel().getColumn(4).setPreferredWidth(80);
        tablaCalificaciones.getColumnModel().getColumn(5).setPreferredWidth(100);
        tablaCalificaciones.getColumnModel().getColumn(6).setPreferredWidth(100);
        tablaCalificaciones.getColumnModel().getColumn(7).setPreferredWidth(200);

        JScrollPane scrollPane = new JScrollPane(tablaCalificaciones);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
    
    private void cargarCalificacionesEjemplo() {
        modeloTabla.addRow(new Object[]{
            "Matemáticas", 
            "95.0", "90.0", "92.0", "91.0",
            "92.0",
            "Excelente",
            "Destacado desempeño en esta materia"
        });
        
        modeloTabla.addRow(new Object[]{
            "Lengua Española", 
            "85.0", "82.0", "88.0", "87.0",
            "85.5", 
            "Bueno",
            "Necesita mejorar en redacción"
        });
        
        modeloTabla.addRow(new Object[]{
            "Ciencias Naturales", 
            "92.0", "88.0", "91.0", "89.0",
            "90.0",
            "Excelente", 
            "Muy buen desempeño en experimentos"
        });
        
        modeloTabla.addRow(new Object[]{
            "Ciencias Sociales", 
            "86.0", "85.0", "89.0", "88.0",
            "87.0",
            "Bueno",
            "Participación activa en clase"
        });
        
        modeloTabla.addRow(new Object[]{
            "Inglés", 
            "90.0", "88.0", "91.0", "89.0",
            "89.5",
            "Bueno", 
            "Buena pronunciación y vocabulario"
        });
        
        modeloTabla.addRow(new Object[]{
            "Educación Física", 
            "95.0", "96.0", "94.0", "95.0",
            "95.0",
            "Excelente",
            "Destacado en actividades deportivas"
        });
        
        modeloTabla.addRow(new Object[]{
            "Arte y Cultura", 
            "92.0", "90.0", "92.0", "90.0",
            "91.0",
            "Excelente", 
            "Creatividad y talento artístico notable"
        });
    }
    
    private void actualizarCalificaciones() {
        JOptionPane.showMessageDialog(this,
            "Filtrando calificaciones para:\n" +
            "Período: " + cmbPeriodo.getSelectedItem() + "\n" +
            "Materia: " + cmbMateria.getSelectedItem(),
            "Filtrando",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Renderer para promedios
    class PromedioRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(JLabel.CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            
            if (value != null && !value.toString().isEmpty()) {
                try {
                    double promedio = Double.parseDouble(value.toString());
                    
                    if (promedio >= 90) {
                        setForeground(SUCCESS_COLOR);
                        setBackground(SUCCESS_COLOR.brighter().brighter());
                    } else if (promedio >= 80) {
                        setForeground(WARNING_COLOR);
                        setBackground(WARNING_COLOR.brighter().brighter());
                    } else if (promedio >= 70) {
                        setForeground(SECONDARY_COLOR);
                        setBackground(SECONDARY_COLOR.brighter().brighter());
                    } else {
                        setForeground(DANGER_COLOR);
                        setBackground(DANGER_COLOR.brighter().brighter());
                    }
                } catch (NumberFormatException e) {
                    setForeground(Color.BLACK);
                    setBackground(Color.WHITE);
                }
            }
            
            return c;
        }
    }
    
    // Renderer para estado
    class EstadoRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(JLabel.CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            
            if (value != null) {
                String estado = value.toString();
                if (estado.contains("Excelente")) {
                    setForeground(SUCCESS_COLOR.darker());
                    setBackground(SUCCESS_COLOR.brighter().brighter());
                } else if (estado.contains("Bueno")) {
                    setForeground(WARNING_COLOR.darker());
                    setBackground(WARNING_COLOR.brighter().brighter());
                } else {
                    setForeground(DANGER_COLOR.darker());
                    setBackground(DANGER_COLOR.brighter().brighter());
                }
            }
            
            return c;
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new CalificacionesPadreApp());
    }
}