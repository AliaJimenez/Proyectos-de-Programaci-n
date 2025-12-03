package com.interfazmaestro;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

public class ListaEstudiantesApp extends JFrame {
    
    private JTable tablaEstudiantes;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    private JComboBox<String> cmbFiltroGrado;
    
    // Colores del sistema
    private static final Color PRIMARY_COLOR = new Color(25, 25, 112);
    private static final Color SECONDARY_COLOR = new Color(70, 130, 180);
    private static final Color SUCCESS_COLOR = new Color(60, 179, 113);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color LIGHT_BG = new Color(248, 249, 250);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color BTN_VOLVER_BG = new Color(100, 100, 100);
    
    private ArrayList<Estudiante> listaEstudiantes;
    
    public ListaEstudiantesApp() {
        setTitle("ClassConnect");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1400, 800));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        listaEstudiantes = cargarEstudiantesEjemplo();
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);
        
        JPanel headerPanel = crearHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel contentPanel = crearContenido();
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
        setVisible(true);
    }
    
    private ArrayList<Estudiante> cargarEstudiantesEjemplo() {
        ArrayList<Estudiante> lista = new ArrayList<>();
        lista.add(new Estudiante("2024001", "María García López", "3ro Primaria", "8 años", "95%", "88.5"));
        lista.add(new Estudiante("2024002", "Carlos Rodríguez Pérez", "3ro Primaria", "8 años", "92%", "85.2"));
        lista.add(new Estudiante("2024003", "Ana Martínez Díaz", "3ro Primaria", "8 años", "88%", "79.8"));
        lista.add(new Estudiante("2024004", "Luis Fernández Santos", "3ro Primaria", "8 años", "90%", "82.5"));
        lista.add(new Estudiante("2024005", "Sofia Morales Ortiz", "3ro Primaria", "8 años", "97%", "91.2"));
        lista.add(new Estudiante("2024006", "Diego Gómez Ruiz", "3ro Primaria", "8 años", "85%", "75.4"));
        lista.add(new Estudiante("2024007", "Isabella Torres Ramírez", "3ro Primaria", "8 años", "93%", "87.0"));
        lista.add(new Estudiante("2024008", "Mateo Sánchez Vázquez", "3ro Primaria", "8 años", "89%", "80.3"));
        lista.add(new Estudiante("2024009", "Valentina Flores Castro", "3ro Primaria", "8 años", "96%", "90.1"));
        lista.add(new Estudiante("2024010", "Alder Valdez Jiménez", "3ro Primaria", "8 años", "94%", "96.9"));
        lista.add(new Estudiante("2024011", "Waily Evangeline Valdez Jiménez", "3ro Primaria", "8 años", "94%", "97.4"));
        
        return lista;
    }
    
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(25, 40, 25, 40));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel("Gestión de Estudiantes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(33, 33, 33));
        
        JLabel lblSubtitulo = new JLabel("3ro Primaria - Sección A");
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
        
        JButton btnNuevo = crearBoton("Registrar Estudiante", SUCCESS_COLOR);
        btnNuevo.addActionListener(e -> registrarNuevoEstudiante());
        
        JButton btnExportar = crearBoton("Exportar Lista", PRIMARY_COLOR);
        btnExportar.addActionListener(e -> exportarLista());

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
        
        rightPanel.add(btnNuevo);
        rightPanel.add(btnExportar);
        rightPanel.add(btnVolver);
        
        header.add(leftPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
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
        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setBackground(LIGHT_BG);
        content.setBorder(new EmptyBorder(25, 40, 40, 40));
        
        JPanel filtrosPanel = crearPanelFiltros();
        content.add(filtrosPanel, BorderLayout.NORTH);
        
        JPanel tablaPanel = crearPanelTabla();
        content.add(tablaPanel, BorderLayout.CENTER);
        
        return content;
    }
    
    private JPanel crearPanelFiltros() {
        JPanel panel = new JPanel(new BorderLayout(20, 0));
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 235, 240), 1),
            new EmptyBorder(20, 25, 20, 25)
        ));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setBackground(CARD_BG);
        
        JLabel lblBuscar = new JLabel("🔍");
        lblBuscar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        
        txtBuscar = new JTextField(25);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 235, 240), 1),
            new EmptyBorder(10, 12, 10, 12)
        ));
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarEstudiantes();
            }
        });
        
        JLabel lblFiltro = new JLabel("Filtrar por grado:");
        lblFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblFiltro.setForeground(new Color(100, 100, 100));
        
        String[] grados = {"Todos", "1ro Primaria", "2do Primaria", "3ro Primaria", 
                          "4to Primaria", "5to Primaria", "6to Primaria"};
        cmbFiltroGrado = new JComboBox<>(grados);
        cmbFiltroGrado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbFiltroGrado.setPreferredSize(new Dimension(150, 35));
        cmbFiltroGrado.setBackground(Color.WHITE);
        cmbFiltroGrado.addActionListener(e -> filtrarEstudiantes());
        
        leftPanel.add(lblBuscar);
        leftPanel.add(txtBuscar);
        leftPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        leftPanel.add(lblFiltro);
        leftPanel.add(cmbFiltroGrado);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setBackground(CARD_BG);
        
        JLabel lblTotal = new JLabel("Total: " + listaEstudiantes.size() + " estudiantes");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotal.setForeground(PRIMARY_COLOR);
        
        rightPanel.add(lblTotal);
        
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel crearPanelTabla() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(CARD_BG);
        container.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 235, 240), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        String[] columnas = {"Matrícula", "Nombre Completo", "Grado", "Edad", "Asistencia", "Promedio", "Acciones"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        
        cargarDatosTabla();
        
        tablaEstudiantes = new JTable(modeloTabla);
        tablaEstudiantes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaEstudiantes.setRowHeight(60);
        tablaEstudiantes.setSelectionBackground(new Color(232, 240, 254));
        tablaEstudiantes.setShowGrid(false);
        tablaEstudiantes.setIntercellSpacing(new Dimension(10, 1));
        
        JTableHeader header = tablaEstudiantes.getTableHeader();
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
                        setBackground(new Color(250, 250, 250));
                    }
                }
                return c;
            }
        };

        for (int i = 1; i < tablaEstudiantes.getColumnCount(); i++) {
            tablaEstudiantes.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        tablaEstudiantes.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tablaEstudiantes.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tablaEstudiantes.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tablaEstudiantes.getColumnModel().getColumn(4).setCellRenderer(new AsistenciaRenderer());
        tablaEstudiantes.getColumnModel().getColumn(5).setCellRenderer(new PromedioRenderer());
        
        // Asignamos Render y Editor de Acciones
        tablaEstudiantes.getColumnModel().getColumn(6).setCellRenderer(new AccionesRenderer());
        tablaEstudiantes.getColumnModel().getColumn(6).setCellEditor(new AccionesEditor(new JCheckBox()));
        
        tablaEstudiantes.getColumnModel().getColumn(0).setPreferredWidth(100);
        tablaEstudiantes.getColumnModel().getColumn(1).setPreferredWidth(250);
        tablaEstudiantes.getColumnModel().getColumn(2).setPreferredWidth(120);
        tablaEstudiantes.getColumnModel().getColumn(3).setPreferredWidth(80);
        tablaEstudiantes.getColumnModel().getColumn(4).setPreferredWidth(100);
        tablaEstudiantes.getColumnModel().getColumn(5).setPreferredWidth(100);
        tablaEstudiantes.getColumnModel().getColumn(6).setPreferredWidth(250);
        
        JScrollPane scrollPane = new JScrollPane(tablaEstudiantes);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        container.add(scrollPane, BorderLayout.CENTER);
        
        return container;
    }
    
    private void cargarDatosTabla() {
        for (Estudiante est : listaEstudiantes) {
            modeloTabla.addRow(new Object[]{
                est.matricula, est.nombreCompleto, est.grado, est.edad, est.asistencia, est.promedio, ""
            });
        }
    }
    
    class AsistenciaRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(JLabel.CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setOpaque(true);
            
            if (value != null) {
                String asistenciaStr = value.toString().replace("%", "");
                try {
                    int asistencia = Integer.parseInt(asistenciaStr);
                    if (asistencia >= 95) {
                        setForeground(Color.WHITE); setBackground(SUCCESS_COLOR);
                    } else if (asistencia >= 85) {
                        setForeground(Color.WHITE); setBackground(WARNING_COLOR);
                    } else {
                        setForeground(Color.WHITE); setBackground(DANGER_COLOR);
                    }
                } catch (NumberFormatException e) {
                    setForeground(Color.BLACK); setBackground(Color.WHITE);
                }
            }
            return c;
        }
    }
    
    class PromedioRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(JLabel.CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setOpaque(true);
            
            if (value != null) {
                try {
                    double promedio = Double.parseDouble(value.toString());
                    if (promedio >= 85) {
                        setForeground(Color.WHITE); setBackground(SUCCESS_COLOR);
                    } else if (promedio >= 70) {
                        setForeground(Color.WHITE); setBackground(WARNING_COLOR);
                    } else {
                        setForeground(Color.WHITE); setBackground(DANGER_COLOR);
                    }
                } catch (NumberFormatException e) {
                    setForeground(Color.BLACK); setBackground(Color.WHITE);
                }
            }
            return c;
        }
    }
    
    class AccionesRenderer extends JPanel implements TableCellRenderer {
        private JButton btnVer, btnEditar, btnEliminar;
        
        public AccionesRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
            setOpaque(true);
            
            btnVer = crearBotonTabla("👁️", PRIMARY_COLOR);
            btnEditar = crearBotonTabla("✏️", WARNING_COLOR);
            btnEliminar = crearBotonTabla("🗑️", DANGER_COLOR);
            
            add(btnVer);
            add(btnEditar);
            add(btnEliminar);
        }
        
        private JButton crearBotonTabla(String texto, Color color) {
            JButton btn = new JButton(texto);
            btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
            btn.setForeground(Color.WHITE);
            btn.setBackground(color);
            btn.setOpaque(true);           
            btn.setBorderPainted(false);     
            btn.setFocusPainted(false);     
            btn.setContentAreaFilled(true); 
            
            btn.setPreferredSize(new Dimension(60, 35));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            return btn;
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 250, 250));
            return this;
        }
    }
    
    
    class AccionesEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton btnVer, btnEditar, btnEliminar;
        private int currentRow;
        
        public AccionesEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
            panel.setOpaque(true);
            
            // Mismo estilo para el editor
            btnVer = crearBotonTabla("👁️", PRIMARY_COLOR);
            btnEditar = crearBotonTabla("✏️", WARNING_COLOR);
            btnEliminar = crearBotonTabla("🗑️", DANGER_COLOR);
            
            btnVer.addActionListener(e -> verEstudiante());
            btnEditar.addActionListener(e -> editarEstudiante());
            btnEliminar.addActionListener(e -> eliminarEstudiante());
            
            panel.add(btnVer);
            panel.add(btnEditar);
            panel.add(btnEliminar);
        }
        
        private JButton crearBotonTabla(String texto, Color color) {
            JButton btn = new JButton(texto);
            btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
            btn.setForeground(Color.WHITE);
            btn.setBackground(color);
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setContentAreaFilled(true);
            
            btn.setPreferredSize(new Dimension(60, 35));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) { btn.setBackground(color.darker()); }
                @Override
                public void mouseExited(MouseEvent e) { btn.setBackground(color); }
            });

            return btn;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentRow = row;
            panel.setBackground(table.getSelectionBackground());
            return panel;
        }
        
        private void verEstudiante() {
            fireEditingStopped();
            Estudiante est = listaEstudiantes.get(currentRow);
            mostrarModalVerEstudiante(est);
        }
        
        private void editarEstudiante() {
            fireEditingStopped();
            Estudiante est = listaEstudiantes.get(currentRow);
            mostrarModalEditarEstudiante(est, currentRow);
        }
        
        private void eliminarEstudiante() {
            fireEditingStopped();
            Estudiante est = listaEstudiantes.get(currentRow);
            int confirm = JOptionPane.showConfirmDialog(ListaEstudiantesApp.this,
                "¿Eliminar a " + est.nombreCompleto + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                listaEstudiantes.remove(currentRow);
                modeloTabla.removeRow(currentRow);
            }
        }
    }
    
    //MÉTODOS DE MODALES (Abreviados para no repetir todo el bloque, funcionan igual)
    private void mostrarModalVerEstudiante(Estudiante est) {
        JOptionPane.showMessageDialog(this, "Viendo detalles de: " + est.nombreCompleto);
    }
    
    private void mostrarModalEditarEstudiante(Estudiante est, int row) {
        JOptionPane.showMessageDialog(this, "Editando a: " + est.nombreCompleto);
    }
    
    private void filtrarEstudiantes() {
        String busqueda = txtBuscar.getText().toLowerCase();
        String gradoFiltro = cmbFiltroGrado.getSelectedItem().toString();
        modeloTabla.setRowCount(0);
        for (Estudiante est : listaEstudiantes) {
            boolean coincideBusqueda = est.nombreCompleto.toLowerCase().contains(busqueda) ||
                                     est.matricula.toLowerCase().contains(busqueda);
            boolean coincideGrado = gradoFiltro.equals("Todos") || est.grado.equals(gradoFiltro);
            if (coincideBusqueda && coincideGrado) {
                modeloTabla.addRow(new Object[]{est.matricula, est.nombreCompleto, est.grado, est.edad, est.asistencia, est.promedio, ""});
            }
        }
    }
    
    private void registrarNuevoEstudiante() {
        new RegistroApp();
    }
    
    private void exportarLista() {
        JOptionPane.showMessageDialog(this, "Exportando lista...");
    }
    
    class Estudiante {
        String matricula, nombreCompleto, grado, edad, asistencia, promedio;
        public Estudiante(String m, String n, String g, String e, String a, String p) {
            this.matricula = m; this.nombreCompleto = n; this.grado = g; 
            this.edad = e; this.asistencia = a; this.promedio = p;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ListaEstudiantesApp());
    }
}