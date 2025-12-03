package com.interfazpadre;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class AvisosPadreApp extends JFrame {

    private JTable tablaAvisos;
    private DefaultTableModel modeloTabla;
    private JTextArea txtRespuesta;
    private JLabel lblAvisosNoLeidos, lblAvisosTotales, lblUltimoAviso;
    private JButton btnEnviarRespuesta;
    private JPanel panelDetalleContenido;

    // Colores del sistema
    private static final Color PRIMARY_COLOR = new Color(25, 25, 112);
    private static final Color SECONDARY_COLOR = new Color(70, 130, 180);
    private static final Color SUCCESS_COLOR = new Color(60, 179, 113);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color INFO_COLOR = new Color(147, 112, 219);
    private static final Color LIGHT_BG = new Color(248, 249, 250);

    public AvisosPadreApp() {
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

        JPanel headerPanel = crearHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
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

        JLabel lblTitulo = new JLabel("Avisos del Profesor");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(PRIMARY_COLOR);

        // Panel de botones derecho
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        botonesPanel.setBackground(Color.WHITE);

        // Botón Marcar Todos como Leídos
        JButton btnMarcarLeidos = new JButton("Marcar Todos como Leídos");
        btnMarcarLeidos.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnMarcarLeidos.setBackground(INFO_COLOR);
        btnMarcarLeidos.setForeground(Color.WHITE);
        btnMarcarLeidos.setFocusPainted(false);
        btnMarcarLeidos.setBorderPainted(false);
        btnMarcarLeidos.setPreferredSize(new Dimension(220, 45));
        btnMarcarLeidos.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnMarcarLeidos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnMarcarLeidos.setBackground(INFO_COLOR.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnMarcarLeidos.setBackground(INFO_COLOR);
            }
        });

        btnMarcarLeidos.addActionListener(e -> marcarTodosLeidos());

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

        botonesPanel.add(btnMarcarLeidos);
        botonesPanel.add(btnVolver);

        header.add(lblTitulo, BorderLayout.WEST);
        header.add(botonesPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel crearContenido() {
        JPanel content = new JPanel(new BorderLayout(0, 25));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(30, 40, 40, 40));

        // Panel de estadísticas
        JPanel statsPanel = crearPanelEstadisticas();
        content.add(statsPanel, BorderLayout.NORTH);

        // Panel dividido (lista de avisos y vista detallada)
        JSplitPane splitPane = crearSplitPane();
        content.add(splitPane, BorderLayout.CENTER);

        return content;
    }

    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 25, 0));

        // Crear las cards de estadísticas
        Object[] totalData = crearStatCard("Total Avisos", "8", SECONDARY_COLOR, "Este mes");
        JPanel totalPanel = (JPanel) totalData[0];
        lblAvisosTotales = (JLabel) totalData[1];

        Object[] noLeidosData = crearStatCard("No Leídos", "2", DANGER_COLOR, "Por revisar");
        JPanel noLeidosPanel = (JPanel) noLeidosData[0];
        lblAvisosNoLeidos = (JLabel) noLeidosData[1];

        Object[] ultimoData = crearStatCard("Último Aviso", "Hoy", SUCCESS_COLOR, "15:30");
        JPanel ultimoPanel = (JPanel) ultimoData[0];
        lblUltimoAviso = (JLabel) ultimoData[1];

        panel.add(totalPanel);
        panel.add(noLeidosPanel);
        panel.add(ultimoPanel);

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

    private JSplitPane crearSplitPane() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500);
        splitPane.setDividerSize(5);
        splitPane.setBorder(null);

        JPanel panelLista = crearPanelListaAvisos();
        splitPane.setLeftComponent(panelLista);

        JPanel panelDetalle = crearPanelDetalle();
        splitPane.setRightComponent(panelDetalle);

        return splitPane;
    }

    private JPanel crearPanelListaAvisos() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 0, 25));

        // Título
        JLabel lblTitulo = new JLabel("Lista de Avisos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(PRIMARY_COLOR);
        lblTitulo.setBorder(new EmptyBorder(0, 0, 15, 0));

        panel.add(lblTitulo, BorderLayout.NORTH);

        // Tabla de avisos
        JPanel tablaPanel = crearTablaAvisos();
        panel.add(tablaPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearTablaAvisos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2, true));

        // Modelo de tabla
        // CORRECCIÓN: Se agregó la columna 0 vacía para el ícono
        String[] columnas = {"", "Fecha", "Asunto", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        cargarAvisosEjemplo();

        // Crear tabla
        tablaAvisos = new JTable(modeloTabla);
        tablaAvisos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaAvisos.setRowHeight(50);
        tablaAvisos.setSelectionBackground(new Color(230, 240, 255));
        tablaAvisos.setSelectionForeground(Color.BLACK);
        tablaAvisos.setShowVerticalLines(false);
        tablaAvisos.setIntercellSpacing(new Dimension(0, 1));
        tablaAvisos.setFillsViewportHeight(true);
        tablaAvisos.setGridColor(new Color(230, 230, 230));
        tablaAvisos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Listener para selección de filas
        tablaAvisos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalleAviso();
            }
        });

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
                JLabel label = new JLabel(value == null ? "" : value.toString());
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

        tablaAvisos.getColumnModel().getColumn(0).setCellRenderer(new EstadoLeidoRenderer());
        tablaAvisos.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tablaAvisos.getColumnModel().getColumn(3).setCellRenderer(new EstadoAvisoRenderer());

        // Anchos de columna
        tablaAvisos.getColumnModel().getColumn(0).setPreferredWidth(30);
        tablaAvisos.getColumnModel().getColumn(1).setPreferredWidth(100);
        tablaAvisos.getColumnModel().getColumn(2).setPreferredWidth(250);
        tablaAvisos.getColumnModel().getColumn(3).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(tablaAvisos);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }


    private void cargarAvisosEjemplo() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Avisos no leídos
        // CORRECCIÓN: Se agregan 4 elementos por fila
        modeloTabla.addRow(new Object[]{
            "", // Icono
            sdf.format(new Date()),
            "Recordatorio: Reunión de padres este viernes",
            "Nuevo"
        });

        modeloTabla.addRow(new Object[]{
            "",
            "18/11/2024",
            "Tarea de Ciencias - Proyecto sistema solar",
            "Nuevo"
        });

        // Avisos leídos
        modeloTabla.addRow(new Object[]{
            "", // Icono vacío o diferente para leído
            "15/11/2024",
            "Felicidades por el rendimiento en Matemáticas",
            "Leído"
        });

        modeloTabla.addRow(new Object[]{
            "",
            "10/11/2024",
            "Información sobre paseo escolar",
            "Leído"
        });

        modeloTabla.addRow(new Object[]{
            "",
            "05/11/2024",
            "Materiales para clase de arte",
            "Leído"
        });

        modeloTabla.addRow(new Object[]{
            "",
            "01/11/2024",
            "Inicio del segundo trimestre",
            "Leído"
        });

        modeloTabla.addRow(new Object[]{
            "",
            "28/10/2024",
            "Recordatorio pago de mensualidad",
            "Leído"
        });

        modeloTabla.addRow(new Object[]{
            "",
            "25/10/2024",
            "Cambio de horario para la próxima semana",
            "Leído"
        });

        actualizarEstadisticas();
    }

    private void mostrarDetalleAviso() {
        int selectedRow = tablaAvisos.getSelectedRow();
        if (selectedRow == -1) return;

        // Marcar como leído
        // CORRECCIÓN: Indices 0 y 3 son correctos ahora
        modeloTabla.setValueAt("Leído", selectedRow, 3);

        // Actualizar estadísticas
        actualizarEstadisticas();

        // Habilitar área de respuesta
        txtRespuesta.setEnabled(true);
        txtRespuesta.setText("");
        btnEnviarRespuesta.setEnabled(true);

        // Obtener datos del aviso seleccionado
        // CORRECCIÓN: Fecha es columna 1, Asunto es columna 2
        String fecha = modeloTabla.getValueAt(selectedRow, 1).toString();
        String asunto = modeloTabla.getValueAt(selectedRow, 2).toString();

        // Generar contenido del aviso según el asunto
        String contenido = generarContenidoAviso(asunto);

        // Actualizar panel de detalle
        panelDetalleContenido.removeAll();

        JPanel contenidoPanel = new JPanel();
        contenidoPanel.setLayout(new BoxLayout(contenidoPanel, BoxLayout.Y_AXIS));
        contenidoPanel.setBackground(Color.WHITE);
        contenidoPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Fecha y asunto
        JLabel lblFecha = new JLabel(fecha);
        lblFecha.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFecha.setForeground(new Color(100, 100, 100));
        lblFecha.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblAsunto = new JLabel(asunto);
        lblAsunto.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblAsunto.setForeground(new Color(25, 25, 112));
        lblAsunto.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(400, 2));
        separator.setForeground(new Color(220, 220, 220));
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Contenido del mensaje
        JTextArea txtContenido = new JTextArea(contenido);
        txtContenido.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtContenido.setLineWrap(true);
        txtContenido.setWrapStyleWord(true);
        txtContenido.setEditable(false);
        txtContenido.setBackground(Color.WHITE);
        txtContenido.setBorder(null);

        JScrollPane scrollContenido = new JScrollPane(txtContenido);
        scrollContenido.setBorder(null);
        scrollContenido.setAlignmentX(Component.LEFT_ALIGNMENT);

        contenidoPanel.add(lblFecha);
        contenidoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        contenidoPanel.add(lblAsunto);
        contenidoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contenidoPanel.add(separator);
        contenidoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contenidoPanel.add(scrollContenido);

        panelDetalleContenido.add(contenidoPanel, BorderLayout.CENTER);
        panelDetalleContenido.revalidate();
        panelDetalleContenido.repaint();
    }

    private String generarContenidoAviso(String asunto) {
        if (asunto.contains("Reunión de padres")) {
            return """
                   Estimados padres de familia,
                   
                   Les recuerdo que este viernes 22 de noviembre tendremos 
                   nuestra reunión trimestral de padres en el auditorio 
                   principal a las 4:00 PM.
                   
                   Temas a tratar:
                   • Rendimiento académico del segundo trimestre
                   • Proyectos escolares para el próximo período
                   • Información sobre el paseo de fin de año
                   • Preguntas y respuestas
                   
                   Esperamos contar con su valiosa presencia.
                   
                   Atentamente,
                   Prof. Juan Pérez
                   Director de 3ro Primaria""";
        } else if (asunto.contains("Tarea de Ciencias")) {
            return """
                   Queridos padres,
                   
                   Los estudiantes deben traer para el próximo lunes 
                   su proyecto sobre el sistema solar. El proyecto 
                   debe incluir:
                   
                   • Maqueta del sistema solar (8 planetas)
                   • Información básica de cada planeta
                   • Fuente de energía (sol) representada
                   
                   Favor revisar las instrucciones detalladas en el 
                   cuaderno de Ciencias.
                   
                   Cualquier duda, estoy a su disposición.
                   
                   Saludos,
                   Prof. Ana Martínez
                   Ciencias Naturales""";
        } else if (asunto.contains("Felicidades")) {
            return """
                   Estimada Sra. García,
                   
                   Me complace informarle que María ha tenido un 
                   excelente rendimiento en Matemáticas durante 
                   este trimestre, obteniendo el mejor promedio 
                   de la clase (92.0).
                   
                   Su dedicación y esfuerzo son ejemplares. 
                   ¡Felicidades!
                   
                   Seguiremos motivándola para mantener este 
                   excelente nivel.
                   
                   Cordialmente,
                   Prof. Carlos Rodríguez
                   Matemáticas""";
        } else {
            return """
                   Mensaje del profesor:
                   
                   Este es un aviso importante sobre el tema 
                   mencionado en el asunto. Por favor revise 
                   la información detallada y no dude en 
                   contactarnos si tiene alguna pregunta.
                   
                   Atentamente,
                   El equipo docente""";
        }
    }

    private void actualizarEstadisticas() {
        int total = modeloTabla.getRowCount();
        int noLeidos = 0;

        for (int i = 0; i < total; i++) {
            // CORRECCIÓN: indice 3 es correcto ahora
            String estado = modeloTabla.getValueAt(i, 3).toString();
            if (estado.equals("Nuevo")) {
                noLeidos++;
            }
        }

        lblAvisosTotales.setText(String.valueOf(total));
        lblAvisosNoLeidos.setText(String.valueOf(noLeidos));
    }

    private void marcarTodosLeidos() {
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            modeloTabla.setValueAt("📭", i, 0);
            modeloTabla.setValueAt("Leído", i, 3);
        }
        actualizarEstadisticas();

        JOptionPane.showMessageDialog(this,
            "Todos los avisos han sido marcados como leídos.",
            "Avisos Actualizados",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void enviarRespuesta() {
        if (txtRespuesta.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor escribe tu respuesta antes de enviar.",
                "Respuesta Vacía",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedRow = tablaAvisos.getSelectedRow();
        if (selectedRow == -1) return;

        // CORRECCIÓN: Asunto está en columna 2
        String asunto = modeloTabla.getValueAt(selectedRow, 2).toString();

        JOptionPane.showMessageDialog(this,
            "Respuesta enviada exitosamente\n\n" +
            "Asunto: " + asunto + "\n" +
            "Tu respuesta ha sido enviada al profesor y será\n" +
            "revisada en la próxima sesión.",
            "Respuesta Enviada",
            JOptionPane.INFORMATION_MESSAGE);

        txtRespuesta.setText("");
        btnEnviarRespuesta.setEnabled(false);
        txtRespuesta.setEnabled(false);
    }

    private JPanel crearPanelDetalle() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(new Color(250, 250, 250));
        panel.setBorder(new EmptyBorder(0, 15, 0, 15));

        // Cabecera detalle
        JLabel lblTituloDetalle = new JLabel("Detalle del Aviso");
        lblTituloDetalle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTituloDetalle.setForeground(PRIMARY_COLOR);
        lblTituloDetalle.setBorder(new EmptyBorder(10, 0, 10, 0));

        panel.add(lblTituloDetalle, BorderLayout.NORTH);

        // Panel donde se mostrará el contenido del aviso seleccionado
        panelDetalleContenido = new JPanel(new BorderLayout());
        panelDetalleContenido.setBackground(Color.WHITE);
        panelDetalleContenido.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true));

        // Mensaje inicial (cuando no hay selección)
        JLabel lblInicial = new JLabel("<html><div style='padding:15px;'>Seleccione un aviso en la lista para ver su contenido aquí.</div></html>");
        lblInicial.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelDetalleContenido.add(lblInicial, BorderLayout.CENTER);

        panel.add(panelDetalleContenido, BorderLayout.CENTER);

        // Area de respuesta y botón (parte inferior)
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        bottomPanel.setBackground(panel.getBackground());

        txtRespuesta = new JTextArea(4, 30);
        txtRespuesta.setLineWrap(true);
        txtRespuesta.setWrapStyleWord(true);
        txtRespuesta.setEnabled(false); // inicialmente deshabilitado hasta seleccionar un aviso
        JScrollPane respuestaScroll = new JScrollPane(txtRespuesta);
        respuestaScroll.setBorder(BorderFactory.createTitledBorder("Escribe tu respuesta"));

        btnEnviarRespuesta = new JButton("Enviar Respuesta");
        btnEnviarRespuesta.setEnabled(false);
        btnEnviarRespuesta.addActionListener(e -> enviarRespuesta());

        bottomPanel.add(respuestaScroll, BorderLayout.CENTER);
        bottomPanel.add(btnEnviarRespuesta, BorderLayout.EAST);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    public JLabel getLblUltimoAviso() {
        return lblUltimoAviso;
    }

    public void setLblUltimoAviso(JLabel lblUltimoAviso) {
        this.lblUltimoAviso = lblUltimoAviso;
    }

    // Renderer para estado leído/no leído
    class EstadoLeidoRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(JLabel.CENTER);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));

            if (value != null) {
                String estado = value.toString();
                if (estado.equals("")) {
                    setForeground(new Color(220, 53, 69)); // Rojo para no leído
                } else {
                    setForeground(new Color(100, 100, 100)); // Gris para leído
                }
            }

            return c;
        }
    }

    // Renderer para estado del aviso
    class EstadoAvisoRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(JLabel.CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 11));
            setOpaque(true);

            if (value != null) {
                String estado = value.toString();
                if (estado.equals("Nuevo")) {
                    setForeground(new Color(220, 53, 69)); // Rojo
                    setBackground(new Color(255, 240, 240)); // Fondo claro
                } else {
                    setForeground(new Color(60, 179, 113)); // Verde
                    setBackground(new Color(240, 255, 240)); // Fondo claro
                }
            } else {
                setBackground(Color.WHITE);
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

        SwingUtilities.invokeLater(() -> new AvisosPadreApp());
    }
}