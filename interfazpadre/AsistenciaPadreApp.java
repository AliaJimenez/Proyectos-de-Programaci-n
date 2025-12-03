package com.interfazpadre;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class AsistenciaPadreApp extends JFrame {
    
    private JTable tablaAsistencia;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> cmbMes;
    private JLabel lblAsistenciaTotal, lblFaltasTotal, lblTardanzasTotal;
    private JPanel panelCalendario;
    
    // Colores del sistema
    private static final Color PRIMARY_COLOR = new Color(25, 25, 112); // Midnight Blue
    private static final Color SECONDARY_COLOR = new Color(70, 130, 180);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113); // Un verde más moderno
    private static final Color DANGER_COLOR = new Color(231, 76, 60);   // Un rojo más suave
    private static final Color WARNING_COLOR = new Color(241, 196, 15); // Amarillo flat
    private static final Color LIGHT_BG = new Color(245, 247, 250);     // Fondo gris muy claro
    private static final Color TEXT_DARK = new Color(44, 62, 80);
    private static final Color TEXT_MUTED = new Color(127, 140, 141);
    
    // Datos simulados
    private int totalDias = 21;
    private int diasPresentes = 19;
    private int faltas = 1;
    private int tardanzas = 1;
    
    public AsistenciaPadreApp() {
        setTitle("ClassConnect");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1400, 850));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);
        
        JPanel headerPanel = crearHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Contenido con Scroll
        JScrollPane scrollContent = new JScrollPane(crearContenido());
        scrollContent.setBorder(null);
        scrollContent.getVerticalScrollBar().setUnitIncrement(20);
        scrollContent.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(scrollContent, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
        setVisible(true);
    }
    
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            new EmptyBorder(20, 40, 20, 40)
        ));
        
        // Panel Izquierdo
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        leftPanel.setOpaque(false);
        
        
        JPanel titleGroup = new JPanel();
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));
        titleGroup.setOpaque(false);
        
        JLabel lblTitulo = new JLabel("Registro de Asistencia");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(TEXT_DARK);
        
        JLabel lblSubtitulo = new JLabel("María García López • 3ro Primaria • Noviembre 2024");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(TEXT_MUTED);
        
        titleGroup.add(lblTitulo);
        titleGroup.add(Box.createRigidArea(new Dimension(0, 4)));
        titleGroup.add(lblSubtitulo);
        
        leftPanel.add(titleGroup);
        
        // Panel Derecho
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        
        JButton btnJustificar = new JButton("Justificar Falta");
        btnJustificar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnJustificar.setBackground(Color.ORANGE);
        btnJustificar.setForeground(Color.WHITE);
        btnJustificar.setFocusPainted(false);
        btnJustificar.setBorderPainted(false);
        btnJustificar.setPreferredSize(new Dimension(120, 45));
        btnJustificar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnJustificar.addActionListener(e -> justificarFalta());

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
        
        rightPanel.add(btnJustificar);
        rightPanel.add(btnVolver);
        
        header.add(leftPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        
        return header;
    }
    

    private JPanel crearContenido() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(LIGHT_BG);
        content.setBorder(new EmptyBorder(30, 40, 50, 40));
        
        JPanel statsPanel = crearEstadisticas();
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(statsPanel);
        
        content.add(Box.createRigidArea(new Dimension(0, 30)));
        
        JPanel middleSection = new JPanel(new BorderLayout(20, 0));
        middleSection.setOpaque(false);
        middleSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        middleSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 450));
        
        JPanel selectorPanel = crearSelectorMes();
        JPanel calendarioContainer = crearCalendarioVisual();
        
        // Organizamos verticalmente: selector arriba y luego el calendario ocupa todo
        JPanel calendarWrapper = new JPanel(new BorderLayout());
        calendarWrapper.setOpaque(false);
        calendarWrapper.add(selectorPanel, BorderLayout.NORTH);
        calendarWrapper.add(Box.createRigidArea(new Dimension(0, 15)), BorderLayout.CENTER); // Espacio
        calendarWrapper.add(calendarioContainer, BorderLayout.SOUTH);
        
        content.add(calendarWrapper);
        
        content.add(Box.createRigidArea(new Dimension(0, 30)));
        
        //Tabla Detallada
        JPanel tablaPanel = crearTablaDetalles();
        tablaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(tablaPanel);
        
        return content;
    }
    
    private JPanel crearEstadisticas() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 25, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        
        Object[] statTotalDias = crearCardStat("Total Días", String.valueOf(totalDias), "Lectivos", new Color(63, 81, 181)); // Indigo
        panel.add((Component)statTotalDias[0]);
        
        Object[] statAsistencia = crearCardStat("Asistencia", String.format("%.0f%%", (diasPresentes * 100.0 / totalDias)), "Excelente", SUCCESS_COLOR);
        panel.add((Component)statAsistencia[0]);
        lblAsistenciaTotal = (JLabel) statAsistencia[1];
        
        Object[] statFaltas = crearCardStat("Faltas", String.valueOf(faltas), "1 Justificada", DANGER_COLOR);
        panel.add((Component)statFaltas[0]);
        lblFaltasTotal = (JLabel) statFaltas[1];
        
        Object[] statTardanzas = crearCardStat("Tardanzas", String.valueOf(tardanzas), "Casi impecable", WARNING_COLOR);
        panel.add((Component)statTardanzas[0]);
        lblTardanzasTotal = (JLabel) statTardanzas[1];
        
        return panel;
    }
    
    private Object[] crearCardStat(String titulo, String valor, String subtitulo, Color colorBorde) {
        CardPanel card = new CardPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel barraColor = new JPanel();
        barraColor.setBackground(colorBorde);
        barraColor.setPreferredSize(new Dimension(4, 0));

        // Poner la barra dentro de un panel con padding para que no toque los bordes redondeados feamente
        JPanel barraContainer = new JPanel(new BorderLayout());
        barraContainer.setOpaque(false);
        barraContainer.setBorder(new EmptyBorder(5, 0, 5, 15));
        barraContainer.add(barraColor, BorderLayout.WEST);

        JPanel content = new JPanel(new GridLayout(3, 1));
        content.setOpaque(false);
        
        JLabel lblTitulo = new JLabel(titulo.toUpperCase());
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTitulo.setForeground(TEXT_MUTED);
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblValor.setForeground(TEXT_DARK);
        
        JLabel lblSub = new JLabel(subtitulo);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(colorBorde);
        
        content.add(lblTitulo);
        content.add(lblValor);
        content.add(lblSub);
        
        card.add(barraContainer, BorderLayout.WEST);
        card.add(content, BorderLayout.CENTER);
        
        return new Object[]{card, lblValor};
    }
    
    private JPanel crearSelectorMes() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);
        
        JLabel lbl = new JLabel("Visualización Mensual:  ");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(TEXT_DARK);
        
        String[] meses = {"Noviembre 2024", "Octubre 2024", "Septiembre 2024"};
        cmbMes = new JComboBox<>(meses);
        cmbMes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbMes.setPreferredSize(new Dimension(200, 35));
        cmbMes.setBackground(Color.WHITE);
        cmbMes.addActionListener(e -> actualizarAsistencia());
        
        panel.add(lbl);
        panel.add(cmbMes);
        return panel;
    }
    
    private JPanel crearCalendarioVisual() {
        CardPanel container = new CardPanel();
        container.setLayout(new BorderLayout());
        container.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel lblTitulo = new JLabel("Calendario");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(Color.BLACK);
        
        topRow.add(lblTitulo, BorderLayout.WEST);
        topRow.add(crearLeyenda(), BorderLayout.EAST);
        container.add(topRow, BorderLayout.NORTH);

        JPanel calendarZone = new JPanel(new BorderLayout());
        calendarZone.setOpaque(false);

        // Usamos GridLayout(1, 7) para que coincida perfectamente con las columnas de los dias
        JPanel headersPanel = new JPanel(new GridLayout(1, 7, 12, 0)); // 12 es el gap horizontal
        headersPanel.setOpaque(false);
        headersPanel.setBorder(new EmptyBorder(0, 0, 10, 0)); 
        
        String[] diasSemana = {"LUN", "MAR", "MIÉ", "JUE", "VIE", "SÁB", "DOM"};
        for (String dia : diasSemana) {
            JLabel lblDia = new JLabel(dia, JLabel.CENTER);
            lblDia.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lblDia.setForeground(Color.BLACK);
            headersPanel.add(lblDia);
        }
        
        //La cuadrícula de días
        //IMPORTANTE: GridLayout(0, 7) -> 0 filas (auto), 7 columnas FIJAS
        panelCalendario = new JPanel(new GridLayout(0, 7, 12, 12)); 
        panelCalendario.setOpaque(false);
        
        // Lógica de llenado (Noviembre 2024 empieza en Viernes)
        Calendar cal = Calendar.getInstance();
        cal.set(2024, Calendar.NOVEMBER, 1);
        
        int primerDiaSemana = cal.get(Calendar.DAY_OF_WEEK); // Domingo=1 ... Viernes=6
        // Ajuste: Queremos Lunes(0) a Domingo(6).
        // Calendar da: DOM(1), LUN(2), MAR(3), MIE(4), JUE(5), VIE(6), SAB(7)
        // Si es VIE(6) -> L, M, X, J vacíos (4 espacios). 6 - 2 = 4. Correcto.
        int espaciosVacios = (primerDiaSemana == Calendar.SUNDAY) ? 6 : primerDiaSemana - 2;
        
        // Espacios vacíos
        for (int i = 0; i < espaciosVacios; i++) {
            panelCalendario.add(new JLabel(""));
        }
        
        // Días del mes
        for (int dia = 1; dia <= 30; dia++) {
            // ... (Tu misma lógica de estados, la copio tal cual para que funcione) ...
            String estado = "OK";
            Color colorEstado = SUCCESS_COLOR;
            Color colorTexto = Color.WHITE; // Por defecto texto blanco en pastillas de color

            if (dia == 15) { 
                estado = "FALTA"; 
                colorEstado = DANGER_COLOR; 
            } else if (dia == 8) { 
                estado = "TARDE"; 
                colorEstado = WARNING_COLOR; 
                colorTexto = TEXT_DARK; // En amarillo el texto blanco se ve mal
            } else if (dia > 27) { 
                estado = "FUTURO"; 
                colorEstado = null; 
            }
            
            // Fin de semana
            cal.set(Calendar.DAY_OF_MONTH, dia);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                 panelCalendario.add(crearDiaCalendario(dia, "FDS", null, null));
            } else {
                 panelCalendario.add(crearDiaCalendario(dia, estado, colorEstado, colorTexto));
            }
        }
        
        int totalCeldas = espaciosVacios + 30;
        int celdasFaltantes = 7 - (totalCeldas % 7);
        if (celdasFaltantes < 7) {
            for (int i = 0; i < celdasFaltantes; i++) {
                panelCalendario.add(new JLabel(""));
            }
        }

        // Armamos la zona del calendario
        calendarZone.add(headersPanel, BorderLayout.NORTH);
        calendarZone.add(panelCalendario, BorderLayout.CENTER);

        container.add(calendarZone, BorderLayout.CENTER);
        return container;
    }
    
    private JPanel crearDiaCalendario(int dia, String tipo, Color colorBg, Color colorTexto) {
        JPanel cell = new JPanel(new BorderLayout());
        
        // Diseño visual de la celda
        if (tipo.equals("FDS") || tipo.equals("FUTURO")) {
            cell.setBackground(new Color(250, 250, 250));
            cell.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240)));
        } else {
            //Fondo suave del color del estado
            Color softBg = new Color(colorBg.getRed(), colorBg.getGreen(), colorBg.getBlue(), 30); // 30 alpha
            cell.setBackground(softBg);
            cell.setBorder(BorderFactory.createLineBorder(colorBg, 1));
            cell.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        
        // Número del día
        JLabel lblNum = new JLabel(String.valueOf(dia));
        lblNum.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNum.setBorder(new EmptyBorder(5, 8, 0, 0));
        
        if (tipo.equals("FDS")) lblNum.setForeground(new Color(200, 200, 200));
        else lblNum.setForeground(TEXT_DARK);
        
        // Icono estado
        JLabel lblIcon = new JLabel();
        lblIcon.setHorizontalAlignment(SwingConstants.RIGHT);
        lblIcon.setBorder(new EmptyBorder(0, 0, 5, 5));
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        
        if (tipo.equals("FALTA")) {
            lblIcon.setText("❌");
        } else if (tipo.equals("TARDE")) {
            lblIcon.setText("⏰");
        } else if (tipo.equals("OK")) {
            lblIcon.setText("✅");
        }
        
        cell.add(lblNum, BorderLayout.NORTH);
        cell.add(lblIcon, BorderLayout.SOUTH);
        cell.setPreferredSize(new Dimension(50, 60)); // Tamaño fijo celda
        
        return cell;
    }
    
    private JPanel crearLeyenda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panel.setOpaque(false);
        panel.add(crearItemLeyenda(SUCCESS_COLOR, "Asistencia"));
        panel.add(crearItemLeyenda(DANGER_COLOR, "Falta"));
        panel.add(crearItemLeyenda(WARNING_COLOR, "Tardanza"));
        return panel;
    }
    
    private JPanel crearItemLeyenda(Color c, String texto) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        item.setOpaque(false);
        
        JPanel bolita = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c);
                g2.fillOval(0, 2, 10, 10);
            }
        };
        bolita.setPreferredSize(new Dimension(12, 14));
        bolita.setOpaque(false);
        
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(TEXT_MUTED);
        
        item.add(bolita);
        item.add(lbl);
        return item;
    }
    
    private JPanel crearTablaDetalles() {
        CardPanel container = new CardPanel();
        container.setLayout(new BorderLayout());
        container.setBorder(new EmptyBorder(0, 0, 10, 0)); // Sin border interno para que el header toque el techo
        
        // Header de la sección de tabla
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 25, 15, 25));
        
        JLabel lblTitulo = new JLabel("Historial Detallado");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(TEXT_DARK);
        headerPanel.add(lblTitulo, BorderLayout.WEST);
        
        container.add(headerPanel, BorderLayout.NORTH);
        
        // Modelo y Tabla
        String[] columnas = {"FECHA", "DÍA", "ESTADO", "HORA", "OBSERVACIONES"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        
        cargarDatosTabla();
        
        tablaAsistencia = new JTable(modeloTabla);
        tablaAsistencia.setRowHeight(45);
        tablaAsistencia.setShowVerticalLines(false);
        tablaAsistencia.setShowHorizontalLines(true);
        tablaAsistencia.setGridColor(new Color(240, 240, 240));
        tablaAsistencia.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaAsistencia.setSelectionBackground(new Color(232, 240, 254)); // Azul muy suave selección
        tablaAsistencia.setSelectionForeground(TEXT_DARK);
        tablaAsistencia.setIntercellSpacing(new Dimension(0, 0));
        
        // Estilo del Header de la Tabla
        JTableHeader header = tablaAsistencia.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setBackground(PRIMARY_COLOR);
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
                lbl.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                    new EmptyBorder(12, 10, 12, 10)
                ));
                lbl.setHorizontalAlignment(column == 0 ? JLabel.LEFT : JLabel.CENTER); // Alinear fecha izquierda, resto centro
                if(column == 4) lbl.setHorizontalAlignment(JLabel.LEFT); // Obs a la izquierda
                return lbl;
            }
        });
        
        // Renderers Celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        tablaAsistencia.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tablaAsistencia.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        
        // Renderer especial para Estado (Badge)
        tablaAsistencia.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString());
                label.setOpaque(true);
                label.setFont(new Font("Segoe UI", Font.BOLD, 12));
                label.setHorizontalAlignment(JLabel.CENTER);
                
                String estado = value.toString();
                if(estado.equals("Presente")) {
                    label.setBackground(new Color(230, 250, 235)); // Verde muy claro
                    label.setForeground(new Color(39, 174, 96));   // Verde texto
                } else if (estado.equals("Tardanza")) {
                    label.setBackground(new Color(254, 249, 231));
                    label.setForeground(new Color(212, 172, 13));
                } else {
                    label.setBackground(new Color(253, 237, 236));
                    label.setForeground(new Color(231, 76, 60));
                }
                
                if (isSelected) {
                    label.setBackground(table.getSelectionBackground());
                    label.setForeground(table.getSelectionForeground());
                }
                return label;
            }
        });
        
        // Padding para celdas normales
        DefaultTableCellRenderer paddingRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                 Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                 ((JComponent)c).setBorder(new EmptyBorder(0, 15, 0, 0));
                 return c;
            }
        };
        tablaAsistencia.getColumnModel().getColumn(0).setCellRenderer(paddingRenderer);
        tablaAsistencia.getColumnModel().getColumn(4).setCellRenderer(paddingRenderer);
        
        // Anchos
        tablaAsistencia.getColumnModel().getColumn(0).setPreferredWidth(120);
        tablaAsistencia.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablaAsistencia.getColumnModel().getColumn(4).setPreferredWidth(300);

        JScrollPane scrollTable = new JScrollPane(tablaAsistencia);
        scrollTable.setBorder(BorderFactory.createEmptyBorder()); // Quitar borde feo del scrollpane
        scrollTable.getViewport().setBackground(Color.WHITE);
        
        container.add(scrollTable, BorderLayout.CENTER);
        
        return container;
    }

    private void cargarDatosTabla() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.set(2024, Calendar.NOVEMBER, 1);
        
        for (int i = 1; i <= 30; i++) {
            cal.set(Calendar.DAY_OF_MONTH, i);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            
            if (dayOfWeek >= Calendar.MONDAY && dayOfWeek <= Calendar.FRIDAY) {
                String fecha = sdf.format(cal.getTime());
                String dia = obtenerDiaSemana(dayOfWeek);
                String estado, hora, observaciones;
                
                if (i == 15) {
                    estado = "Ausente"; hora = "--:--"; observaciones = "Justificado: Cita médica";
                } else if (i == 8) {
                    estado = "Tardanza"; hora = "08:15"; observaciones = "Tráfico pesado";
                } else if (i > 27) {
                    // Futuro, no agregar a tabla aun
                    continue; 
                } else {
                    estado = "Presente"; 
                    hora = String.format("07:%02d", (int)(Math.random() * 20) + 40);
                    observaciones = "";
                }
                modeloTabla.addRow(new Object[]{fecha, dia, estado, hora, observaciones});
            }
        }
    }
    
    // --- LÓGICA (Mantenida igual) ---
    private String obtenerDiaSemana(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.MONDAY: return "Lunes";
            case Calendar.TUESDAY: return "Martes";
            case Calendar.WEDNESDAY: return "Miércoles";
            case Calendar.THURSDAY: return "Jueves";
            case Calendar.FRIDAY: return "Viernes";
            default: return "";
        }
    }
    
    private void actualizarAsistencia() {
        JOptionPane.showMessageDialog(this, "Actualizando datos para: " + cmbMes.getSelectedItem());
    }
    
    private void justificarFalta() {
        int filaSinJustificar = -1;
        String fechaFalta = "";

        // Buscar la primera fila que tenga estado "Ausente" o "Falta"
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String estado = modeloTabla.getValueAt(i, 2).toString(); // Columna 2 es Estado
            String obs = modeloTabla.getValueAt(i, 4).toString();    // Columna 4 es Observaciones
            
            if ((estado.equalsIgnoreCase("Ausente") || estado.equals("FALTA")) && obs.isEmpty()) {
                filaSinJustificar = i;
                fechaFalta = modeloTabla.getValueAt(i, 0).toString();
                break;
            }
        }

        if (filaSinJustificar == -1) {
            JOptionPane.showMessageDialog(this,
                "¡Buenas noticias!\nNo tienes faltas pendientes de justificación.",
                "Todo en orden",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        mostrarDialogoFormulario(filaSinJustificar, fechaFalta);
    }
    
    private void mostrarDialogoFormulario(int fila, String fechaFalta) {
        JDialog dialogo = new JDialog(this, "Justificar Falta", true);
        dialogo.setSize(500, 300);
        dialogo.setLocationRelativeTo(this);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        
        JLabel lblFecha = new JLabel("Fecha de la falta: " + fechaFalta);
        lblFecha.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JLabel lblMotivo = new JLabel("Motivo de la justificación:");
        lblMotivo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JTextArea txtMotivo = new JTextArea(5, 30);
        txtMotivo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtMotivo.setLineWrap(true);
        txtMotivo.setWrapStyleWord(true);
        JScrollPane scrollMotivo = new JScrollPane(txtMotivo);
        
        contenido.add(lblFecha);
        contenido.add(Box.createRigidArea(new Dimension(0, 15)));
        contenido.add(lblMotivo);
        contenido.add(Box.createRigidArea(new Dimension(0, 10)));
        contenido.add(scrollMotivo);
        
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton btnJustificar = new JButton("Justificar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnJustificar.addActionListener(e -> {
            String motivo = txtMotivo.getText().trim();
            if (motivo.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Por favor ingresa un motivo.", "Campo vacío", JOptionPane.WARNING_MESSAGE);
            } else {
                modeloTabla.setValueAt(motivo, fila, 4);
                JOptionPane.showMessageDialog(dialogo, "Falta justificada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dialogo.dispose();
            }
        });
        
        btnCancelar.addActionListener(e -> dialogo.dispose());
        
        botones.add(btnJustificar);
        botones.add(btnCancelar);
        
        panel.add(contenido, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);
        
        dialogo.setContentPane(panel);
        dialogo.setVisible(true);
    }
    
    class CardPanel extends JPanel {
        public CardPanel() {
            setBackground(Color.WHITE);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int shadowSize = 3;
            int arc = 15; // Radio de borde redondeado
            
            // Dibujar Sombra
            g2.setColor(new Color(220, 220, 220));
            g2.fillRoundRect(shadowSize, shadowSize, getWidth() - shadowSize - 1, getHeight() - shadowSize - 1, arc, arc);
            
            // Dibujar Fondo Blanco
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - shadowSize - 1, getHeight() - shadowSize - 1, arc, arc);
            
            // Borde sutil
            g2.setColor(new Color(240, 240, 240));
            g2.drawRoundRect(0, 0, getWidth() - shadowSize - 1, getHeight() - shadowSize - 1, arc, arc);
            
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // Truco para mejorar renderizado de texto en Windows
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
        } catch (Exception e) {}
        
        SwingUtilities.invokeLater(AsistenciaPadreApp::new);
    }
}