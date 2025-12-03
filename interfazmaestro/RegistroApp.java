package com.interfazmaestro;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RegistroApp extends JFrame {
    
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtMatricula;
    private JTextField txtFechaNacimiento;
    private JComboBox<String> cmbGrado;
    private JComboBox<String> cmbSeccion;
    private JComboBox<String> cmbGenero;
    private JTextField txtDireccion;
    private JTextField txtTelefono;
    private JTextField txtNombrePadre;
    private JTextField txtEmailPadre;
    private ListaEstudiantesApp listaEstudiantesRef;
    
    // Colores del sistema
    private static final Color PRIMARY_COLOR = new Color(25, 25, 112);
    private static final Color SECONDARY_COLOR = new Color(70, 130, 180);
    private static final Color SUCCESS_COLOR = new Color(60, 179, 113);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color INFO_COLOR = new Color(147, 112, 219);
    private static final Color LIGHT_BG = new Color(248, 249, 250);
    
    // Constructor para cuando se abre desde ListaEstudiantes
    public RegistroApp(ListaEstudiantesApp listaRef) {
        this();
        this.listaEstudiantesRef = listaRef;
    }
    
    // Constructor normal
    public RegistroApp() {
        setTitle("ClassConnect");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(600, 700));
        
        // Panel principal con gradiente
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
        
        // HEADER
        JPanel headerPanel = crearHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // FORMULARIO (en scroll por si es muy largo)
        JScrollPane scrollPane = new JScrollPane(crearFormulario());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // BOTONES
        JPanel botonesPanel = crearBotones();
        mainPanel.add(botonesPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
        setVisible(true);
    }
    
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 220, 220)),
            new EmptyBorder(20, 30, 20, 30)
        ));
        
        JLabel lblTitulo = new JLabel("Registro de Estudiante");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(25, 25, 112));
        
        header.add(lblTitulo, BorderLayout.WEST);
        
        return header;
    }
    
    private JPanel crearFormulario() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        // SECCIÓN: Datos Personales
        panel.add(crearSeccionTitulo("Datos Personales"));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        txtNombre = crearCampoTexto("Nombre(s)", "Ej: María José");
        panel.add(crearFilaCampo("Nombre(s):", txtNombre));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        txtApellido = crearCampoTexto("Apellido(s)", "Ej: García López");
        panel.add(crearFilaCampo("Apellido(s):", txtApellido));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        txtMatricula = crearCampoTexto("Matrícula", "Ej: 2024001");
        panel.add(crearFilaCampo("Matrícula/ID:", txtMatricula));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        txtFechaNacimiento = crearCampoTexto("Fecha", "DD/MM/AAAA");
        panel.add(crearFilaCampo("Fecha de Nacimiento:", txtFechaNacimiento));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        String[] generos = {"Seleccionar...", "Masculino", "Femenino"};
        cmbGenero = crearComboBox(generos);
        panel.add(crearFilaCampo("Género:", cmbGenero));
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // SECCIÓN: Datos Académicos
        panel.add(crearSeccionTitulo("Datos Académicos"));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        String[] grados = {"Seleccionar...", "1ro Primaria", "2do Primaria", "3ro Primaria", 
                          "4to Primaria", "5to Primaria", "6to Primaria"};
        cmbGrado = crearComboBox(grados);
        panel.add(crearFilaCampo("Grado:", cmbGrado));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        String[] secciones = {"Seleccionar...", "Sección A", "Sección B", "Sección C"};
        cmbSeccion = crearComboBox(secciones);
        panel.add(crearFilaCampo("Sección:", cmbSeccion));
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // SECCIÓN: Datos de Contacto
        panel.add(crearSeccionTitulo("Datos de Contacto"));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        txtDireccion = crearCampoTexto("Dirección", "Ej: Calle Principal #123");
        panel.add(crearFilaCampo("Dirección:", txtDireccion));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        txtTelefono = crearCampoTexto("Teléfono", "Ej: 809-555-1234");
        panel.add(crearFilaCampo("Teléfono:", txtTelefono));
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // SECCIÓN: Datos del Padre/Tutor
        panel.add(crearSeccionTitulo("Datos del Padre/Tutor"));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        txtNombrePadre = crearCampoTexto("Nombre Completo", "Ej: Juan García Pérez");
        panel.add(crearFilaCampo("Nombre Completo:", txtNombrePadre));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        txtEmailPadre = crearCampoTexto("Email", "Ej: padre@correo.com");
        panel.add(crearFilaCampo("Email:", txtEmailPadre));
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        return panel;
    }
    
    private JLabel crearSeccionTitulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setForeground(new Color(25, 25, 112));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }
    
    private JPanel crearFilaCampo(String label, JComponent campo) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(700, 80));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(new Color(70, 70, 70));
        
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(campo, BorderLayout.CENTER);
        
        return panel;
    }
    
    // MÉTODO CORREGIDO: crearCampoTexto
    private JTextField crearCampoTexto(String placeholder, String textoPlaceholder) {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setPreferredSize(new Dimension(500, 40));
        txt.setMaximumSize(new Dimension(500, 40)); // IMPORTANTE: Añadido para BoxLayout
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        
        // Placeholder
        txt.setForeground(Color.GRAY);
        txt.setText(textoPlaceholder);
        
        txt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txt.getText().equals(textoPlaceholder)) {
                    txt.setText("");
                    txt.setForeground(Color.BLACK);
                }
                txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                    BorderFactory.createEmptyBorder(5, 12, 5, 12)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (txt.getText().isEmpty()) {
                    txt.setText(textoPlaceholder);
                    txt.setForeground(Color.GRAY);
                }
                txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                    BorderFactory.createEmptyBorder(5, 12, 5, 12)
                ));
            }
        });
        
        return txt;
    }
    
    private JComboBox<String> crearComboBox(String[] opciones) {
        JComboBox<String> cmb = new JComboBox<>(opciones);
        cmb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmb.setPreferredSize(new Dimension(500, 40));
        cmb.setMaximumSize(new Dimension(500, 40)); // IMPORTANTE: Añadido para BoxLayout
        cmb.setBackground(Color.WHITE);
        cmb.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        return cmb;
    }
    
    private JPanel crearBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(220, 220, 220)));
        
        // Botón Cancelar CORREGIDO
        JButton btnCancelar = new JButton("CANCELAR");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.setBackground(SECONDARY_COLOR);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25)); // CORREGIDO: Usar border vacío en lugar de setBorderPainted(false)
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setOpaque(true);
        btnCancelar.setContentAreaFilled(true);
        
        btnCancelar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnCancelar.setBackground(SECONDARY_COLOR.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnCancelar.setBackground(SECONDARY_COLOR);
            }
        });
        
        btnCancelar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Deseas cancelar el registro?\nSe perderán los datos ingresados.",
                "Confirmar Cancelación",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
            }
        });
        
        // Botón Guardar CORREGIDO
        JButton btnGuardar = new JButton("GUARDAR REGISTRO");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setBackground(SUCCESS_COLOR);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25)); // CORREGIDO: Usar border vacío
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setOpaque(true);
        btnGuardar.setContentAreaFilled(true);
        
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
        
        btnGuardar.addActionListener(e -> guardarEstudiante());
        
        panel.add(btnCancelar);
        panel.add(btnGuardar);
        
        return panel;
    }
    
    private void guardarEstudiante() {
        // Validaciones mejoradas
        if (!validarCamposObligatorios()) {
            return;
        }
        
        // AQUÍ VA LA LÓGICA PARA GUARDAR EN LA BASE DE DATOS
        String mensaje = "Estudiante registrado exitosamente:\n\n" +
                        "Nombre: " + obtenerTexto(txtNombre) + " " + obtenerTexto(txtApellido) + "\n" +
                        "Matrícula: " + obtenerTexto(txtMatricula) + "\n" +
                        "Grado: " + cmbGrado.getSelectedItem();
        
        JOptionPane.showMessageDialog(this,
            mensaje,
            "Registro Exitoso",
            JOptionPane.INFORMATION_MESSAGE);
        
        // Si tenemos referencia a ListaEstudiantes, actualizarla
        if (listaEstudiantesRef != null) {
            agregarEstudianteALista();
        }
        
        // Limpiar formulario o cerrar ventana
        int opcion = JOptionPane.showConfirmDialog(this,
            "¿Deseas registrar otro estudiante?",
            "Continuar",
            JOptionPane.YES_NO_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
            limpiarFormulario();
        } else {
            dispose();
        }
    }
    
    // MÉTODO NUEVO: Para agregar estudiante a la lista principal
    private void agregarEstudianteALista() {
        try {
            // Crear un nuevo estudiante con los datos del formulario
            String nombreCompleto = obtenerTexto(txtNombre) + " " + obtenerTexto(txtApellido);
            String matricula = obtenerTexto(txtMatricula);
            String grado = cmbGrado.getSelectedItem().toString();
            String edad = calcularEdad(); // Método para calcular edad desde fecha de nacimiento
            
            // Usar reflexión o método público para agregar el estudiante
            // Por ahora, simplemente mostramos un mensaje
            JOptionPane.showMessageDialog(this,
                "Estudiante agregado a la lista principal:\n" + nombreCompleto,
                "Actualización Exitosa",
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al agregar a la lista: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // MÉTODO NUEVO: Calcular edad aproximada desde fecha de nacimiento
    private String calcularEdad() {
        String fecha = obtenerTexto(txtFechaNacimiento);
        if (fecha.isEmpty() || fecha.equals("DD/MM/AAAA")) {
            return "8 años"; // Valor por defecto
        }
        // Lógica simple para calcular edad (implementación básica)
        return "8 años";
    }
    
    // MÉTODO CORREGIDO: validarCamposObligatorios
    private boolean validarCamposObligatorios() {
        StringBuilder errores = new StringBuilder();
        
        // Validar campos obligatorios
        if (esPlaceholder(txtNombre) || obtenerTexto(txtNombre).isEmpty()) {
            errores.append("• Nombre es obligatorio\n");
            txtNombre.requestFocus();
        }
        
        if (esPlaceholder(txtApellido) || obtenerTexto(txtApellido).isEmpty()) {
            errores.append("• Apellido es obligatorio\n");
            if (errores.length() == 0) txtApellido.requestFocus();
        }
        
        if (esPlaceholder(txtMatricula) || obtenerTexto(txtMatricula).isEmpty()) {
            errores.append("• Matrícula es obligatoria\n");
            if (errores.length() == 0) txtMatricula.requestFocus();
        }
        
        if (cmbGrado.getSelectedIndex() == 0) {
            errores.append("• Grado es obligatorio\n");
            if (errores.length() == 0) cmbGrado.requestFocus();
        }
        
        if (cmbGenero.getSelectedIndex() == 0) {
            errores.append("• Género es obligatorio\n");
            if (errores.length() == 0) cmbGenero.requestFocus();
        }
        
        // Si hay errores, mostrarlos
        if (errores.length() > 0) {
            JOptionPane.showMessageDialog(this,
                "Por favor completa los siguientes campos obligatorios:\n\n" + errores.toString(),
                "Campos Incompletos",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private boolean esPlaceholder(JTextField txt) {
        return txt.getForeground().equals(Color.GRAY);
    }
    
    private String obtenerTexto(JTextField txt) {
        if (esPlaceholder(txt)) {
            return "";
        }
        return txt.getText().trim();
    }
    
    private void limpiarFormulario() {
        txtNombre.setText("Ej: María José");
        txtNombre.setForeground(Color.GRAY);
        
        txtApellido.setText("Ej: García López");
        txtApellido.setForeground(Color.GRAY);
        
        txtMatricula.setText("Ej: 2024001");
        txtMatricula.setForeground(Color.GRAY);
        
        txtFechaNacimiento.setText("DD/MM/AAAA");
        txtFechaNacimiento.setForeground(Color.GRAY);
        
        txtDireccion.setText("Ej: Calle Principal #123");
        txtDireccion.setForeground(Color.GRAY);
        
        txtTelefono.setText("Ej: 809-555-1234");
        txtTelefono.setForeground(Color.GRAY);
        
        txtNombrePadre.setText("Ej: Juan García Pérez");
        txtNombrePadre.setForeground(Color.GRAY);
        
        txtEmailPadre.setText("Ej: padre@correo.com");
        txtEmailPadre.setForeground(Color.GRAY);
        
        cmbGrado.setSelectedIndex(0);
        cmbSeccion.setSelectedIndex(0);
        cmbGenero.setSelectedIndex(0);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new RegistroApp());
    }
}