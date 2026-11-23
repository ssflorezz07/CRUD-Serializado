package com.ejemplo.crud;

import java.io.*;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ProductoRepository {
    private final Path archivoDatos;
    private final List<Producto> productos;

    public ProductoRepository(String rutaArchivo) {
        this.archivoDatos = Path.of(rutaArchivo);
        this.productos = new ArrayList<>(cargarDesdeArchivo());
    }

    public List<Producto> listarTodos() {
        return productos.stream()
                .sorted(Comparator.comparingInt(Producto::id))
                .toList();
    }

    public Optional<Producto> buscarPorId(int id) {
        return productos.stream()
                .filter(p -> p.id() == id)
                .findFirst();
    }

    public void crear(Producto nuevo) throws IOException {
        if (buscarPorId(nuevo.id()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un producto con el ID " + nuevo.id());
        }
        productos.add(nuevo);
        guardarEnArchivo();
    }

    public void actualizar(int idOriginal, Producto actualizado) throws IOException {
        int indice = -1;
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).id() == idOriginal) {
                indice = i;
                break;
            }
        }
        if (indice == -1) {
            throw new IllegalArgumentException("No se encontró el producto con ID " + idOriginal);
        }
        if (idOriginal != actualizado.id() && buscarPorId(actualizado.id()).isPresent()) {
            throw new IllegalArgumentException("El nuevo ID " + actualizado.id() + " ya está en uso.");
        }
        productos.set(indice, actualizado);
        guardarEnArchivo();
    }

    public void eliminar(int id) throws IOException {
        boolean eliminado = productos.removeIf(p -> p.id() == id);
        if (!eliminado) {
            throw new IllegalArgumentException("No se encontró el producto con ID " + id);
        }
        guardarEnArchivo();
    }

    private List<Producto> cargarDesdeArchivo() {
        if (!Files.exists(archivoDatos)) {
            return List.of();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(Files.newInputStream(archivoDatos)))) {
            Object leido = ois.readObject();
            if (leido instanceof List<?> lista) {
                List<Producto> resultado = new ArrayList<>();
                for (Object item : lista) {
                    if (item instanceof Producto p) {
                        resultado.add(p);
                    }
                }
                return resultado;
            }
            return List.of();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al leer el archivo de datos, se iniciará vacío: " + e.getMessage());
            return List.of();
        }
    }

    private void guardarEnArchivo() throws IOException {
        Path padre = archivoDatos.getParent();
        if (padre != null && !Files.exists(padre)) {
            Files.createDirectories(padre);
        }

        Path temporal = archivoDatos.resolveSibling(archivoDatos.getFileName().toString() + ".tmp");
        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(temporal)))) {
            oos.writeObject(new ArrayList<>(productos));
        }

        try {
            Files.move(temporal, archivoDatos, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(temporal, archivoDatos, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
