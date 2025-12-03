package com.interfazpadre;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class PerfilEstudianteApp extends JFrame {
    
    private final String nombreEstudiante = "María García López";
    
    // Paleta de Colores Refinada
    private static final Color PRIMARY = new Color(93, 50, 165); // Azul Profundo
    private static final Color PRIMARY_LIGHT = new Color(42, 82, 152); // Azul más claro (gradiente)
    private static final Color ACCENT = new Color(255, 255, 255); // Blanco
    private static final Color BG_COLOR = new Color(240, 242, 245); // Gris muy tenue de fondo
    private static final Color TEXT_DARK = new Color(50, 50, 50);
    private static final Color TEXT_MUTED = new Color(130, 130, 130);
    private static final Color SUCCESS = new Color(39, 174, 96);
    
    public PerfilEstudianteApp() {
        setTitle("ClassConnect");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 800));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Panel Principal con Layout Personalizado para el Banner
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null); 
        layeredPane.setBackground(BG_COLOR);
        layeredPane.setOpaque(true);
        
        // 1. Banner Superior (Fondo Azul)
        JPanel banner = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY, getWidth(), 0, PRIMARY_LIGHT);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        banner.setBounds(0, 0, 3000, 180); // Ancho grande para cubrir maximized
        
        JButton btnVolver = crearBotonTransparente("← Volver");
        btnVolver.setBounds(30, 30, 180, 40);
        btnVolver.addActionListener(e -> {
            new DashboardPadreApp();
            this.dispose();
        });
        layeredPane.add(btnVolver, JLayeredPane.PALETTE_LAYER);
        
        // 2. Tarjeta de Perfil (Flotante)
        JPanel profileCard = crearTarjetaPerfil();
        profileCard.setBounds(50, 100, 350, 550); // Posición fija a la izquierda
        
        // 3. Panel de Contenido con Pestañas (Derecha)
        JTabbedPane tabbedPane = crearTabsContenido();
        tabbedPane.setBounds(430, 190, 800, 550); // A la derecha de la tarjeta
        
        // Header de Acciones (Botones Editar/Imprimir)
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionsPanel.setOpaque(false);
        actionsPanel.setBounds(430, 130, 800, 50);
        
        // Añadir todo al LayeredPane
        layeredPane.add(banner, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(profileCard, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(actionsPanel, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(tabbedPane, JLayeredPane.PALETTE_LAYER);
        
        // Listener para redimensionar componentes responsivamente
        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = getWidth();
                banner.setSize(w, 180);
                // El tabbed pane se estira
                if (w > 850) {
                    tabbedPane.setSize(w - 480, 550);
                    actionsPanel.setSize(w - 480, 50);
                }
            }
        });

        setContentPane(layeredPane);
        setVisible(true);
    }
    
    // --- COMPONENTES VISUALES ---
    
    private JPanel crearTarjetaPerfil() {
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(new Color(230, 230, 230), 1, true));
        
        // Sombra simulada (truco visual simple)
        JPanel shadow = new JPanel();
        shadow.setBackground(new Color(200, 200, 200, 50));
        shadow.setBounds(53, 103, 350, 550);
        
        // Avatar Circular con Iniciales (MG)
        JLabel avatar = new JLabel("MG", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Círculo de fondo
                g2.setColor(new Color(235, 240, 255));
                g2.fillOval(0, 0, getWidth(), getHeight());
                
                // Borde Blanco Grueso (para separarlo del banner si se superpone)
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(5));
                g2.drawOval(2, 2, getWidth()-5, getHeight()-5);
                
                // Texto
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 48));
        avatar.setForeground(PRIMARY);
        avatar.setBounds(100, 30, 150, 150);
        
        // Datos Principales
        JLabel lblNombre = new JLabel(nombreEstudiante, SwingConstants.CENTER);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblNombre.setForeground(TEXT_DARK);
        lblNombre.setBounds(20, 190, 310, 30);
        
        JLabel lblGrado = new JLabel("3ro Primaria • Sección A", SwingConstants.CENTER);
        lblGrado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblGrado.setForeground(TEXT_MUTED);
        lblGrado.setBounds(20, 220, 310, 20);
        
        // Badge Estado
        JLabel badge = new JLabel("ESTUDIANTE ACTIVO", SwingConstants.CENTER);
        badge.setOpaque(true);
        badge.setBackground(new Color(220, 255, 220));
        badge.setForeground(new Color(30, 120, 30));
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setBorder(new EmptyBorder(5, 10, 5, 10));
        // Centrar badge manual
        badge.setBounds(100, 255, 150, 25);
        
        JSeparator sep = new JSeparator();
        sep.setBounds(30, 300, 290, 2);
        
        // Lista de datos rápidos
        JPanel infoList = new JPanel(new GridLayout(4, 1, 0, 15));
        infoList.setOpaque(false);
        infoList.setBounds(30, 320, 290, 200);
        
        infoList.add(crearItemInfoLateral("Edad", "8 años"));
        infoList.add(crearItemInfoLateral("Matrícula", "2024-0058"));
        infoList.add(crearItemInfoLateral("Nacionalidad", "Dominicana"));
        infoList.add(crearItemInfoLateral("Ingreso", "Agosto 2021"));
        
        card.add(avatar);
        card.add(lblNombre);
        card.add(lblGrado);
        card.add(badge);
        card.add(sep);
        card.add(infoList);
        
        return card;
    }
    
    private JPanel crearItemInfoLateral(String titulo, String valor) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel l1 = new JLabel(titulo);
        l1.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        l1.setForeground(TEXT_MUTED);
        
        JLabel l2 = new JLabel(valor);
        l2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l2.setForeground(TEXT_DARK);
        
        p.add(l1, BorderLayout.WEST);
        p.add(l2, BorderLayout.EAST);
        return p;
    }
    
    //SISTEMA DE PESTAÑAS (TABS)
    
    private JTabbedPane crearTabsContenido() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabs.setBackground(Color.WHITE);
        
        tabs.addTab("Información General", crearTabGeneral());
        tabs.addTab("Médico", crearTabMedico());
        tabs.addTab("Historial Académico", crearTabHistorial());
        
        return tabs;
    }
    
    private JPanel crearTabGeneral() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        p.add(crearTituloSeccion("Información de Contacto"));
        p.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JPanel grid = new JPanel(new GridLayout(2, 2, 20, 20));
        grid.setOpaque(false);
        grid.add(crearCampoDato("Dirección Residencial", "Calle Los Prados #45, Edif. A, Apto 2B"));
        grid.add(crearCampoDato("Teléfono Casa", "(809) 555-0101"));
        grid.add(crearCampoDato("Email Institucional", "maria.garcia24@escuela.edu.do"));
        grid.add(crearCampoDato("Contacto Emergencia", "Juan García (Padre) - 809-555-9999"));
        grid.setMaximumSize(new Dimension(2000, 150));
        
        p.add(grid);
        p.add(Box.createRigidArea(new Dimension(0, 30)));
        
        p.add(crearTituloSeccion("Información Familiar"));
        p.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JPanel gridFam = new JPanel(new GridLayout(2, 2, 20, 20));
        gridFam.setOpaque(false);
        gridFam.add(crearCampoDato("Padre / Tutor", "Jose García Pérez"));
        gridFam.add(crearCampoDato("Ocupación Padre", "Ingeniero Civil"));
        gridFam.add(crearCampoDato("Madre", "Ana López"));
        gridFam.add(crearCampoDato("Ocupación Madre", "Arquitecta"));
        gridFam.setMaximumSize(new Dimension(2000, 150));
        
        p.add(gridFam);
        
        return p;
    }
    
    private JPanel crearTabMedico() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        p.add(crearTituloSeccion("Ficha Médica"));
        p.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Panel de alertas médicas
        JPanel alerta = new JPanel(new BorderLayout());
        alerta.setBackground(new Color(255, 245, 245));
        alerta.setBorder(new LineBorder(new Color(255, 200, 200), 1, true));
        alerta.setMaximumSize(new Dimension(2000, 60));
        
        JLabel lblAlerta = new JLabel("ALERTA: Estudiante alérgica a la Penicilina e indica asma leve.", SwingConstants.CENTER);
        lblAlerta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblAlerta.setForeground(new Color(200, 50, 50));
        alerta.add(lblAlerta, BorderLayout.CENTER);
        
        p.add(alerta);
        p.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel grid = new JPanel(new GridLayout(2, 2, 20, 20));
        grid.setOpaque(false);
        grid.add(crearCampoDato("Tipo de Sangre", "O-"));
        grid.add(crearCampoDato("Seguro Médico", "Humano - Plan Royal"));
        grid.add(crearCampoDato("Pediatra", "Dr. Roberto Santos"));
        grid.add(crearCampoDato("Teléfono Doctor", "809-555-2222"));
        grid.setMaximumSize(new Dimension(2000, 150));
        
        p.add(grid);
        
        return p;
    }
    
    private JPanel crearTabHistorial() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        String[] cols = {"Periodo", "Grado", "Promedio", "Asistencia", "Condición Final"};
        Object[][] rows = {
            {"2023-2024", "2do Primaria", "92", "98%", "Promovido"},
            {"2022-2023", "1ro Primaria", "89", "95%", "Promovido"},
            {"2021-2022", "Pre-Primario", "A", "100%", "Promovido"}
        };
        
        DefaultTableModel model = new DefaultTableModel(rows, cols);
        JTable table = new JTable(model) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(240, 240, 240));
        
        // Renderers
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(center);
        table.getColumnModel().getColumn(1).setCellRenderer(center);
        table.getColumnModel().getColumn(2).setCellRenderer(center);
        table.getColumnModel().getColumn(3).setCellRenderer(center);
        
        // Renderer para "Promovido" (Badge verde)
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel l = new JLabel(value.toString(), JLabel.CENTER);
                l.setFont(new Font("Segoe UI", Font.BOLD, 12));
                l.setForeground(new Color(30, 120, 30));
                return l;
            }
        });
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(248, 249, 250));
        header.setForeground(TEXT_DARK);
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(new Color(230, 230, 230)));
        
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }
    
    // --- HELPERS UI ---
    
    private JPanel crearCampoDato(String label, String valor) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setOpaque(false);
        
        JLabel l1 = new JLabel(label.toUpperCase());
        l1.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l1.setForeground(new Color(150, 150, 150));
        
        // Valor en un campo estilo "input" pero solo lectura
        JLabel l2 = new JLabel(valor);
        l2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        l2.setForeground(TEXT_DARK);
        l2.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            new EmptyBorder(5, 0, 5, 0)
        ));
        
        p.add(l1, BorderLayout.NORTH);
        p.add(l2, BorderLayout.CENTER);
        return p;
    }
    
    private JLabel crearTituloSeccion(String titulo) {
        JLabel l = new JLabel(titulo);
        l.setFont(new Font("Segoe UI", Font.BOLD, 18));
        l.setForeground(PRIMARY);
        return l;
    }
    
    private JButton crearBotonTransparente(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setForeground(new Color(200, 200, 255)); }
            public void mouseExited(MouseEvent e) { btn.setForeground(Color.WHITE); }
        });
        return btn;
    }
    
    private JButton crearBotonAccion(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bg.darker() : bg);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 40));
        
        if (bg.equals(Color.WHITE)) {
            btn.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
            btn.setBorderPainted(true);
        }
        
        return btn;
    }
    
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(PerfilEstudianteApp::new);
    }
}