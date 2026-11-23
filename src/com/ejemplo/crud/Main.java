package com.ejemplo.crud;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class Main extends JFrame {
    private static final String RUTA_ARCHIVO = "data/productos.dat";

    private final ProductoRepository repository;
    private final DefaultTableModel tableModel;
    private final JTable tabla;

    public Main() {
        super("CRUD Serializado con Record - Productos");
        this.repository = new ProductoRepository(RUTA_ARCHIVO);

        this.tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio", "Stock"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.tabla = new JTable(tableModel);

        initUI();
        refrescarTabla();
        setSize(680, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(24);
        tabla.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(scrollPane, BorderLayout.CENTER);

        JButton btnNuevo = new JButton("Nuevo");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRecargar = new JButton("Recargar archivo");

        btnNuevo.addActionListener(e -> crearProducto());
        btnEditar.addActionListener(e -> editarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnRecargar.addActionListener(e -> {
            refrescarTabla();
            JOptionPane.showMessageDialog(this, "Datos cargados desde " + RUTA_ARCHIVO);
        });

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRecargar);

        JLabel lblRuta = new JLabel("Archivo: " + RUTA_ARCHIVO + "  ");
        lblRuta.setForeground(Color.DARK_GRAY);

        JPanel barraInferior = new JPanel(new BorderLayout());
        barraInferior.add(panelBotones, BorderLayout.WEST);
        barraInferior.add(lblRuta, BorderLayout.EAST);

        add(barraInferior, BorderLayout.SOUTH);
    }

    private void crearProducto() {
        ProductoDialog dialog = new ProductoDialog(this, "Nuevo Producto", null);
        dialog.setVisible(true);
        Producto nuevo = dialog.getResultado();
        if (nuevo != null) {
            try {
                repository.crear(nuevo);
                refrescarTabla();
            } catch (IOException | IllegalArgumentException ex) {
                mostrarError("No se pudo crear el producto", ex.getMessage());
            }
        }
    }

    private void editarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto de la tabla para editar.");
            return;
        }

        int idOriginal = (int) tableModel.getValueAt(fila, 0);
        Producto actual = repository.buscarPorId(idOriginal).orElse(null);
        if (actual == null) {
            refrescarTabla();
            return;
        }

        ProductoDialog dialog = new ProductoDialog(this, "Editar Producto", actual);
        dialog.setVisible(true);
        Producto editado = dialog.getResultado();
        if (editado != null) {
            try {
                repository.actualizar(idOriginal, editado);
                refrescarTabla();
            } catch (IOException | IllegalArgumentException ex) {
                mostrarError("No se pudo actualizar el producto", ex.getMessage());
            }
        }
    }

    private void eliminarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto de la tabla para eliminar.");
            return;
        }

        int id = (int) tableModel.getValueAt(fila, 0);
        String nombre = String.valueOf(tableModel.getValueAt(fila, 1));

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar el producto \"" + nombre + "\" (ID " + id + ")?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                repository.eliminar(id);
                refrescarTabla();
            } catch (IOException | IllegalArgumentException ex) {
                mostrarError("No se pudo eliminar el producto", ex.getMessage());
            }
        }
    }

    private void refrescarTabla() {
        tableModel.setRowCount(0);
        List<Producto> lista = repository.listarTodos();
        for (Producto p : lista) {
            tableModel.addRow(new Object[]{p.id(), p.nombre(), p.precio(), p.stock()});
        }
    }

    private void mostrarError(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new Main().setVisible(true);
        });
    }
}
