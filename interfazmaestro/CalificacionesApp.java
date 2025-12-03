package com.interfazmaestro;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class CalificacionesApp extends JFrame {
    
    private JTable tablaCalificaciones;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> cmbEstudiante;
    private JComboBox<String> cmbPeriodo;
    private JComboBox<String> cmbMateria;
    private JLabel lblPromedio, lblAprobadas, lblReprobadas, lblMejorNota;
    
    // Colores del sistema
    private static final Color PRIMARY_COLOR = new Color(25, 25, 112);
    private static final Color SUCCESS_COLOR = new Color(60, 179, 113);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color WARNING_COLOR = new Color(255, 140, 0);
    private static final Color LIGHT_BG = new Color(248, 249, 250);
    
    public CalificacionesApp() {
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
                Color color1 = new Color(240, 242, 245); // Color superior
                Color color2 = new Color(255, 255, 255); // Color inferior
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2); // De arriba hacia abajo
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
        
        JLabel lblIcono = new JLabel("📚");
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 35));
        
        JLabel lblTitulo = new JLabel("Registro de Calificaciones");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(PRIMARY_COLOR);
        
        titlePanel.add(lblIcono);
        titlePanel.add(lblTitulo);
        
        // Panel de botones derecho
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        botonesPanel.setBackground(Color.WHITE);
        
        // Botón Guardar
        JButton btnGuardar = new JButton("Guardar Calificaciones");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setBackground(SUCCESS_COLOR);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setPreferredSize(new Dimension(200, 45));
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
        
        btnGuardar.addActionListener(e -> guardarCalificaciones());
        
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
        JPanel tablaPanel = crearTablaCalificaciones();
        content.add(tablaPanel, BorderLayout.CENTER);
        
        // Panel de estadísticas
        JPanel statsPanel = crearPanelEstadisticas();
        content.add(statsPanel, BorderLayout.SOUTH);
        
        return content;
    }
    
    private JPanel crearPanelControles() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 0));
        panel.setOpaque(false);
        
        // Selector de Estudiante
        JPanel estudiantePanel = new JPanel(new BorderLayout(10, 0));
        estudiantePanel.setOpaque(false);
        
        JLabel lblEstudiante = new JLabel("Estudiante:");
        lblEstudiante.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblEstudiante.setForeground(new Color(70, 70, 70));
        
        String[] estudiantes = {
            "Seleccionar estudiante",
            "María García López (2024001)",
            "Carlos Rodríguez Pérez (2024002)", 
            "Ana Martínez Díaz (2024003)",
            "Luis Fernández Santos (2024004)",
            "Sofía Ramírez Cruz (2024005)"
        };
        cmbEstudiante = new JComboBox<>(estudiantes);
        cmbEstudiante.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbEstudiante.setPreferredSize(new Dimension(270, 45));
        cmbEstudiante.setBackground(Color.WHITE);
        cmbEstudiante.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true));
        cmbEstudiante.addActionListener(e -> cargarCalificacionesEstudiante());
        
        estudiantePanel.add(lblEstudiante, BorderLayout.WEST);
        estudiantePanel.add(cmbEstudiante, BorderLayout.CENTER);
        
        // Selector de Período
        JPanel periodoPanel = new JPanel(new BorderLayout(10, 0));
        periodoPanel.setOpaque(false);
        
        JLabel lblPeriodo = new JLabel("Período:");
        lblPeriodo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblPeriodo.setForeground(new Color(70, 70, 70));
        
        String[] periodos = {"Primer Trimestre", "Segundo Trimestre", "Tercer Trimestre", "Examen Final"};
        cmbPeriodo = new JComboBox<>(periodos);
        cmbPeriodo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbPeriodo.setPreferredSize(new Dimension(190, 45));
        cmbPeriodo.setBackground(Color.WHITE);
        cmbPeriodo.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true));
        cmbPeriodo.addActionListener(e -> cargarCalificacionesEstudiante());
        
        periodoPanel.add(lblPeriodo, BorderLayout.WEST);
        periodoPanel.add(cmbPeriodo, BorderLayout.CENTER);
        
        panel.add(estudiantePanel);
        panel.add(periodoPanel);
        
        return panel;
    }
    
    private JPanel crearTablaCalificaciones() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2, true));

        // Modelo de tabla
        String[] columnas = {"Materia", "Nota 1", "Nota 2", "Nota 3", "Nota 4", "Promedio", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 1 && column <= 4;
            }
            
            @Override
            public void setValueAt(Object value, int row, int column) {
                super.setValueAt(value, row, column);
                if (column >= 1 && column <= 4) {
                    calcularPromedioFila(row);
                    actualizarEstadisticas();
                }
            }
        };

        // Crear tabla
        tablaCalificaciones = new JTable(modeloTabla);
        tablaCalificaciones.setFont(new Font("Segoe UI", Font.PLAIN, 13)); // Fuente base
        tablaCalificaciones.setRowHeight(50); // Altura de fila
        tablaCalificaciones.setSelectionBackground(new Color(230, 240, 255)); // 
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

        for (int i = 1; i < tablaCalificaciones.getColumnCount(); i++) {
            tablaCalificaciones.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        tablaCalificaciones.getColumnModel().getColumn(5).setCellRenderer(new PromedioRenderer());
        tablaCalificaciones.getColumnModel().getColumn(6).setCellRenderer(new EstadoRenderer());

        // Anchos de columna
        tablaCalificaciones.getColumnModel().getColumn(0).setPreferredWidth(200);
        tablaCalificaciones.getColumnModel().getColumn(1).setPreferredWidth(90);
        tablaCalificaciones.getColumnModel().getColumn(2).setPreferredWidth(90);
        tablaCalificaciones.getColumnModel().getColumn(3).setPreferredWidth(90);
        tablaCalificaciones.getColumnModel().getColumn(4).setPreferredWidth(90);
        tablaCalificaciones.getColumnModel().getColumn(5).setPreferredWidth(110);
        tablaCalificaciones.getColumnModel().getColumn(6).setPreferredWidth(130);

        JScrollPane scrollPane = new JScrollPane(tablaCalificaciones);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
    
    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 15));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 0, 0, 0));

        Object[] promedioData = crearStatCard("Promedio General", "0.0", new Color(70, 130, 180));
        JPanel promedioPanel = (JPanel) promedioData[0];
        lblPromedio = (JLabel) promedioData[1];

        Object[] aprobadasData = crearStatCard("Aprobadas", "0", SUCCESS_COLOR);
        JPanel aprobadasPanel = (JPanel) aprobadasData[0];
        lblAprobadas = (JLabel) aprobadasData[1];

        Object[] reprobadasData = crearStatCard("Reprobadas", "0", DANGER_COLOR);
        JPanel reprobadasPanel = (JPanel) reprobadasData[0];
        lblReprobadas = (JLabel) reprobadasData[1];

        Object[] mejorData = crearStatCard("Mejor Nota", "0.0", WARNING_COLOR);
        JPanel mejorPanel = (JPanel) mejorData[0];
        lblMejorNota = (JLabel) mejorData[1];

        panel.add(promedioPanel);
        panel.add(aprobadasPanel);
        panel.add(reprobadasPanel);
        panel.add(mejorPanel);

        return panel;
    }
    
    private Object[] crearStatCard(String titulo, String valor, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(color.brighter().brighter());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2, true),
            new EmptyBorder(20, 25, 20, 25)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitulo.setForeground(new Color(80, 80, 80));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValor.setForeground(color.darker().darker());
        lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(lblTitulo);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(lblValor);
        
        return new Object[]{card, lblValor};
    }
    
    private void cargarCalificacionesEstudiante() {
        modeloTabla.setRowCount(0);
        
        if (cmbEstudiante.getSelectedIndex() == 0) {
            return;
        }
        
        String[] materias = {
            "Matemáticas", "Lengua Española", "Ciencias Naturales",
            "Ciencias Sociales", "Inglés", "Educación Física", "Arte y Cultura"
        };
        
        for (String materia : materias) {
            double nota1 = Math.round((Math.random() * 30 + 70) * 10.0) / 10.0;
            double nota2 = Math.round((Math.random() * 30 + 70) * 10.0) / 10.0;
            double nota3 = Math.round((Math.random() * 30 + 70) * 10.0) / 10.0;
            double nota4 = Math.round((Math.random() * 30 + 70) * 10.0) / 10.0;
            
            double promedio = Math.round(((nota1 + nota2 + nota3 + nota4) / 4) * 10.0) / 10.0;
            String estado = promedio >= 70 ? "Aprobado" : "Reprobado";
            
            modeloTabla.addRow(new Object[]{
                materia, String.valueOf(nota1), String.valueOf(nota2),
                String.valueOf(nota3), String.valueOf(nota4),
                String.valueOf(promedio), estado
            });
        }
        
        actualizarEstadisticas();
    }
    
    private void calcularPromedioFila(int row) {
        try {
            double suma = 0;
            int contador = 0;
            
            for (int col = 1; col <= 4; col++) {
                Object valor = modeloTabla.getValueAt(row, col);
                if (valor != null && !valor.toString().isEmpty()) {
                    double nota = Double.parseDouble(valor.toString());
                    if (nota >= 0 && nota <= 100) {
                        suma += nota;
                        contador++;
                    }
                }
            }
            
            if (contador > 0) {
                double promedio = Math.round((suma / contador) * 10.0) / 10.0;
                modeloTabla.setValueAt(String.valueOf(promedio), row, 5);
                
                String estado = promedio >= 70 ? "Aprobado" : "Reprobado";
                modeloTabla.setValueAt(estado, row, 6);
            }
        } catch (NumberFormatException e) {
            // Ignorar errores de formato
        }
    }
    
    private void actualizarEstadisticas() {
        double sumaTotal = 0;
        int materiasAprobadas = 0;
        int materiasReprobadas = 0;
        double mejorNota = 0;
        int totalMaterias = modeloTabla.getRowCount();
        
        for (int row = 0; row < totalMaterias; row++) {
            Object valorPromedio = modeloTabla.getValueAt(row, 5);
            if (valorPromedio != null && !valorPromedio.toString().isEmpty()) {
                try {
                    double promedio = Double.parseDouble(valorPromedio.toString());
                    sumaTotal += promedio;
                    
                    if (promedio >= 70) {
                        materiasAprobadas++;
                    } else {
                        materiasReprobadas++;
                    }
                    
                    if (promedio > mejorNota) {
                        mejorNota = promedio;
                    }
                } catch (NumberFormatException e) {
                    // Ignorar
                }
            }
        }
        
        double promedioGeneral = totalMaterias > 0 ? Math.round((sumaTotal / totalMaterias) * 10.0) / 10.0 : 0;
        
        lblPromedio.setText(String.valueOf(promedioGeneral));
        lblAprobadas.setText(String.valueOf(materiasAprobadas));
        lblReprobadas.setText(String.valueOf(materiasReprobadas));
        lblMejorNota.setText(String.valueOf(mejorNota));
    }
    
    private void guardarCalificaciones() {
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "No hay calificaciones para guardar.\nSelecciona un estudiante primero.",
                "Sin Datos",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String estudiante = cmbEstudiante.getSelectedItem().toString();
        String periodo = cmbPeriodo.getSelectedItem().toString();
        
        JOptionPane.showMessageDialog(this,
            "✅ Calificaciones guardadas exitosamente:\n\n" +
            "Estudiante: " + estudiante + "\n" +
            "Período: " + periodo + "\n" +
            "Materias registradas: " + modeloTabla.getRowCount(),
            "Calificaciones Guardadas",
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
            
            if (!isSelected) {
                if (row % 2 == 0) {
                    setBackground(Color.WHITE);
                } else {
                    setBackground(LIGHT_BG);
                }
            }
            
            if (value != null && !value.toString().isEmpty()) {
                try {
                    double promedio = Double.parseDouble(value.toString());
                    
                    if (promedio >= 90) {
                        setForeground(SUCCESS_COLOR);
                    } else if (promedio >= 80) {
                        setForeground(WARNING_COLOR);
                    } else if (promedio >= 70) {
                        setForeground(new Color(65, 105, 225));
                    } else {
                        setForeground(DANGER_COLOR);
                    }
                } catch (NumberFormatException e) {
                    setForeground(Color.BLACK);
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
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            
            if (!isSelected) {
                if (value != null && value.toString().contains("Aprobado")) {
                    setForeground(SUCCESS_COLOR);
                    setBackground(SUCCESS_COLOR.brighter().brighter());
                } else {
                    setForeground(DANGER_COLOR);
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
        
        SwingUtilities.invokeLater(() -> new CalificacionesApp());
    }
}