package com.interfazmaestro;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class DashboardProfesorApp extends JFrame {

    private String nombreProfesor = "Prof. Juan Pérez";
    private JLabel lblTotalEstudiantes, lblAsistenciaHoy, lblTareasRevisadas;

    // Colores del sistema (Mantenidos del original)
    private static final Color PRIMARY_COLOR = new Color(25, 25, 112);
    private static final Color SECONDARY_COLOR = new Color(70, 130, 180);
    private static final Color SUCCESS_COLOR = new Color(60, 179, 113);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color INFO_COLOR = new Color(147, 112, 219);
    private static final Color LIGHT_BG = new Color(248, 249, 250);

    // Bordes utilizados
    private static final Border CARD_BORDER = BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 235, 240), 1, true),
            new EmptyBorder(25, 25, 25, 25)
    );

    public DashboardProfesorApp() {
        setTitle("ClassConnect");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1400, 800));
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(LIGHT_BG);

        // Sidebar con contenido ÚTIL (Agenda y Pendientes)
        JPanel sidebarPanel = crearSidebar();
        mainPanel.add(sidebarPanel, BorderLayout.WEST);

        // Área de contenido principal
        JPanel contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(LIGHT_BG);

        JPanel headerPanel = crearHeader();
        contentArea.add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = crearContenido();
        contentArea.add(contentPanel, BorderLayout.CENTER);

        mainPanel.add(contentArea, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setVisible(true);
    }

    private JPanel crearSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(PRIMARY_COLOR);
        sidebar.setPreferredSize(new Dimension(300, 0));
        sidebar.setBorder(new EmptyBorder(30, 20, 30, 20));

        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBackground(PRIMARY_COLOR);
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblLogo = new JLabel("🎓");
        lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel("Panel Docente");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Portal de Profesores");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(Color.WHITE);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoPanel.add(lblLogo);
        logoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        logoPanel.add(lblTitulo);
        logoPanel.add(lblSubtitulo);

        sidebar.add(logoPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        sidebar.add(crearSeccionTitulo("CLASES DE HOY"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        
        sidebar.add(crearItemAgenda("08:00 - 09:30", "Matemáticas - 3ro A", "Aula 101", false)); // En curso
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(crearItemAgenda("10:00 - 11:30", "Física - 4to B", "Laboratorio", false));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(crearItemAgenda("14:00 - 15:30", "Matemáticas - 3ro C", "Aula 102", false));
        
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        sidebar.add(crearSeccionTitulo("PENDIENTES URGENTES"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        
        sidebar.add(crearItemPendiente("Subir notas parciales 3ro A"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(crearItemPendiente("Reunión de área (Viernes)"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(crearItemPendiente("Enviar recordatorio de proyecto"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(crearItemPendiente("Actualizar plan de clases 4to B"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));

        sidebar.add(Box.createVerticalGlue());

        // --- 4. Info del Profesor y Logout ---
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setBackground(new Color(0, 0, 0, 50)); // Fondo oscuro translúcido
        profilePanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        profilePanel.setMaximumSize(new Dimension(280, 120));

        JLabel lblNombreProf = new JLabel(nombreProfesor);
        lblNombreProf.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNombreProf.setForeground(Color.WHITE);
        lblNombreProf.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblRol = new JLabel("Profesor Titular");
        lblRol.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRol.setForeground(new Color(200, 210, 230));
        lblRol.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setBackground(new Color(220, 53, 69)); // Rojo suave
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.addActionListener(e -> cerrarSesion());

        profilePanel.add(lblNombreProf);
        profilePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        profilePanel.add(lblRol);
        profilePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        profilePanel.add(btnLogout);

        sidebar.add(profilePanel);

        return sidebar;
    }
    
    private JLabel crearSeccionTitulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(150, 170, 200));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    // Helper para items de agenda (clases)
    private JPanel crearItemAgenda(String hora, String materia, String aula, boolean activo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(260, 50));
        panel.setBorder(new EmptyBorder(8, 12, 8, 12));
        
        if (activo) {
            panel.setBackground(new Color(255, 255, 255, 40)); // Resaltado
            panel.setBorder(new CompoundBorder(
                new MatteBorder(0, 3, 0, 0, SUCCESS_COLOR), // Borde verde izquierdo
                new EmptyBorder(8, 9, 8, 12)
            ));
        } else {
            panel.setBackground(new Color(0, 0, 0, 30));
        }

        JLabel lblHora = new JLabel(hora);
        lblHora.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblHora.setForeground(activo ? Color.WHITE : new Color(200, 200, 200));
        
        JLabel lblMateria = new JLabel(materia);
        lblMateria.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMateria.setForeground(Color.WHITE);
        
        JLabel lblAula = new JLabel(aula);
        lblAula.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblAula.setForeground(new Color(220, 220, 220));
        
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.setOpaque(false);
        centerPanel.add(lblMateria);
        centerPanel.add(lblAula);
        
        panel.add(lblHora, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Helper para items de pendientes (checklist visual)
    private JPanel crearItemPendiente(String texto) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(260, 30));
        panel.setBorder(new EmptyBorder(8, 12, 8, 12));
        panel.setOpaque(false);
        
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(Color.WHITE);

        panel.add(lbl, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(25, 40, 25, 40));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Bienvenido, " + nombreProfesor);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(33, 33, 33));

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy");
        JLabel lblFecha = new JLabel(sdf.format(new Date()));
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblFecha.setForeground(new Color(117, 117, 117));

        JPanel titleGroup = new JPanel();
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));
        titleGroup.setBackground(Color.WHITE);
        titleGroup.add(lblTitulo);
        titleGroup.add(Box.createRigidArea(new Dimension(0, 5)));
        titleGroup.add(lblFecha);

        leftPanel.add(titleGroup);
        header.add(leftPanel, BorderLayout.WEST);

        return header;
    }

    private JPanel crearContenido() {
        JPanel content = new JPanel(new BorderLayout(0, 25));
        content.setBackground(LIGHT_BG);
        content.setBorder(new EmptyBorder(25, 40, 40, 40));

        JPanel statsPanel = crearEstadisticasRapidas();
        content.add(statsPanel, BorderLayout.NORTH);

        JPanel gridPanel = crearGridAcciones();
        content.add(gridPanel, BorderLayout.CENTER);

        return content;
    }

    private JPanel crearEstadisticasRapidas() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 20, 0));
        panel.setOpaque(false);

        // Tarjetas superiores
        panel.add(crearStatCard("👥", "Total Estudiantes", "42", "+5 este mes", SUCCESS_COLOR));
        panel.add(crearStatCard("✅", "Asistencia Hoy", "95%", "40/42 presentes", PRIMARY_COLOR)); 
        panel.add(crearStatCard("📝", "Por Calificar", "28", "Exámenes pendientes", WARNING_COLOR));
        panel.add(crearStatCard("⚠️", "Alertas", "3", "Requieren atención", DANGER_COLOR));

        return panel;
    }

    private JPanel crearStatCard(String icono, String titulo, String valor, String subtitulo, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(CARD_BORDER);

        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setBackground(Color.WHITE);

        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel colorIndicator = new JPanel();
        colorIndicator.setBackground(color);
        colorIndicator.setPreferredSize(new Dimension(6, 50));
        colorIndicator.setBorder(new EmptyBorder(6, 0, 6, 0));

        headerRow.add(lblIcono, BorderLayout.CENTER);
        headerRow.add(colorIndicator, BorderLayout.WEST);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitulo.setForeground(new Color(117, 117, 117));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblValor.setForeground(new Color(33, 33, 33));
        lblValor.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel(subtitulo);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSubtitulo.setForeground(new Color(150, 150, 150));
        lblSubtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(headerRow);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(lblTitulo);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(lblValor);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(lblSubtitulo);

        // Hover Effect
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { card.setBackground(new Color(250, 251, 252)); }
            public void mouseExited(MouseEvent e) { card.setBackground(Color.WHITE); }
        });

        return card;
    }

    private JPanel crearGridAcciones() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 20, 20));
        panel.setOpaque(false);

        // Botones de acción grandes
        panel.add(crearActionCard("👥", "Mis Estudiantes",
                "Listado, perfiles y datos de contacto",
                PRIMARY_COLOR, () -> abrirEstudiantes()));

        panel.add(crearActionCard("📅", "Registro de Asistencia",
                "Pasar lista y ver historial por grupo",
                SUCCESS_COLOR, () -> abrirAsistencia()));

        panel.add(crearActionCard("📚", "Libro de Calificaciones",
                "Ingresar notas, editar promedios",
                WARNING_COLOR, () -> abrirCalificaciones()));

        panel.add(crearActionCard("💬", "Gestión de Avisos",
                "Enviar comunicados a padres y alumnos",
                SECONDARY_COLOR, () -> abrirAvisos()));

        panel.add(crearActionCard("📄", "Reportes Académicos",
                "Generar boletines y estadísticas",
                PRIMARY_COLOR, () -> abrirReportes()));

        panel.add(crearActionCard("⚠️", "Centro de Alertas",
                "Seguimiento a casos especiales",
                DANGER_COLOR, () -> abrirAlertas()));

        return panel;
    }

    private JPanel crearActionCard(String icono, String titulo, String descripcion, Color color, Runnable accion) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        // Borde por defecto
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 235, 240), 1, true),
                new EmptyBorder(30, 25, 30, 25)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 45));
        lblIcono.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(33, 33, 33));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDesc = new JLabel("<html>" + descripcion + "</html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDesc.setForeground(new Color(117, 117, 117));
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(lblIcono);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(lblTitulo);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(lblDesc);
        card.add(Box.createVerticalGlue());
        card.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio para la barra de color al hover

        // Hover Effect con barra de color inferior
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(250, 251, 252));
                // Al hacer hover, pintamos el borde del color de la acción
                card.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(color, 2, true),
                        new EmptyBorder(30, 25, 30, 25)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(230, 235, 240), 1, true),
                        new EmptyBorder(30, 25, 30, 25)
                ));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                accion.run();
            }
        });

        return card;
    }

    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro que deseas cerrar sesión?",
                "Confirmar Cierre de Sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            new LoginApp();
            this.dispose();
        }
    }

    private void abrirEstudiantes() {
        new ListaEstudiantesApp();
        this.dispose();
    }

    private void abrirAsistencia() {
        new AsistenciaApp();
        this.dispose();
    }

    private void abrirCalificaciones() {
        new CalificacionesApp();
        this.dispose();
    }

    private void abrirAvisos() {
        new AvisosApp();
        this.dispose();
    }

    private void abrirReportes() {
        new ReportesApp();
        this.dispose();
    }

    private void abrirAlertas() {
        new AlertasApp();
        this.dispose();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new DashboardProfesorApp());
    }
}