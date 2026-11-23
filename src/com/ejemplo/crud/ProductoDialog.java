package com.ejemplo.crud;

import javax.swing.*;
import java.awt.*;

public class ProductoDialog extends JDialog {
    private final JTextField txtId = new JTextField(15);
    private final JTextField txtNombre = new JTextField(20);
    private final JTextField txtPrecio = new JTextField(15);
    private final JTextField txtStock = new JTextField(15);

    private Producto resultado;

    public ProductoDialog(Frame owner, String titulo, Producto existente) {
        super(owner, titulo, true);
        initUI(existente);
        pack();
        setLocationRelativeTo(owner);
    }

    private void initUI(Producto existente) {
        setLayout(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new GridLayout(4, 2, 8, 8));
        formulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        formulario.add(new JLabel("ID:"));
        formulario.add(txtId);

        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);

        formulario.add(new JLabel("Precio:"));
        formulario.add(txtPrecio);

        formulario.add(new JLabel("Stock:"));
        formulario.add(txtStock);

        if (existente != null) {
            txtId.setText(String.valueOf(existente.id()));
            txtNombre.setText(existente.nombre());
            txtPrecio.setText(String.valueOf(existente.precio()));
            txtStock.setText(String.valueOf(existente.stock()));
        }

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> {
            resultado = null;
            dispose();
        });

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setBorder(BorderFactory.createEmptyBorder(0, 15, 12, 15));
        botones.add(btnCancelar);
        botones.add(btnGuardar);

        add(formulario, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(btnGuardar);
    }

    private void guardar() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String nombre = txtNombre.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim().replace(',', '.'));
            int stock = Integer.parseInt(txtStock.getText().trim());

            resultado = new Producto(id, nombre, precio, stock);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Verifica los campos numéricos: ID y Stock deben ser enteros; Precio debe ser un número.",
                    "Formato inválido",
                    JOptionPane.WARNING_MESSAGE
            );
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    public Producto getResultado() {
        return resultado;
    }
}
