package com.interfazpadre;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ProgresoPadreApp extends JFrame {
    
    private final String nombreEstudiante = "María García López";
    
    // Paleta de Colores
    private static final Color BG_COLOR = new Color(240, 244, 248); // Gris azulado muy suave
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(30, 41, 59); // Slate dark
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139); // Slate gray
    private static final Color ACCENT_COLOR = new Color(79, 70, 229); // Indigo vibrante
    // Colores de estado (Notas)
    private static final Color GRADE_EXCELLENT = new Color(16, 185, 129); // Esmeralda
    private static final Color GRADE_GOOD = new Color(245, 158, 11); // Ámbar
    private static final Color GRADE_LOW = new Color(239, 68, 68); // Rojo
    
    public ProgresoPadreApp() {
        setTitle("ClassConnect");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1280, 800));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);
        
        mainPanel.add(crearHeader(), BorderLayout.NORTH);
        
        JScrollPane scroll = new JScrollPane(crearCuerpo());
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(scroll, BorderLayout.CENTER);

        JPanel headerPanel = crearHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 2, 0, new Color(226, 232, 240)),
            new EmptyBorder(20, 30, 20, 30)
        ));
        
        // Títulos
        JPanel titles = new JPanel(new GridLayout(2, 1));
        titles.setOpaque(false);
        
        JLabel lblTitulo = new JLabel("Progreso Académico");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(TEXT_PRIMARY);
        
        JLabel lblSub = new JLabel("Año Escolar 2024-2025 • " + nombreEstudiante);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(TEXT_SECONDARY);
        
        titles.add(lblTitulo);
        titles.add(lblSub);
        
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
        
        header.add(titles, BorderLayout.WEST);
        header.add(btnVolver, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel crearCuerpo() {
        JPanel body = new JPanel(new BorderLayout(0, 30));
        body.setBackground(BG_COLOR);
        body.setBorder(new EmptyBorder(30, 40, 40, 40));
        
        // 1. Sección de Tarjetas de Resumen (KPIs)
        JPanel kpiPanel = new JPanel(new GridLayout(1, 3, 25, 0));
        kpiPanel.setOpaque(false);
        kpiPanel.setPreferredSize(new Dimension(0, 140));
        
        kpiPanel.add(crearKpiCard("Promedio General", "92.5", "+1.2% vs mes anterior", ACCENT_COLOR, "📈"));
        kpiPanel.add(crearKpiCard("Materias Aprobadas", "8/8", "100% Completado", GRADE_EXCELLENT, "📚"));
        kpiPanel.add(crearKpiCard("Asistencia", "98%", "Solo 1 falta justificada", new Color(14, 165, 233), "✅")); // Azul cielo
        
        body.add(kpiPanel, BorderLayout.NORTH);
        
        //Sección Principal (Lista de Materias y Panel Lateral)
        JPanel contentGrid = new JPanel(new BorderLayout(30, 0));
        contentGrid.setOpaque(false);
        
        //Lista de Materias --
        JPanel materiasPanel = crearListaMaterias();
        contentGrid.add(materiasPanel, BorderLayout.CENTER);
        
        //Análisis y Recomendaciones --
        JPanel sidePanel = crearPanelLateral();

        // Dimensiones fijas para el panel lateral
        sidePanel.setPreferredSize(new Dimension(380, 0));
        contentGrid.add(sidePanel, BorderLayout.EAST);
        
        body.add(contentGrid, BorderLayout.CENTER);
        
        return body;
    }
    
    private JPanel crearListaMaterias() {
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        
        JLabel lblTitle = new JLabel("Desglose por Asignatura");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(TEXT_PRIMARY);
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        container.add(lblTitle, BorderLayout.NORTH);
        
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);
        
        // Datos de ejemplo
        list.add(crearFilaMateria("Matemáticas", 95, "Excelente dominio de multiplicación"));
        list.add(Box.createRigidArea(new Dimension(0, 15)));
        list.add(crearFilaMateria("Lengua Española", 88, "Mejorando redacción"));
        list.add(Box.createRigidArea(new Dimension(0, 15)));
        list.add(crearFilaMateria("Ciencias Naturales", 92, "Gran participación en laboratorio"));
        list.add(Box.createRigidArea(new Dimension(0, 15)));
        list.add(crearFilaMateria("Ciencias Sociales", 85, "Reforzar historia local"));
        list.add(Box.createRigidArea(new Dimension(0, 15)));
        list.add(crearFilaMateria("Inglés", 90, "Vocabulary test: 10/10"));
        list.add(Box.createRigidArea(new Dimension(0, 15)));
        list.add(crearFilaMateria("Educación Artística", 98, "Proyecto final destacado"));
        
        container.add(list, BorderLayout.CENTER);
        return container;
    }
    
    private JPanel crearFilaMateria(String materia, int nota, String comentario) {
        JPanel row = new JPanel(new BorderLayout(20, 0));
        row.setBackground(CARD_BG);
        row.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true), // Borde sutil
            new EmptyBorder(20, 25, 20, 25)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        //Info Izquierda (Nombre y Comentario)
        JPanel info = new JPanel(new GridLayout(2, 1, 0, 5));
        info.setOpaque(false);
        
        JLabel lblName = new JLabel(materia);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblName.setForeground(TEXT_PRIMARY);
        
        JLabel lblComment = new JLabel(comentario);
        lblComment.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblComment.setForeground(TEXT_SECONDARY);
        
        info.add(lblName);
        info.add(lblComment);
        
        //Centro (Barra de Progreso)
        JPanel progressContainer = new JPanel(new BorderLayout(0, 5));
        progressContainer.setOpaque(false);
        progressContainer.setBorder(new EmptyBorder(10, 0, 10, 0)); // Centrar verticalmente
        
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(nota);
        bar.setPreferredSize(new Dimension(200, 10)); // Barra más fina y elegante
        bar.setBorderPainted(false);
        bar.setBackground(new Color(241, 245, 249)); // Fondo de barra muy claro
        
        //Color dinámico
        Color barColor;
        if (nota >= 90) barColor = GRADE_EXCELLENT;
        else if (nota >= 80) barColor = GRADE_GOOD;
        else barColor = GRADE_LOW;
        
        bar.setForeground(barColor);
        
        //Etiqueta pequeña sobre la barra (opcional, aquí solo pongo la barra limpia)
        progressContainer.add(bar, BorderLayout.CENTER);
        
        //Derecha (Nota Grande)
        JPanel gradePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        gradePanel.setOpaque(false);
        gradePanel.setPreferredSize(new Dimension(80, 50));
        
        JLabel lblNota = new JLabel(String.valueOf(nota));
        lblNota.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblNota.setForeground(barColor); // La nota tiene el color del estado
        
        gradePanel.add(lblNota);
        
        row.add(info, BorderLayout.WEST);
        row.add(progressContainer, BorderLayout.CENTER);
        row.add(gradePanel, BorderLayout.EAST);
        
        return row;
    }
    
    //RESUMEN SUPERIOR)
    
    private JPanel crearKpiCard(String titulo, String valor, String subtitulo, Color color, String icono) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        
        card.setBorder(new CompoundBorder(
            new MatteBorder(0, 5, 0, 0, color),
            new CompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(20, 25, 20, 25)
            )
        ));
        
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel lblIcon = new JLabel(icono);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        JLabel lblTitle = new JLabel(titulo.toUpperCase());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTitle.setForeground(TEXT_SECONDARY);
        
        top.add(lblTitle, BorderLayout.CENTER);
        top.add(lblIcon, BorderLayout.EAST);
        
        JLabel lblValue = new JLabel(valor);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblValue.setForeground(TEXT_PRIMARY);
        lblValue.setBorder(new EmptyBorder(10, 0, 5, 0));

        JLabel lblSub = new JLabel(subtitulo);
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSub.setForeground(color); // Color del tema
        
        card.add(top, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        card.add(lblSub, BorderLayout.SOUTH);
        
        return card;
    }
    
    
    private JPanel crearPanelLateral() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        // Card de Análisis
        JPanel cardAnalysis = new JPanel(new BorderLayout());
        cardAnalysis.setBackground(CARD_BG);
        cardAnalysis.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        JLabel lblTitle = new JLabel("Análisis Pedagógico");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(TEXT_PRIMARY);
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JEditorPane txtAnalysis = new JEditorPane();
        txtAnalysis.setContentType("text/html");
        txtAnalysis.setEditable(false);
        txtAnalysis.setOpaque(false);
        txtAnalysis.setText(
            "<html><body style='font-family: Segoe UI; color: #475569; font-size: 11px;'>" +
            "<p><b>FORTALEZAS:</b><br>" +
            "María demuestra un rendimiento excepcional en áreas lógicas y creativas. Su promedio de <b>95 en Matemáticas</b> indica una fuerte capacidad de resolución de problemas.</p>" +
            "<br>" +
            "<p><b>ÁREAS DE ENFOQUE:</b><br>" +
            "Aunque su rendimiento es sólido, se recomienda reforzar la lectura comprensiva en <b>Ciencias Sociales</b> para mejorar la retención de hechos históricos.</p>" +
            "<br>" +
            "<p><b>OBSERVACIÓN DOCENTE:</b><br>" +
            "Participa activamente en clase. Es colaborativa con sus compañeros. Mantiene una actitud positiva frente a nuevos retos.</p>" +
            "</body></html>"
        );
        
        cardAnalysis.add(lblTitle, BorderLayout.NORTH);
        cardAnalysis.add(txtAnalysis, BorderLayout.CENTER);
        
        // Botón Descargar PDF
        JButton btnDownload = new JButton("Descargar Boletín PDF");
        estilizarBoton(btnDownload, ACCENT_COLOR, Color.WHITE);
        btnDownload.addActionListener(e -> JOptionPane.showMessageDialog(this, "Descargando PDF..."));
        
        JPanel btnWrapper = new JPanel(new BorderLayout());
        btnWrapper.setOpaque(false);
        btnWrapper.setBorder(new EmptyBorder(20, 0, 0, 0));
        btnWrapper.add(btnDownload, BorderLayout.CENTER);
        cardAnalysis.add(btnWrapper, BorderLayout.SOUTH);
        
        panel.add(cardAnalysis);
        
        return panel;
    }
    
    // Helper para botones
    private void estilizarBoton(JButton btn, Color bg, Color fg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(ProgresoPadreApp::new);
    }
}