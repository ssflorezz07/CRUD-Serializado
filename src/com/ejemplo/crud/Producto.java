package com.ejemplo.crud;

import java.io.Serial;
import java.io.Serializable;

public record Producto(int id, String nombre, double precio, int stock) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public Producto {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que 0.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        nombre = nombre.trim();
    }
}
