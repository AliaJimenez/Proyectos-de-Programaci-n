package com.interfazpadre;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class CompetenciaEstudiantilApp extends JFrame {
    
    private final String nombreEstudiante = "María García López";
    private JTable tablaRankingAula, tablaRankingEscuela;
    
    // Colores del sistema
    private static final Color PRIMARY_COLOR = new Color(25, 25, 112); 
    private static final Color PRIMARY_LIGHT = new Color(235, 242, 250); 
    private static final Color SECONDARY_COLOR = new Color(70, 130, 180); 
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69); 
    private static final Color DANGER_COLOR = new Color(220, 53, 69); 
    private static final Color WARNING_COLOR = new Color(255, 193, 7); 
    private static final Color INFO_COLOR = new Color(23, 162, 184); 
    private static final Color ORANGE_COLOR = new Color(253, 126, 20); 
    private static final Color PURPLE_COLOR = new Color(111, 66, 193); 
    private static final Color CARD_BG = Color.WHITE; 
    private static final Color BORDER_COLOR = new Color(230, 230, 230);

    public CompetenciaEstudiantilApp() {
        setTitle("ClassConnect");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1400, 900));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(230, 240, 255); 
                Color color2 = new Color(255, 255, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        
        JPanel headerPanel = crearHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel contentPanel = crearContenido();
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 2, 0, new Color(220, 225, 230)),
            new EmptyBorder(20, 30, 20, 30)
        ));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel("Competencia Estudiantil");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26)); 
        lblTitulo.setForeground(PRIMARY_COLOR);
        
        JLabel lblSubtitulo = new JLabel("Ranking y logros de " + nombreEstudiante);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(100, 100, 100));
        
        JLabel lblSemana = new JLabel(" Semana del 18-22 Nov 2024");
        lblSemana.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSemana.setForeground(new Color(60, 60, 60));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(lblTitulo);
        titlePanel.add(lblSubtitulo);

        leftPanel.add(titlePanel);
        leftPanel.add(lblSemana);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(Color.WHITE);

        JButton btnVolver = crearBoton("← Volver", new Color(140, 145, 150), 120, 40);
        btnVolver.addActionListener(e -> {
            new DashboardPadreApp(); 
            this.dispose();
        });
        
        JButton btnVistaEstudiante = crearBoton("Vista Estudiante", PRIMARY_COLOR, 150, 40);
        btnVistaEstudiante.addActionListener(e -> {
            new AmbienteEstudiantilApp(nombreEstudiante);
            this.dispose();
        });

        rightPanel.add(btnVistaEstudiante);
        rightPanel.add(btnVolver);
        
        header.add(leftPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        
        return header;
    }
    
    private JButton crearBoton(String texto, Color color, int width, int height) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(color, 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        btn.setPreferredSize(new Dimension(width, height));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
        
        return btn;
    }
    
    private JPanel crearContenido() {
        JPanel content = new JPanel(new BorderLayout(0, 25)); 
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(25, 35, 35, 35));
        
        JPanel statsPanel = crearPanelEstadisticas();
        content.add(statsPanel, BorderLayout.NORTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(700);
        splitPane.setDividerSize(4); 
        splitPane.setBorder(null);
        splitPane.setOpaque(false);
        
        JPanel panelIzquierdo = crearPanelIzquierdo();
        splitPane.setLeftComponent(panelIzquierdo);
        
        JPanel panelDerecho = crearPanelDerecho();
        splitPane.setRightComponent(panelDerecho);
        
        content.add(splitPane, BorderLayout.CENTER);
        
        return content;
    }
    
    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 20, 0)); 
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 30, 0));
        
        panel.add(crearStatCard("📊", "Posición en Aula", "2°", "De 25 estudiantes", 
            SECONDARY_COLOR, "Subió 1 posición"));
        
        panel.add(crearStatCard("🏫", "Posición en Escuela", "15°", "De 300 estudiantes", 
            SUCCESS_COLOR, "Subió 3 posiciones"));
        
        panel.add(crearStatCard("⭐", "Puntos de Mérito", "185", "Esta semana", 
            ORANGE_COLOR, "+25 puntos"));
        
        panel.add(crearStatCard("📈", "Progreso Semanal", "+12%", "vs semana anterior", 
            PURPLE_COLOR, "Tendencia positiva"));
        
        return panel;
    }
    
    private JPanel crearStatCard(String icono, String titulo, String valor, String subtitulo, 
                                Color color, String tendencia) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(CARD_BG);
        
        card.setBorder(new CompoundBorder(
            new MatteBorder(4, 1, 1, 1, color), 
            new EmptyBorder(20, 25, 20, 25)
        ));
        
        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        lblIcono.setForeground(color); 
        lblIcono.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(80, 80, 80));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 32)); 
        lblValor.setForeground(color.darker());
        lblValor.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblSubtitulo = new JLabel(subtitulo);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(120, 120, 120));
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblTendencia = new JLabel(tendencia);
        lblTendencia.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTendencia.setForeground(SUCCESS_COLOR);
        lblTendencia.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(CARD_BG);
        contentPanel.add(lblIcono);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(lblTitulo);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(lblValor);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(lblSubtitulo);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(lblTendencia);
        
        card.add(contentPanel, BorderLayout.CENTER);
        return card;
    }
    
    private JPanel crearPanelIzquierdo() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 0, 15)); 
        
        JPanel rankingAulaPanel = crearPanelRankingAula();
        panel.add(rankingAulaPanel);
        
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        JPanel asignacionesPanel = crearPanelAsignaciones();
        panel.add(asignacionesPanel);
        
        return panel;
    }
    
    private JPanel crearHeaderSeccion(String titulo, String icono) {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        headerPanel.setBackground(PRIMARY_LIGHT); 
        headerPanel.setBorder(new MatteBorder(0, 0, 2, 0, new Color(220, 230, 240)));

        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(PRIMARY_COLOR);
        
        headerPanel.add(lblIcono);
        headerPanel.add(lblTitulo);
        return headerPanel;
    }

    private JPanel crearPanelRankingAula() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(CARD_BG);
        container.setBorder(new CompoundBorder(
            new MatteBorder(3, 0, 0, 0, PRIMARY_COLOR),
            new MatteBorder(0, 1, 1, 1, BORDER_COLOR)
        ));
        
        container.add(crearHeaderSeccion("Ranking de tu Aula - 3ro Primaria A", "👥"), BorderLayout.NORTH);
        
        String[] columnas = {"Posición", "Estudiante", "Puntos", "Tendencia"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        
        Object[][] datosAula = {
            {"1°", "Carlos Rodríguez Pérez", "210", "+5"},
            {"2°", "María García López", "185", "+25"},
            {"3°", "Ana Martínez Díaz", "175", "-10"},
            {"4°", "Luis Fernández Santos", "160", "+15"},
            {"5°", "Diego Morales Ortiz", "155", "+8"},
            {"6°", "Sofía Hernández Ruiz", "150", "-5"},
            {"7°", "Miguel Ángel Castro", "145", "+12"},
            {"8°", "Valentina López García", "140", "0"},
            {"9°", "Javier Ramírez Méndez", "135", "+7"},
            {"10°", "Camila Torres Díaz", "130", "-3"}
        };
        
        for (Object[] fila : datosAula) {
            modelo.addRow(fila);
        }
        
        tablaRankingAula = new JTable(modelo);
        tablaRankingAula.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaRankingAula.setRowHeight(40); 
        tablaRankingAula.setSelectionBackground(new Color(220, 235, 255)); 
        tablaRankingAula.setSelectionForeground(Color.BLACK);
        tablaRankingAula.setShowGrid(false);
        tablaRankingAula.setIntercellSpacing(new Dimension(0, 0));
        
        JTableHeader header = tablaRankingAula.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(245, 248, 250)); 
        header.setForeground(PRIMARY_COLOR); 
        header.setBorder(new MatteBorder(0, 0, 2, 0, new Color(220, 230, 240)));
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        
        tablaRankingAula.getColumnModel().getColumn(0).setCellRenderer(new PosicionRenderer());
        tablaRankingAula.getColumnModel().getColumn(3).setCellRenderer(new TendenciaRenderer());
        
        tablaRankingAula.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                String estudiante = table.getValueAt(row, 1).toString();
                if (estudiante.equals(nombreEstudiante)) {
                    c.setBackground(new Color(230, 242, 255)); 
                    c.setFont(new Font("Segoe UI", Font.BOLD, 13));
                } else {
                    c.setBackground(Color.WHITE);
                    c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                }
                setBorder(noFocusBorder); 
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablaRankingAula);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10)); 
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        container.add(scrollPane, BorderLayout.CENTER);
        
        return container;
    }
    
    private JPanel crearPanelAsignaciones() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(CARD_BG);
        container.setBorder(new CompoundBorder(
            new MatteBorder(3, 0, 0, 0, SECONDARY_COLOR), 
            new MatteBorder(0, 1, 1, 1, BORDER_COLOR)
        ));
        
        container.add(crearHeaderSeccion("Asignaciones de la Semana", "📚"), BorderLayout.NORTH);
        
        JPanel asignacionesContent = new JPanel();
        asignacionesContent.setLayout(new BoxLayout(asignacionesContent, BoxLayout.Y_AXIS));
        asignacionesContent.setBackground(CARD_BG);
        asignacionesContent.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        String[][] asignaciones = {
            {"Matemáticas", "Problemas de multiplicación", "Para el 20/11", "Completada"},
            {"Lengua Española", "Redacción: Mi familia", "Para el 21/11", "En progreso"},
            {"Ciencias", "Experimento: Plantas", "Para el 22/11", "Pendiente"},
            {"Arte", "Dibujo libre - Técnica acuarela", "Para el 23/11", "Pendiente"},
            {"Educación Física", "Rutina de ejercicios diaria", "Para el 24/11", "En progreso"}
        };
        
        for (String[] asignacion : asignaciones) {
            JPanel cardAsignacion = crearCardAsignacion(asignacion[0], asignacion[1], asignacion[2], asignacion[3]);
            asignacionesContent.add(cardAsignacion);
            asignacionesContent.add(Box.createRigidArea(new Dimension(0, 12)));
        }
        
        JScrollPane scrollPane = new JScrollPane(asignacionesContent);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(CARD_BG);
        
        container.add(scrollPane, BorderLayout.CENTER);
        
        return container;
    }
    
    private JPanel crearCardAsignacion(String materia, String descripcion, String fecha, String estado) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(new Color(252, 253, 255)); 
        
        Color estadoColor = estado.contains("Completada") ? SUCCESS_COLOR : (estado.contains("En progreso") ? WARNING_COLOR : DANGER_COLOR);
        
        card.setBorder(new CompoundBorder(
            new MatteBorder(0, 4, 0, 0, estadoColor), 
            new CompoundBorder(
                    new MatteBorder(1, 0, 1, 1, BORDER_COLOR),
                    new EmptyBorder(12, 15, 12, 15)
            )
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        
        JLabel lblMateria = new JLabel(materia);
        lblMateria.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMateria.setForeground(new Color(40, 40, 40));
        
        JLabel lblDescripcion = new JLabel(descripcion);
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDescripcion.setForeground(new Color(100, 100, 100));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.add(lblMateria);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        infoPanel.add(lblDescripcion);
        
        JLabel lblFecha = new JLabel(fecha.replace("Para el ", "")); 
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFecha.setForeground(new Color(120, 120, 120));
        lblFecha.setHorizontalAlignment(SwingConstants.RIGHT);
        
        JLabel lblEstado = new JLabel(estado);
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblEstado.setForeground(estadoColor);
        lblEstado.setHorizontalAlignment(SwingConstants.RIGHT);
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        rightPanel.add(lblFecha);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        rightPanel.add(lblEstado);
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);
        
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { card.setBackground(new Color(245, 250, 255)); }
            public void mouseExited(MouseEvent e) { card.setBackground(new Color(252, 253, 255)); }
        });
        
        return card;
    }
    
    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 15, 0, 0)); 
        
        JPanel rankingEscuelaPanel = crearPanelRankingEscuela();
        panel.add(rankingEscuelaPanel);
        
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        JPanel accionDestacadaPanel = crearPanelAccionDestacada();
        panel.add(accionDestacadaPanel);
        
        return panel;
    }
    
    private JPanel crearPanelRankingEscuela() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(CARD_BG);
        container.setBorder(new CompoundBorder(
            new MatteBorder(3, 0, 0, 0, PURPLE_COLOR), 
            new MatteBorder(0, 1, 1, 1, BORDER_COLOR)
        ));
        
        container.add(crearHeaderSeccion("Ranking General de la Escuela", "🏫"), BorderLayout.NORTH);
        
        String[] columnas = {"Posición", "Estudiante", "Grado", "Puntos"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        
        Object[][] datosEscuela = {
            {"1°", "Santiago López Mendoza", "5to A", "295"},
            {"2°", "Valeria Castro Ruiz", "4to B", "285"},
            {"3°", "Mateo Herrera Díaz", "6to A", "280"},
            {"4°", "Isabella Ramírez Soto", "5to B", "275"},
            {"5°", "Sebastián Morales Vega", "4to A", "270"},
            {"...", "...", "...", "..."},
            {"14°", "Diego Silva Pérez", "3ro B", "190"},
            {"15°", "María García López", "3ro A", "185"},
            {"16°", "Andrea Rojas Castro", "3ro C", "180"},
            {"17°", "Fernando Díaz López", "3ro A", "175"}
        };
        
        for (Object[] fila : datosEscuela) {
            modelo.addRow(fila);
        }
        
        tablaRankingEscuela = new JTable(modelo);
        tablaRankingEscuela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaRankingEscuela.setRowHeight(40);
        tablaRankingEscuela.setSelectionBackground(new Color(220, 235, 255));
        tablaRankingEscuela.setSelectionForeground(Color.BLACK);
        tablaRankingEscuela.setShowGrid(false);
        tablaRankingEscuela.setIntercellSpacing(new Dimension(0, 0));
        
        JTableHeader header = tablaRankingEscuela.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(245, 248, 250));
        header.setForeground(PRIMARY_COLOR);
        header.setBorder(new MatteBorder(0, 0, 2, 0, new Color(220, 230, 240)));
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        
        tablaRankingEscuela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                String estudiante = table.getValueAt(row, 1).toString();
                if (estudiante.equals(nombreEstudiante)) {
                    c.setBackground(new Color(230, 242, 255));
                    c.setFont(new Font("Segoe UI", Font.BOLD, 13));
                } else {
                    c.setBackground(Color.WHITE);
                    c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                }
                setBorder(noFocusBorder);
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablaRankingEscuela);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        container.add(scrollPane, BorderLayout.CENTER);
        
        return container;
    }
    
    private JPanel crearPanelAccionDestacada() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(CARD_BG);
        container.setBorder(new CompoundBorder(
            new MatteBorder(3, 0, 0, 0, ORANGE_COLOR), 
            new MatteBorder(0, 1, 1, 1, BORDER_COLOR)
        ));
        
        container.add(crearHeaderSeccion("Acción Destacada de la Semana", "🌟"), BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBackground(new Color(255, 252, 245)); 
        contentPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        JLabel lblIcono = new JLabel("🏆");
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64)); 
        lblIcono.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblDescripcion = new JLabel("<html><div style='text-align: justify;'>" +
            "<font size='5' color='#E67E22'><b>¡Excelente trabajo en matemáticas!</b></font><br><br>" +
            "<font size='4'>María resolvió correctamente 15 problemas de multiplicación complejos " +
            "y ayudó a dos compañeros que tenían dificultades. Su actitud colaborativa " +
            "y su excelente desempeño le han valido <font color='#28a745'><b>25 puntos de mérito extra</b></font>.</font>" +
            "</div></html>");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDescripcion.setForeground(new Color(60, 60, 60));
        
        contentPanel.add(lblIcono, BorderLayout.WEST);
        contentPanel.add(lblDescripcion, BorderLayout.CENTER);
        
        container.add(contentPanel, BorderLayout.CENTER);
        
        return container;
    }
    
    class PosicionRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            String posicion = value.toString();
            if (posicion.contains("1")) {
                c.setForeground(new Color(218, 165, 32)); 
            } else if (posicion.contains("2")) {
                c.setForeground(new Color(169, 169, 169)); 
            } else if (posicion.contains("3")) {
                c.setForeground(new Color(205, 127, 50)); 
            } else {
                c.setForeground(new Color(80, 80, 80));
            }
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            return c;
        }
    }
    
    class TendenciaRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            String tendencia = value.toString();
            if (tendencia.contains("+")) {
                c.setForeground(SUCCESS_COLOR);
            } else if (tendencia.contains("-")) {
                c.setForeground(DANGER_COLOR);
            } else {
                c.setForeground(new Color(120, 120, 120));
            }
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            return c;
        }
    }
    
    class AmbienteEstudiantilApp extends JFrame {

        private final String nombreEstudiante;
        private static final Color PRIMARY_STUDENT = new Color(41, 128, 185);
        private static final Color SUCCESS_STUDENT = new Color(46, 204, 113);
        private static final Color CARD_BG = Color.WHITE;

        public AmbienteEstudiantilApp(String nombreEstudiante) {
            this.nombreEstudiante = nombreEstudiante;

            setTitle("Mi Ambiente Estudiantil - " + nombreEstudiante);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setMinimumSize(new Dimension(1400, 900));
            setExtendedState(JFrame.MAXIMIZED_BOTH);

            JPanel mainPanel = new JPanel(new BorderLayout(0, 0)) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                    int w = getWidth();
                    int h = getHeight();
                    Color color1 = new Color(225, 245, 255); 
                    Color color2 = new Color(255, 255, 255);
                    GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, w, h);
                }
            };

            JPanel headerPanel = crearHeader();
            mainPanel.add(headerPanel, BorderLayout.NORTH);

            // CORRECCIÓN AQUÍ: Se añade JScrollPane para evitar que se corte el contenido
            JPanel contentPanel = crearContenido();
            JScrollPane scrollPane = new JScrollPane(contentPanel);
            scrollPane.setBorder(null);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setOpaque(false);
            scrollPane.getVerticalScrollBar().setUnitIncrement(20);
            
            mainPanel.add(scrollPane, BorderLayout.CENTER);

            setContentPane(mainPanel);
            setLocationRelativeTo(null);
            setVisible(true);
        }

        private JPanel crearHeader() {
            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(Color.WHITE);
            header.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 2, 0, new Color(220, 225, 230)),
                new EmptyBorder(20, 30, 20, 30)
            ));

            JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
            leftPanel.setBackground(Color.WHITE);

            JPanel titlePanel = new JPanel();
            titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
            titlePanel.setBackground(Color.WHITE);

            JLabel lblTitulo = new JLabel("Mi Ambiente Estudiantil");
            lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
            lblTitulo.setForeground(PRIMARY_STUDENT);

            JLabel lblSubtitulo = new JLabel("Bienvenido " + nombreEstudiante + " - ¡Sigue esforzándote!");
            lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblSubtitulo.setForeground(new Color(100, 100, 100));
            titlePanel.add(lblTitulo);
            titlePanel.add(lblSubtitulo);
            
            JLabel lblSemana = new JLabel(" Semana del 18-22 Nov 2024");
            lblSemana.setFont(new Font("Segoe UI", Font.BOLD, 15));
            lblSemana.setForeground(new Color(60, 60, 60));

            leftPanel.add(titlePanel);
            leftPanel.add(lblSemana);

            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
            rightPanel.setBackground(Color.WHITE);

            JButton btnVolver = crearBoton("← Volver", new Color(140, 145, 150), 120, 40);
            btnVolver.addActionListener(e -> {
                new CompetenciaEstudiantilApp();
                this.dispose();
            });

            rightPanel.add(btnVolver);

            header.add(leftPanel, BorderLayout.WEST);
            header.add(rightPanel, BorderLayout.EAST);

            return header;
        }

        private JButton crearBoton(String texto, Color color, int width, int height) {
            JButton btn = new JButton(texto);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btn.setBackground(color);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
             btn.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(color, 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            btn.setPreferredSize(new Dimension(width, height));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(color.darker());
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(color);
                }
            });

            return btn;
        }

        private JPanel crearContenido() {
            JPanel content = new JPanel(new BorderLayout(0, 25));
            content.setOpaque(false);
            content.setBorder(new EmptyBorder(25, 35, 35, 35));

            JPanel progresoPanel = crearPanelMiProgreso();
            content.add(progresoPanel, BorderLayout.NORTH);

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setDividerLocation(700);
            splitPane.setDividerSize(4);
            splitPane.setBorder(null);
            splitPane.setOpaque(false);

            JPanel panelIzquierdo = crearPanelIzquierdo();
            splitPane.setLeftComponent(panelIzquierdo);

            JPanel panelDerecho = crearPanelDerecho();
            splitPane.setRightComponent(panelDerecho);

            content.add(splitPane, BorderLayout.CENTER);

            return content;
        }

        private JPanel crearPanelMiProgreso() {
            JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
            panel.setOpaque(false);
            panel.setBorder(new EmptyBorder(0, 0, 30, 0));

            panel.add(crearStatCardEstudiante("🥈", "Mi Posición en Aula", "2° Lugar", "De 25 estudiantes", 
                new Color(169, 169, 169), "¡Casi llegas al 1er lugar!"));

            panel.add(crearStatCardEstudiante("🏆", "Mi Posición en Escuela", "15° Lugar", "De 300 estudiantes", 
                PRIMARY_STUDENT, "Subiste 3 posiciones"));

            panel.add(crearStatCardEstudiante("⭐", "Mis Puntos de Mérito", "185 puntos", "Esta semana", 
                ORANGE_COLOR, "+25 puntos ganados"));

            return panel;
        }

        private JPanel crearStatCardEstudiante(String icono, String titulo, String valor, String subtitulo, 
                                               Color color, String mensaje) {
            JPanel card = new JPanel(new BorderLayout(0, 10));
            card.setBackground(CARD_BG);
            // Borde superior grueso de color
            card.setBorder(new CompoundBorder(
                new MatteBorder(4, 1, 1, 1, color),
                new EmptyBorder(20, 25, 20, 25)
            ));

            JLabel lblIcono = new JLabel(icono);
            lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
            lblIcono.setForeground(color);
            lblIcono.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel lblTitulo = new JLabel(titulo);
            lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblTitulo.setForeground(new Color(80, 80, 80));
            lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel lblValor = new JLabel(valor);
            lblValor.setFont(new Font("Segoe UI", Font.BOLD, 30));
            lblValor.setForeground(color.darker());
            lblValor.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel lblSubtitulo = new JLabel(subtitulo);
            lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblSubtitulo.setForeground(new Color(120, 120, 120));
            lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel lblMensaje = new JLabel(mensaje);
            lblMensaje.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lblMensaje.setForeground(SUCCESS_STUDENT);
            lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);

            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(CARD_BG);
            contentPanel.add(lblIcono);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            contentPanel.add(lblTitulo);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            contentPanel.add(lblValor);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            contentPanel.add(lblSubtitulo);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            contentPanel.add(lblMensaje);

            card.add(contentPanel, BorderLayout.CENTER);
            return card;
        }

        private JPanel crearPanelIzquierdo() {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setOpaque(false);
            panel.setBorder(new EmptyBorder(0, 0, 0, 15));

            JPanel asignacionesPanel = crearPanelMisAsignaciones();
            panel.add(asignacionesPanel);

            panel.add(Box.createRigidArea(new Dimension(0, 25)));

            JPanel rankingAulaPanel = crearPanelRankingAulaEstudiante();
            panel.add(rankingAulaPanel);

            return panel;
        }

        private JPanel crearPanelMisAsignaciones() {
            JPanel container = new JPanel(new BorderLayout());
            container.setBackground(CARD_BG);
             container.setBorder(new CompoundBorder(
                new MatteBorder(3, 0, 0, 0, PRIMARY_STUDENT),
                new MatteBorder(0, 1, 1, 1, BORDER_COLOR)
            ));

            JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            headerPanel.setBackground(new Color(235, 245, 255));
            headerPanel.setBorder(new MatteBorder(0, 0, 2, 0, new Color(220, 230, 245)));
            JLabel lblTitulo = new JLabel("Mis Asignaciones de la Semana");
            lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblTitulo.setForeground(PRIMARY_STUDENT);
            headerPanel.add(lblTitulo);
            container.add(headerPanel, BorderLayout.NORTH);

            JPanel asignacionesContent = new JPanel();
            asignacionesContent.setLayout(new BoxLayout(asignacionesContent, BoxLayout.Y_AXIS));
            asignacionesContent.setBackground(CARD_BG);
            asignacionesContent.setBorder(new EmptyBorder(15, 15, 15, 15));

            String[][] asignaciones = {
                {"Matemáticas", "Problemas de multiplicación", "Para el 20/11", "Completada", "✅"},
                {"Lengua Española", "Redacción: Mi familia", "Para el 21/11", "En progreso", "📝"},
                {"Ciencias", "Experimento: Plantas", "Para el 22/11", "Pendiente", "🌱"},
                {"Arte", "Dibujo libre - Técnica acuarela", "Para el 23/11", "Pendiente", "🎨"},
                {"Educación Física", "Rutina de ejercicios diaria", "Para el 24/11", "En progreso", "⚽"}
            };

            for (String[] asignacion : asignaciones) {
                JPanel cardAsignacion = crearCardAsignacionEstudiante(asignacion[0], asignacion[1], 
                    asignacion[2], asignacion[3], asignacion[4]);
                asignacionesContent.add(cardAsignacion);
                asignacionesContent.add(Box.createRigidArea(new Dimension(0, 12)));
            }

            JScrollPane scrollPane = new JScrollPane(asignacionesContent);
            scrollPane.setBorder(null);
            scrollPane.getViewport().setBackground(CARD_BG);

            container.add(scrollPane, BorderLayout.CENTER);

            return container;
        }

        private JPanel crearCardAsignacionEstudiante(String materia, String descripcion, String fecha, 
                                                    String estado, String emoji) {
            JPanel card = new JPanel(new BorderLayout(15, 0));
            card.setBackground(new Color(252, 253, 255));
            
            Color estadoColor = estado.contains("Completada") ? SUCCESS_STUDENT : (estado.contains("En progreso") ? WARNING_COLOR : DANGER_COLOR);

             card.setBorder(new CompoundBorder(
                new MatteBorder(0, 4, 0, 0, estadoColor),
                new CompoundBorder(
                        new MatteBorder(1, 0, 1, 1, BORDER_COLOR),
                        new EmptyBorder(12, 15, 12, 15)
                )
            ));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));

            JLabel lblEmoji = new JLabel(emoji);
            lblEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

            JLabel lblMateria = new JLabel(materia);
            lblMateria.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblMateria.setForeground(new Color(40, 40, 40));

            JLabel lblDescripcion = new JLabel(descripcion);
            lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblDescripcion.setForeground(new Color(100, 100, 100));

            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setOpaque(false);
            infoPanel.add(lblMateria);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 4)));
            infoPanel.add(lblDescripcion);

            JLabel lblFecha = new JLabel(fecha.replace("Para el ", ""));
            lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lblFecha.setForeground(new Color(120, 120, 120));
            lblFecha.setHorizontalAlignment(SwingConstants.RIGHT);

            JLabel lblEstado = new JLabel(estado);
            lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lblEstado.setForeground(estadoColor);
            lblEstado.setHorizontalAlignment(SwingConstants.RIGHT);

            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.setOpaque(false);
            rightPanel.add(lblFecha);
            rightPanel.add(Box.createRigidArea(new Dimension(0, 6)));
            rightPanel.add(lblEstado);

            card.add(lblEmoji, BorderLayout.WEST);
            card.add(infoPanel, BorderLayout.CENTER);
            card.add(rightPanel, BorderLayout.EAST);
            
             card.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { card.setBackground(new Color(245, 250, 255)); }
                public void mouseExited(MouseEvent e) { card.setBackground(new Color(252, 253, 255)); }
            });

            return card;
        }

        private JPanel crearPanelRankingAulaEstudiante() {
            JPanel container = new JPanel(new BorderLayout());
            container.setBackground(CARD_BG);
            container.setBorder(new CompoundBorder(
                new MatteBorder(3, 0, 0, 0, PRIMARY_STUDENT),
                new MatteBorder(0, 1, 1, 1, BORDER_COLOR)
            ));

            JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            headerPanel.setBackground(new Color(235, 245, 255));
            headerPanel.setBorder(new MatteBorder(0, 0, 2, 0, new Color(220, 230, 245)));
            JLabel lblTitulo = new JLabel("Top 5 de Nuestra Aula");
            lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblTitulo.setForeground(PRIMARY_STUDENT);
            headerPanel.add(lblTitulo);
            container.add(headerPanel, BorderLayout.NORTH);


            String[] columnas = {"Posición", "Estudiante", "Puntos", "Tendencia"};
            DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

            Object[][] datosAula = {
                {"1°", "Carlos Rodríguez Pérez", "210", "+5"},
                {"2°", nombreEstudiante, "185", "+25"},
                {"3°", "Ana Martínez Díaz", "175", "-10"},
                {"4°", "Luis Fernández Santos", "160", "+15"},
                {"5°", "Diego Morales Ortiz", "155", "+8"}
            };

            for (Object[] fila : datosAula) {
                modelo.addRow(fila);
            }

            JTable tabla = new JTable(modelo);
            tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            tabla.setRowHeight(45);
            tabla.setSelectionBackground(new Color(220, 235, 255));
            tabla.setSelectionForeground(Color.BLACK);
            tabla.setShowGrid(false);
            tabla.setIntercellSpacing(new Dimension(0, 0));

            JTableHeader header = tabla.getTableHeader();
            header.setFont(new Font("Segoe UI", Font.BOLD, 13));
            header.setBackground(new Color(245, 248, 250));
            header.setForeground(PRIMARY_STUDENT);
            header.setBorder(new MatteBorder(0, 0, 2, 0, new Color(220, 230, 240)));
            header.setPreferredSize(new Dimension(header.getWidth(), 45));

            tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    String estudiante = table.getValueAt(row, 1).toString();
                    if (estudiante.equals(nombreEstudiante)) {
                        c.setBackground(new Color(255, 245, 220)); // Un dorado muy suave para el estudiante
                        c.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    }
                    setBorder(noFocusBorder);
                    return c;
                }
            });

            JScrollPane scrollPane = new JScrollPane(tabla);
            scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
            scrollPane.getViewport().setBackground(Color.WHITE);

            container.add(scrollPane, BorderLayout.CENTER);

            return container;
        }

        private JPanel crearPanelDerecho() {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setOpaque(false);
            panel.setBorder(new EmptyBorder(0, 15, 0, 0));

            JPanel galeriaPanel = crearPanelGaleria();
            panel.add(galeriaPanel);

            panel.add(Box.createRigidArea(new Dimension(0, 25)));

            JPanel accionDestacadaPanel = crearPanelMiAccionDestacada();
            panel.add(accionDestacadaPanel);

            panel.add(Box.createRigidArea(new Dimension(0, 25)));

            JPanel mensajePanel = crearPanelMensajeMotivacional();
            panel.add(mensajePanel);

            return panel;
        }

        private JPanel crearPanelGaleria() {
            JPanel container = new JPanel(new BorderLayout());
            container.setBackground(CARD_BG);
            container.setBorder(new CompoundBorder(
                new MatteBorder(3, 0, 0, 0, SECONDARY_COLOR),
                new MatteBorder(0, 1, 1, 1, BORDER_COLOR)
            ));

            JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            headerPanel.setBackground(new Color(235, 245, 250));
            headerPanel.setBorder(new MatteBorder(0, 0, 2, 0, new Color(220, 230, 240)));
            JLabel lblTitulo = new JLabel("Mis Momentos en Clase");
            lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblTitulo.setForeground(SECONDARY_COLOR);
            headerPanel.add(lblTitulo);
            container.add(headerPanel, BorderLayout.NORTH);

            JPanel galeriaContent = new JPanel(new GridLayout(2, 2, 15, 15));
            galeriaContent.setBackground(CARD_BG);
            galeriaContent.setBorder(new EmptyBorder(15, 15, 15, 15));

            String[] fotos = {
                "Presentando mi proyecto de ciencias",
                "Trabajando en equipo en matemáticas", 
                "Participando en educación física",
                "Mi dibujo destacado de la semana"
            };

            for (String foto : fotos) {
                JPanel fotoCard = crearCardFoto(foto);
                galeriaContent.add(fotoCard);
            }

            container.add(galeriaContent, BorderLayout.CENTER);

            return container;
        }

        private JPanel crearCardFoto(String descripcion) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(new Color(248, 249, 250));
            card.setBorder(new CompoundBorder(
                new MatteBorder(1, 1, 1, 1, new Color(230, 230, 230)),
                new EmptyBorder(15, 15, 15, 15)
            ));
            card.setPreferredSize(new Dimension(150, 130));

            JLabel lblIcono = new JLabel("📷");
            lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
            lblIcono.setForeground(SECONDARY_COLOR);
            lblIcono.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel lblDescripcion = new JLabel("<html><div style='text-align: center;'>" + descripcion + "</div></html>");
            lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lblDescripcion.setForeground(new Color(100, 100, 100));
            lblDescripcion.setHorizontalAlignment(SwingConstants.CENTER);

            card.add(lblIcono, BorderLayout.CENTER);
            card.add(lblDescripcion, BorderLayout.SOUTH);
            
            card.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { card.setBackground(new Color(240, 245, 250)); }
                public void mouseExited(MouseEvent e) { card.setBackground(new Color(248, 249, 250)); }
            });

            return card;
        }

        private JPanel crearPanelMiAccionDestacada() {
            JPanel container = new JPanel(new BorderLayout());
            container.setBackground(CARD_BG);
             container.setBorder(new CompoundBorder(
                new MatteBorder(3, 0, 0, 0, ORANGE_COLOR),
                new MatteBorder(0, 1, 1, 1, BORDER_COLOR)
            ));

            JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            headerPanel.setBackground(new Color(255, 245, 235));
            headerPanel.setBorder(new MatteBorder(0, 0, 2, 0, new Color(250, 230, 220)));
            JLabel lblTitulo = new JLabel("Mi Acción Destacada de la Semana");
            lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblTitulo.setForeground(ORANGE_COLOR);
            headerPanel.add(lblTitulo);
            container.add(headerPanel, BorderLayout.NORTH);

            JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
            contentPanel.setBackground(new Color(255, 250, 240));
            contentPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

            JLabel lblIcono = new JLabel("🏆");
            lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
            lblIcono.setHorizontalAlignment(SwingConstants.CENTER);
            
            JLabel lblDescripcion = new JLabel("<html><div style='text-align: justify;'>" +
                "<font size='5' color='#E67E22'><b>¡Ayudaste a tus compañeros!</b></font><br><br>" +
                "<font size='4'>Esta semana demostraste gran solidaridad al explicar problemas " +
                "de multiplicación a Ana y Luis. Tu paciencia y claridad les " +
                "ayudaron mucho. ¡Sigue así!</font>" +
                "</div></html>");
            lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblDescripcion.setForeground(new Color(60, 60, 60));

            contentPanel.add(lblIcono, BorderLayout.WEST);
            contentPanel.add(lblDescripcion, BorderLayout.CENTER);

            container.add(contentPanel, BorderLayout.CENTER);

            return container;
        }

        private JPanel crearPanelMensajeMotivacional() {
            JPanel container = new JPanel(new BorderLayout());
            container.setBackground(new Color(255, 252, 235)); // Fondo amarillo muy claro
            container.setBorder(new CompoundBorder(
                new MatteBorder(2, 2, 2, 2, new Color(255, 230, 150)), // Borde dorado suave
                new EmptyBorder(25, 25, 25, 25)
            ));

            JLabel lblTitulo = new JLabel("¡Sigue Así!");
            lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblTitulo.setForeground(new Color(180, 140, 0));
            lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
            lblTitulo.setBorder(new EmptyBorder(0, 0, 15, 0));

            JLabel lblMensaje = new JLabel("<html><div style='text-align: center;'>" +
                "<font size='4'>Estás haciendo un excelente trabajo. Tu esfuerzo y dedicación " +
                "te están llevando cada vez más cerca del primer lugar.<br><br>" +
                "<b>¡Tú puedes lograrlo!</b></font>" +
                "</div></html>");
            lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblMensaje.setForeground(new Color(120, 100, 0));
            lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);

            container.add(lblTitulo, BorderLayout.NORTH);
            container.add(lblMensaje, BorderLayout.CENTER);

            return container;
        }   
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new CompetenciaEstudiantilApp();
        });
    }
}