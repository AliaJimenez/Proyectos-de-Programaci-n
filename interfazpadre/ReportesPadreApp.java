package com.interfazpadre;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

public class ReportesPadreApp extends JFrame {
    
    private JTable tablaReportesRecibidos, tablaReportesSolicitados;
    private DefaultTableModel modeloRecibidos, modeloSolicitados;
    private JLabel lblTotalRecibidos, lblPendientesSolicitud, lblDescargados;
    
    private final String nombreEstudiante = "María García López";
    
    // Colores del sistema
    private static final Color PRIMARY_COLOR = new Color(25, 25, 112);
    private static final Color SECONDARY_COLOR = new Color(70, 130, 180);
    private static final Color SUCCESS_COLOR = new Color(60, 179, 113);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color INFO_COLOR = new Color(147, 112, 219);
    private static final Color LIGHT_BG = new Color(248, 249, 250);
    private static final Color CARD_BG = Color.WHITE;
    
    public ReportesPadreApp() {
        setTitle("ClassConnect");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1400, 800));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);
        
        JPanel headerPanel = crearHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JScrollPane scrollContent = new JScrollPane(crearContenido());
        scrollContent.setBorder(null);
        scrollContent.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollContent, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
        setVisible(true);
    }
    
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(25, 40, 25, 40));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel("Reportes Académicos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(33, 33, 33));
        
        JLabel lblSubtitulo = new JLabel(nombreEstudiante);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(117, 117, 117));
        
        JPanel titleGroup = new JPanel();
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));
        titleGroup.setBackground(Color.WHITE);
        titleGroup.add(lblTitulo);
        titleGroup.add(Box.createRigidArea(new Dimension(0, 5)));
        titleGroup.add(lblSubtitulo);
        
        leftPanel.add(titleGroup);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(Color.WHITE);
        
        JButton btnSolicitar = crearBotonModerno("Solicitar Reporte", PRIMARY_COLOR);
        btnSolicitar.setFocusPainted(false);
        btnSolicitar.setBorderPainted(false);
        btnSolicitar.addActionListener(e -> solicitarNuevoReporte());

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
        

        rightPanel.add(btnSolicitar);
        rightPanel.add(btnVolver);
        
        header.add(leftPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JButton crearBotonModerno(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(color); }
        });
        
        return btn;
    }
    
    private JPanel crearContenido() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(LIGHT_BG);
        content.setBorder(new EmptyBorder(25, 40, 40, 40));
        
        JPanel statsPanel = crearEstadisticas();
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        content.add(statsPanel);
        
        content.add(Box.createRigidArea(new Dimension(0, 25)));
        
        JPanel recibidosPanel = crearPanelReportesRecibidos();
        recibidosPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 380));
        content.add(recibidosPanel);
        
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel solicitadosPanel = crearPanelReportesSolicitados();
        solicitadosPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 380));
        content.add(solicitadosPanel);
        
        return content;
    }
    
    private JPanel crearEstadisticas() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setOpaque(false);
        
        Object[] stat1 = crearStatCard("📨", "Reportes Recibidos", "5", "Del profesor", PRIMARY_COLOR);
        Object[] stat2 = crearStatCard("⏳", "Solicitudes Pendientes", "2", "En proceso", WARNING_COLOR);
        Object[] stat3 = crearStatCard("📥", "Descargados", "3", "Guardados localmente", SUCCESS_COLOR);
        
        panel.add((JPanel) stat1[0]);
        panel.add((JPanel) stat2[0]);
        panel.add((JPanel) stat3[0]);
        
        lblTotalRecibidos = (JLabel) stat1[1];
        lblPendientesSolicitud = (JLabel) stat2[1];
        lblDescargados = (JLabel) stat3[1];
        
        return panel;
    }
    
    private Object[] crearStatCard(String icono, String titulo, String valor, String subtitulo, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 235, 240), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setBackground(CARD_BG);
        
        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        
        JPanel colorIndicator = new JPanel();
        colorIndicator.setBackground(color);
        colorIndicator.setPreferredSize(new Dimension(4, 50));
        
        headerRow.add(lblIcono, BorderLayout.WEST);
        headerRow.add(colorIndicator, BorderLayout.EAST);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitulo.setForeground(new Color(117, 117, 117));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 36));
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
        
        return new Object[]{card, lblValor};
    }
    
    private JPanel crearPanelReportesRecibidos() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(CARD_BG);
        container.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 235, 240), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_BG);
        
        JLabel lblTitulo = new JLabel("Reportes Recibidos del Profesor");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(33, 33, 33));
        
        JLabel lblInfo = new JLabel("Reportes enviados por el profesor sobre el desempeño de su hijo/a");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(new Color(117, 117, 117));
        
        JPanel titleGroup = new JPanel();
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));
        titleGroup.setBackground(CARD_BG);
        titleGroup.add(lblTitulo);
        titleGroup.add(Box.createRigidArea(new Dimension(0, 5)));
        titleGroup.add(lblInfo);
        
        headerPanel.add(titleGroup, BorderLayout.WEST);
        container.add(headerPanel, BorderLayout.NORTH);
        
        String[] columnas = {"Fecha Recibido", "Tipo de Reporte", "Período", "Estado", "Acciones"};
        modeloRecibidos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Solo la columna de acciones es editable (para los botones)
            }
        };
        
        cargarReportesRecibidos();
        
        tablaReportesRecibidos = new JTable(modeloRecibidos);
        tablaReportesRecibidos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaReportesRecibidos.setRowHeight(55);
        tablaReportesRecibidos.setSelectionBackground(new Color(237, 231, 246));
        tablaReportesRecibidos.setShowGrid(false);
        tablaReportesRecibidos.setIntercellSpacing(new Dimension(10, 1));
        
        JTableHeader header = tablaReportesRecibidos.getTableHeader();
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
        for (int i = 0; i < tablaReportesRecibidos.getColumnCount(); i++) {
            if (i != 0 && i != 7) {
                tablaReportesRecibidos.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        
        tablaReportesRecibidos.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tablaReportesRecibidos.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tablaReportesRecibidos.getColumnModel().getColumn(3).setCellRenderer(new EstadoRenderer());
        
        // Asignar Renderer y Editor para la columna de botones
        tablaReportesRecibidos.getColumnModel().getColumn(4).setCellRenderer(new AccionesRecibidosRenderer());
        tablaReportesRecibidos.getColumnModel().getColumn(4).setCellEditor(new AccionesRecibidosEditor(new JCheckBox()));
        
        tablaReportesRecibidos.getColumnModel().getColumn(0).setPreferredWidth(130);
        tablaReportesRecibidos.getColumnModel().getColumn(1).setPreferredWidth(300);
        tablaReportesRecibidos.getColumnModel().getColumn(4).setPreferredWidth(250);
        
        JScrollPane scrollPane = new JScrollPane(tablaReportesRecibidos);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(CARD_BG);
        tableWrapper.setBorder(new EmptyBorder(15, 0, 0, 0));
        tableWrapper.add(scrollPane, BorderLayout.CENTER);
        
        container.add(tableWrapper, BorderLayout.CENTER);
        return container;
    }
    
    private void cargarReportesRecibidos() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        modeloRecibidos.addRow(new Object[]{sdf.format(new Date()), "Reporte Académico Completo", "Segundo Trimestre", "Nuevo", ""});
        modeloRecibidos.addRow(new Object[]{"15/11/2024", "Reporte de Progreso Mensual", "Noviembre 2024", "Leído", ""});
        modeloRecibidos.addRow(new Object[]{"01/11/2024", "Reporte de Asistencia", "Octubre 2024", "Descargado", ""});
    }
    
    private JPanel crearPanelReportesSolicitados() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(CARD_BG);
        container.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 235, 240), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_BG);
        
        JLabel lblTitulo = new JLabel("📋 Mis Solicitudes de Reportes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(33, 33, 33));
        
        headerPanel.add(lblTitulo, BorderLayout.WEST);
        container.add(headerPanel, BorderLayout.NORTH);
        
        String[] columnas = {"Fecha Solicitud", "Tipo Solicitado", "Período", "Estado", "Acciones"};
        modeloSolicitados = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        
        modeloSolicitados.addRow(new Object[]{"18/11/2024", "Reporte Detallado", "Noviembre 2024", "En Proceso", ""});
        modeloSolicitados.addRow(new Object[]{"10/11/2024", "Informe de Comportamiento", "Último Mes", "Pendiente", ""});
        
        tablaReportesSolicitados = new JTable(modeloSolicitados);
        tablaReportesSolicitados.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaReportesSolicitados.setRowHeight(55);
        tablaReportesSolicitados.setShowGrid(false);
        tablaReportesSolicitados.setIntercellSpacing(new Dimension(10, 1));
        
        JTableHeader header = tablaReportesSolicitados.getTableHeader();
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
        for (int i = 0; i < tablaReportesSolicitados.getColumnCount(); i++) {
            if (i != 0 && i != 7) {
                tablaReportesSolicitados.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        
        tablaReportesSolicitados.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tablaReportesSolicitados.getColumnModel().getColumn(3).setCellRenderer(new EstadoSolicitudRenderer());
        
        // Renderer y Editor para botones de cancelación
        tablaReportesSolicitados.getColumnModel().getColumn(4).setCellRenderer(new AccionesSolicitadosRenderer());
        tablaReportesSolicitados.getColumnModel().getColumn(4).setCellEditor(new AccionesSolicitadosEditor(new JCheckBox()));
        
        JScrollPane scrollPane = new JScrollPane(tablaReportesSolicitados);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(CARD_BG);
        tableWrapper.setBorder(new EmptyBorder(15, 0, 0, 0));
        tableWrapper.add(scrollPane, BorderLayout.CENTER);
        
        container.add(tableWrapper, BorderLayout.CENTER);
        return container;
    }
    
    class EstadoRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setHorizontalAlignment(JLabel.CENTER);
            c.setFont(new Font("Segoe UI", Font.BOLD, 12));
            c.setOpaque(true);
            
            if (value != null) {
                String estado = value.toString();
                if (estado.equals("Nuevo")) {
                    c.setForeground(Color.WHITE); c.setBackground(INFO_COLOR);
                } else if (estado.equals("Leído")) {
                    c.setForeground(Color.WHITE); c.setBackground(SECONDARY_COLOR);
                } else if (estado.equals("Descargado")) {
                    c.setForeground(Color.WHITE); c.setBackground(SUCCESS_COLOR);
                }
            }
            return c;
        }
    }
    
    class EstadoSolicitudRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setHorizontalAlignment(JLabel.CENTER);
            c.setFont(new Font("Segoe UI", Font.BOLD, 12));
            c.setOpaque(true);
            
            if (value != null) {
                String estado = value.toString();
                if (estado.equals("En Proceso")) {
                    c.setForeground(Color.WHITE); c.setBackground(WARNING_COLOR);
                } else if (estado.equals("Pendiente")) {
                    c.setForeground(Color.WHITE); c.setBackground(SECONDARY_COLOR);
                }
            }
            return c;
        }
    }
    
    // Panel con botones para renderizar en la tabla
    class AccionesRecibidosRenderer extends JPanel implements TableCellRenderer {
        public AccionesRecibidosRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
            setOpaque(true);
            add(crearBotonChico("Ver", PRIMARY_COLOR));
            add(crearBotonChico("Descargar", SUCCESS_COLOR));
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            return this;
        }
    }
    
    // Editor para manejar clics en botones de la tabla de recibidos
    class AccionesRecibidosEditor extends DefaultCellEditor {
        private JPanel panel;
        private int currentRow;
        
        public AccionesRecibidosEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
            
            JButton btnVer = crearBotonChico("Ver", PRIMARY_COLOR);
            btnVer.addActionListener(e -> verReporte());
            
            JButton btnDescargar = crearBotonChico("Descargar", SUCCESS_COLOR);
            btnDescargar.addActionListener(e -> descargarReporte());
            
            panel.add(btnVer);
            panel.add(btnDescargar);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentRow = row;
            panel.setBackground(table.getSelectionBackground());
            return panel;
        }
        
        private void verReporte() {
            String tipo = modeloRecibidos.getValueAt(currentRow, 1).toString();
            mostrarModalVerReporte(tipo);
            if (modeloRecibidos.getValueAt(currentRow, 3).equals("Nuevo")) {
                modeloRecibidos.setValueAt("Leído", currentRow, 3);
            }
            fireEditingStopped();
        }
        
        private void descargarReporte() {
            JOptionPane.showMessageDialog(null, "Reporte descargado correctamente.");
            modeloRecibidos.setValueAt("Descargado", currentRow, 3);
            fireEditingStopped();
        }
    }
    
    class AccionesSolicitadosRenderer extends JPanel implements TableCellRenderer {
        public AccionesSolicitadosRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
            setOpaque(true);
            add(crearBotonChico("Cancelar", DANGER_COLOR));
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            return this;
        }
    }
    
    class AccionesSolicitadosEditor extends DefaultCellEditor {
        private JPanel panel;
        private int currentRow;
        
        public AccionesSolicitadosEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
            JButton btnCancelar = crearBotonChico("Cancelar", DANGER_COLOR);
            btnCancelar.addActionListener(e -> {
                if (JOptionPane.showConfirmDialog(null, "¿Cancelar solicitud?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    modeloSolicitados.removeRow(currentRow);
                }
                fireEditingStopped();
            });
            panel.add(btnCancelar);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentRow = row;
            panel.setBackground(table.getSelectionBackground());
            return panel;
        }
    }
    
    private JButton crearBotonChico(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setFocusPainted(false);
        return btn;
    }
    
    private void mostrarModalVerReporte(String titulo) {
        JDialog d = new JDialog(this, titulo, true);
        d.setSize(600, 500);
        d.setLocationRelativeTo(this);
        JTextArea ta = new JTextArea("Contenido del reporte...\n\n(Aquí iría el detalle del documento)");
        ta.setBorder(new EmptyBorder(20, 20, 20, 20));
        ta.setEditable(false);
        d.add(new JScrollPane(ta));
        d.setVisible(true);
    }
    
    private void solicitarNuevoReporte() {
        JOptionPane.showMessageDialog(this, "Formulario de solicitud enviado.");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        modeloSolicitados.insertRow(0, new Object[]{sdf.format(new Date()), "Nuevo Reporte Solicitado", "Actual", "Pendiente", ""});
    }
    
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(ReportesPadreApp::new);
    }
}