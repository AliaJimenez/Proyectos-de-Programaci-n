package com.interfazmaestro;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class AlertasApp extends JFrame {
    
    private JPanel panelListaAlertas;
    private JLabel lblTotal, lblCriticas, lblResueltas;
    
    // Paleta de Colores
    private static final Color PRIMARY_COLOR = new Color(25, 25, 112);
    private static final Color BG_COLOR = new Color(245, 247, 250);
    private static final Color CARD_BG = Color.WHITE;
    
    // Colores de estado
    private static final Color COLOR_CRITICO = new Color(220, 53, 69);   // Rojo
    private static final Color COLOR_MEDIA = new Color(255, 193, 7);     // Amarillo/Naranja
    private static final Color COLOR_BAJA = new Color(60, 179, 113);     // Verde
    private static final Color COLOR_INFO = new Color(70, 130, 180);     // Azul
    private static final Color TEXT_PRIMARY = new Color(33, 37, 41);
    private static final Color TEXT_SECONDARY = new Color(108, 117, 125);

    public AlertasApp() {
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
        
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            new EmptyBorder(20, 40, 20, 40)
        ));
        
        // Títulos
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        JLabel lblTitulo = new JLabel("Centro de Alertas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(TEXT_PRIMARY);
        
        JLabel lblSub = new JLabel("Monitoreo de riesgo académico y conductual");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(TEXT_SECONDARY);
        
        titlePanel.add(lblTitulo);
        titlePanel.add(lblSub);
        
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
        
        // Botón Configuración
        JButton btnConfig = crearBotonSimple("Configurar Reglas", COLOR_INFO, Color.WHITE);
        btnConfig.addActionListener(e -> configurarReglas());
        
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);
        buttons.add(btnConfig);
        buttons.add(btnVolver);
        
        header.add(titlePanel, BorderLayout.WEST);
        header.add(buttons, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel crearCuerpo() {
        JPanel body = new JPanel(new BorderLayout(0, 30));
        body.setBackground(BG_COLOR);
        body.setBorder(new EmptyBorder(30, 40, 40, 40));
        
        //Panel de Estadísticas (KPIs)
        body.add(crearPanelKPIs(), BorderLayout.NORTH);
        
        //Lista de Alertas (Feed)
        JPanel feedContainer = new JPanel(new BorderLayout(0, 15));
        feedContainer.setOpaque(false);
        
        JPanel feedHeader = new JPanel(new BorderLayout());
        feedHeader.setOpaque(false);
        JLabel lblFeed = new JLabel("Alertas Activas");
        lblFeed.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblFeed.setForeground(TEXT_PRIMARY);
        
        // Botón Simulado de Filtro
        JComboBox<String> filtro = new JComboBox<>(new String[]{"Todas", "Alta Prioridad", "Asistencia", "Rendimiento"});
        filtro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        filtro.setPreferredSize(new Dimension(150, 30));
        
        feedHeader.add(lblFeed, BorderLayout.WEST);
        feedHeader.add(filtro, BorderLayout.EAST);
        
        feedContainer.add(feedHeader, BorderLayout.NORTH);
        
        // El panel que contiene las tarjetas verticalmente
        panelListaAlertas = new JPanel();
        panelListaAlertas.setLayout(new BoxLayout(panelListaAlertas, BoxLayout.Y_AXIS));
        panelListaAlertas.setOpaque(false);
        
        cargarAlertas();
        
        feedContainer.add(panelListaAlertas, BorderLayout.CENTER);
        
        body.add(feedContainer, BorderLayout.CENTER);
        
        return body;
    }
    
    private JPanel crearPanelKPIs() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 25, 0));
        panel.setOpaque(false);
        
        panel.add(crearTarjetaKPI("Alertas Críticas", "3", "Requieren acción inmediata", COLOR_CRITICO, "🔥"));
        panel.add(crearTarjetaKPI("Advertencias", "5", "Riesgo moderado", COLOR_MEDIA, "⚠️"));
        panel.add(crearTarjetaKPI("Resueltas (Mes)", "12", "Casos cerrados exitosamente", COLOR_BAJA, "✅"));
        
        return panel;
    }
    
    private JPanel crearTarjetaKPI(String titulo, String valor, String subtitulo, Color color, String icono) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        // Borde izquierdo grueso del color del estado
        card.setBorder(new CompoundBorder(
            new MatteBorder(0, 6, 0, 0, color),
            new CompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(15, 20, 15, 20)
            )
        ));
        
        // Top: Titulo e Icono
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel lTitle = new JLabel(titulo.toUpperCase());
        lTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lTitle.setForeground(TEXT_SECONDARY);
        
        JLabel lIcon = new JLabel(icono);
        lIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        top.add(lTitle, BorderLayout.WEST);
        top.add(lIcon, BorderLayout.EAST);
        
        // Center: Valor grande
        JLabel lValue = new JLabel(valor);
        lValue.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lValue.setForeground(TEXT_PRIMARY);
        
        // Bottom: Subtítulo
        JLabel lSub = new JLabel(subtitulo);
        lSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lSub.setForeground(color);
        
        card.add(top, BorderLayout.NORTH);
        card.add(lValue, BorderLayout.CENTER);
        card.add(lSub, BorderLayout.SOUTH);
        
        return card;
    }
    
    private void cargarAlertas() {
        panelListaAlertas.removeAll();
        
        // Alerta 1: Crítica - Asistencia
        panelListaAlertas.add(crearTarjetaAlerta(
            "Carlos Rodríguez", 
            "3ro Primaria A", 
            "ASISTENCIA CRÍTICA", 
            "El estudiante ha faltado 4 días consecutivos sin justificación. Se requiere contactar a los padres inmediatamente.",
            "Hoy, 08:30 AM",
            COLOR_CRITICO
        ));
        panelListaAlertas.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Alerta 2: Media - Rendimiento
        panelListaAlertas.add(crearTarjetaAlerta(
            "Ana Martínez", 
            "3ro Primaria A", 
            "BAJO RENDIMIENTO", 
            "Promedio de Matemáticas ha bajado al 65%. Se sugiere asignar tutoría de refuerzo.",
            "Ayer, 10:15 AM",
            COLOR_MEDIA
        ));
        panelListaAlertas.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Alerta 3: Media - Tareas
        panelListaAlertas.add(crearTarjetaAlerta(
            "Diego Morales", 
            "3ro Primaria A", 
            "TAREAS PENDIENTES", 
            "No ha entregado las últimas 3 asignaciones de Ciencias Naturales.",
            "18 Nov, 02:00 PM",
            COLOR_MEDIA
        ));

        panelListaAlertas.add(crearTarjetaAlerta(
            "Lucía Gómez", 
            "3ro Primaria A", 
            "COMPORTAMIENTO INADECUADO", 
            "Ha tenido 2 incidentes reportados en clase relacionados con interrupciones constantes.",
            "Hoy, 09:00 AM",
            COLOR_CRITICO
        ));
        
        panelListaAlertas.revalidate();
        panelListaAlertas.repaint();
    }
    
    private JPanel crearTarjetaAlerta(String nombre, String curso, String tipo, String descripcion, String fecha, Color colorSeveridad) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140)); // Altura fija cómoda
        
        // 1. Izquierda: Indicador visual y Avatar
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(60, 0));
        
        // Avatar circular simple con inicial
        JLabel avatar = new JLabel(String.valueOf(nombre.charAt(0)), SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(colorSeveridad.getRed(), colorSeveridad.getGreen(), colorSeveridad.getBlue(), 30));
                g2.fillOval(0, 0, getWidth(), getHeight());
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 24));
        avatar.setForeground(colorSeveridad);
        avatar.setPreferredSize(new Dimension(50, 50));
        
        leftPanel.add(avatar, BorderLayout.NORTH);
        
        // 2. Centro: Información
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        
        // Fila 1: Nombre y Badge de Tipo
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row1.setOpaque(false);
        
        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblNombre.setForeground(TEXT_PRIMARY);
        
        JLabel lblCurso = new JLabel("• " + curso);
        lblCurso.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblCurso.setForeground(TEXT_SECONDARY);
        
        JLabel badge = new JLabel(" " + tipo + " ");
        badge.setOpaque(true);
        badge.setBackground(colorSeveridad);
        badge.setForeground(Color.WHITE);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setBorder(new EmptyBorder(2, 5, 2, 5));
        
        row1.add(lblNombre);
        row1.add(lblCurso);
        row1.add(badge);
        
        // Fila 2: Descripción
        JTextArea txtDesc = new JTextArea(descripcion);
        txtDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDesc.setForeground(new Color(80, 80, 80));
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        txtDesc.setOpaque(false);
        txtDesc.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        centerPanel.add(row1);
        centerPanel.add(txtDesc);
        
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(200, 0));
        
        JLabel lblFecha = new JLabel(fecha, SwingConstants.RIGHT);
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFecha.setForeground(TEXT_SECONDARY);
        
        JPanel btnPanel = new JPanel(new GridLayout(2, 1, 0, 8));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JButton btnResolver = crearBotonAccion("Marcar Resuelta", COLOR_BAJA);
        btnResolver.addActionListener(e -> resolverAlerta(card));
        
        JButton btnNotificar = crearBotonAccion("Notificar Padres", COLOR_INFO);
        btnNotificar.addActionListener(e -> notificarPadres(nombre));
        
        btnPanel.add(btnResolver);
        btnPanel.add(btnNotificar);
        
        rightPanel.add(lblFecha, BorderLayout.NORTH);
        rightPanel.add(btnPanel, BorderLayout.SOUTH);
        
        card.add(leftPanel, BorderLayout.WEST);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);
        
        // Efecto Hover simple para la tarjeta
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { card.setBackground(new Color(250, 252, 255)); }
            public void mouseExited(MouseEvent e) { card.setBackground(Color.WHITE); }
        });
        
        return card;
    }
    
    //LÓGICA Y DIÁLOGOS
    
    private void resolverAlerta(JPanel card) {
        int opt = JOptionPane.showConfirmDialog(this, 
            "¿Estás seguro de marcar esta alerta como resuelta?\nDesaparecerá de la lista de pendientes.", 
            "Resolver Alerta", JOptionPane.YES_NO_OPTION);
            
        if (opt == JOptionPane.YES_OPTION) {
            panelListaAlertas.remove(card);
            // También removemos el espaciador (un poco sucio pero funcional para demo visual)
            panelListaAlertas.revalidate();
            panelListaAlertas.repaint();
            JOptionPane.showMessageDialog(this, "Alerta archivada correctamente.");
        }
    }
    
    private void notificarPadres(String nombre) {
        JDialog d = new JDialog(this, "Redactar Notificación", true);
        d.setSize(500, 400);
        d.setLocationRelativeTo(this);
        
        JPanel p = new JPanel(new BorderLayout(20, 20));
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("Mensaje para padres de: " + nombre);
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(TEXT_PRIMARY);
        
        JTextArea area = new JTextArea();
        area.setText("Estimados padres,\n\nLes informamos sobre la situación reciente...\n\nAtte. Prof. Pérez");
        area.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY),
            new EmptyBorder(10, 10, 10, 10)
        ));
        area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        JButton send = crearBotonSimple("Enviar Correo", COLOR_INFO, Color.WHITE);
        send.addActionListener(e -> {
            d.dispose();
            JOptionPane.showMessageDialog(this, "Correo enviado exitosamente.");
        });
        
        p.add(title, BorderLayout.NORTH);
        p.add(new JScrollPane(area), BorderLayout.CENTER);
        p.add(send, BorderLayout.SOUTH);
        
        d.add(p);
        d.setVisible(true);
    }
    
    private void configurarReglas() {
        JDialog dialog = new JDialog(this, "Configuración de Reglas", true);
        dialog.setSize(550, 600);
        dialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JLabel lblTitulo = new JLabel("Umbrales de Alerta Automática");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(TEXT_PRIMARY);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblDesc = new JLabel("Define cuándo el sistema debe generar una alerta.");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDesc.setForeground(TEXT_SECONDARY);
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(lblTitulo);
        mainPanel.add(lblDesc);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        JSpinner spinFaltas = createSpinner(3);
        JPanel cardAsistencia = crearCardRegla("Control de Asistencia", "Generar alerta crítica cuando el estudiante acumule:", spinFaltas, "faltas injustificadas al mes.", true);
        mainPanel.add(cardAsistencia);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JSpinner spinNota = createSpinner(70);
        JPanel cardNotas = crearCardRegla("Rendimiento Académico", "Generar alerta de riesgo si el promedio baja de:", spinNota, "puntos en cualquier materia.", true);
        mainPanel.add(cardNotas);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JSpinner spinTareas = createSpinner(4);
        JPanel cardTareas = crearCardRegla("Cumplimiento de Tareas", "Generar advertencia si no se entregan:", spinTareas, "tareas consecutivas.", false);
        mainPanel.add(cardTareas);
        
        // Empuja todo hacia arriba
        mainPanel.add(Box.createVerticalGlue());
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        btnPanel.setOpaque(false);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); 
        btnPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 

        JButton btnCancelar = crearBotonSimple("Cancelar", new Color(220, 220, 220), TEXT_PRIMARY);
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        JButton btnGuardar = crearBotonSimple("Guardar Cambios", PRIMARY_COLOR, Color.WHITE); 
        btnGuardar.addActionListener(e -> {
            JOptionPane.showMessageDialog(dialog, "Cambios guardados exitosamente.", "Guardado", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });
        
        btnPanel.add(btnCancelar);
        btnPanel.add(btnGuardar);
        
        mainPanel.add(btnPanel);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    //HELPERS PARA EL FORMULARIO DE REGLAS
    
    private JPanel crearCardRegla(String titulo, String descripcion, JSpinner spinner, String unidad, boolean activo) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            new EmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        // Switch de activado (Simulado con Checkbox bonito)
        JCheckBox toggle = new JCheckBox();
        toggle.setSelected(activo);
        toggle.setOpaque(false);
        toggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Panel Central (Textos)
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);
        
        JLabel lblTitle = new JLabel(titulo);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(TEXT_PRIMARY);
        
        JLabel lblDesc = new JLabel(descripcion);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(TEXT_SECONDARY);
        
        center.add(lblTitle);
        center.add(Box.createRigidArea(new Dimension(0, 5)));
        center.add(lblDesc);
        
        // Panel Derecho (Input)
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        right.setOpaque(false);
        
        JLabel lblUnit = new JLabel(unidad);
        lblUnit.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblUnit.setForeground(TEXT_SECONDARY);
        
        right.add(spinner);
        right.add(lblUnit);
        
        // Lógica visual del toggle
        toggle.addActionListener(e -> {
            boolean isSel = toggle.isSelected();
            spinner.setEnabled(isSel);
            lblTitle.setForeground(isSel ? TEXT_PRIMARY : Color.LIGHT_GRAY);
        });
        
        card.add(toggle, BorderLayout.WEST);
        card.add(center, BorderLayout.CENTER);
        card.add(right, BorderLayout.SOUTH); // O East si prefieres
        
        return card;
    }
    
    private JSpinner createSpinner(int valorInicial) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(valorInicial, 0, 100, 1));
        JComponent editor = spinner.getEditor();
        JFormattedTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
        tf.setColumns(3);
        tf.setHorizontalAlignment(SwingConstants.CENTER);
        tf.setFont(new Font("Segoe UI", Font.BOLD, 13));
        spinner.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        spinner.setPreferredSize(new Dimension(60, 30));
        return spinner;
    }
    
    // --- HELPERS PARA BOTONES ---
    
    private JButton crearBotonSimple(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setPreferredSize(new Dimension(150, 45));
        return btn;
    }
    
    private JButton crearBotonAccion(String texto, Color color) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(color.darker());
                } else {
                    g2.setColor(color);
                }
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 30));
        return btn;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(AlertasApp::new);
    }
}