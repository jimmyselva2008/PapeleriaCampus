package com.tuempresa.papeleriacampus.controller;

import com.tuempresa.papeleriacampus.dao.ProductoDAO;
import com.tuempresa.papeleriacampus.model.Producto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductoController {

    @FXML private TextField txtFiltroTexto;
    @FXML private ComboBox<String> cmbFiltroCategoria;
    @FXML private CheckBox chkSoloConStock;

    @FXML private TableView<Producto> tblProductos;
    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String> colCodigo;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, BigDecimal> colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;
    @FXML private Label lblContador;

    @FXML private TextField txtIdProducto;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private ComboBox<String> cmbCategoria;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ObservableList<Producto> listaProductos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarTabla();
        cargarCategorias();
        configurarSeleccionTabla();
        cargarProductos();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        tblProductos.setItems(listaProductos);
    }

    private void cargarCategorias() {
        ObservableList<String> categorias = FXCollections.observableArrayList("Papelería", "Tecnología", "Accesorios");
        cmbCategoria.setItems(categorias);

        ObservableList<String> categoriasFiltro = FXCollections.observableArrayList("Todas", "Papelería", "Tecnología", "Accesorios");
        cmbFiltroCategoria.setItems(categoriasFiltro);
        cmbFiltroCategoria.setValue("Todas");
    }

    private void configurarSeleccionTabla() {
        tblProductos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtIdProducto.setText(String.valueOf(newVal.getIdProducto()));
                txtCodigo.setText(newVal.getCodigo());
                txtNombre.setText(newVal.getNombre());
                cmbCategoria.setValue(newVal.getCategoria());
                txtPrecio.setText(newVal.getPrecio().toPlainString());
                txtStock.setText(String.valueOf(newVal.getStock()));
            }
        });
    }

    @FXML
    public void buscarProductos() {
        try {
            String texto = txtFiltroTexto.getText();
            String categoria = cmbFiltroCategoria.getValue();
            boolean soloConStock = chkSoloConStock.isSelected();

            List<Producto> resultado = productoDAO.buscar(texto, categoria, soloConStock);
            listaProductos.setAll(resultado);
            actualizarContador();
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Búsqueda (E1)", "No se pudo realizar la consulta: " + e.getMessage());
        }
    }

    @FXML
    public void cargarProductos() {
        try {
            List<Producto> resultado = productoDAO.buscar(null, "Todas", false);
            listaProductos.setAll(resultado);
            actualizarContador();
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Conexión (E1)", "Error al conectar con PostgreSQL: " + e.getMessage());
        }
    }

    @FXML
    public void limpiarFiltros() {
        txtFiltroTexto.clear();
        cmbFiltroCategoria.setValue("Todas");
        chkSoloConStock.setSelected(false);
        cargarProductos();
    }

    private void actualizarContador() {
        lblContador.setText("Registros visibles: " + listaProductos.size());
    }

    @FXML
    public void guardarProducto() {
        if (!validarFormulario(null)) return;

        try {
            Producto p = extraerProductoFormulario(null);
            if (productoDAO.guardar(p)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto registrado correctamente.");
                limpiarFormulario();
                cargarProductos();
            }
        } catch (SQLException e) {
            manejarExcepcionSQL(e);
        }
    }

    @FXML
    public void actualizarProducto() {
        Producto seleccionado = tblProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección Requerida (V10)", "Debe seleccionar un producto de la tabla para actualizar.");
            return;
        }

        if (!validarFormulario(seleccionado.getIdProducto())) return;

        try {
            Producto p = extraerProductoFormulario(seleccionado.getIdProducto());
            if (productoDAO.actualizar(p)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto actualizado correctamente.");
                limpiarFormulario();
                cargarProductos();
            }
        } catch (SQLException e) {
            manejarExcepcionSQL(e);
        }
    }

    @FXML
    public void eliminarProducto() {
        Producto seleccionado = tblProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección Requerida (V10)", "Debe seleccionar un producto de la tabla para eliminar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar Eliminación");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Está seguro de eliminar el producto '" + seleccionado.getNombre() + "'?");

        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            try {
                if (productoDAO.eliminar(seleccionado.getIdProducto())) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto eliminado correctamente.");
                    limpiarFormulario();
                    cargarProductos();
                }
            } catch (SQLException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error al Eliminar", "No se pudo eliminar el registro: " + e.getMessage());
            }
        }
    }

    @FXML
    public void limpiarFormulario() {
        txtIdProducto.clear();
        txtCodigo.clear();
        txtNombre.clear();
        cmbCategoria.setValue(null);
        txtPrecio.clear();
        txtStock.clear();
        tblProductos.getSelectionModel().clearSelection();
    }

    private boolean validarFormulario(Integer idExcluir) {
        String codigo = txtCodigo.getText() != null ? txtCodigo.getText().trim() : "";
        String nombre = txtNombre.getText() != null ? txtNombre.getText().trim() : "";
        String categoria = cmbCategoria.getValue();
        String precioStr = txtPrecio.getText() != null ? txtPrecio.getText().trim() : "";
        String stockStr = txtStock.getText() != null ? txtStock.getText().trim() : "";

        if (codigo.isEmpty() || nombre.isEmpty() || categoria == null || precioStr.isEmpty() || stockStr.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Incompletos (V1)", "Todos los campos con (*) son obligatorios.");
            return false;
        }

        if (codigo.length() > 10) {
            mostrarAlerta(Alert.AlertType.WARNING, "Código Inválido (V2)", "El código no puede superar los 10 caracteres.");
            return false;
        }

        if (nombre.length() < 3 || nombre.length() > 80) {
            mostrarAlerta(Alert.AlertType.WARNING, "Nombre Inválido (V4)", "El nombre debe tener entre 3 y 80 caracteres.");
            return false;
        }

        BigDecimal precio;
        try {
            precio = new BigDecimal(precioStr);
            if (precio.compareTo(BigDecimal.ZERO) <= 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Precio Inválido (V6)", "El precio debe ser un número estrictamente mayor que 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Formato Inválido (V5)", "El precio debe ser un valor numérico válido (Ej: 65.50).");
            return false;
        }

        try {
            int stock = Integer.parseInt(stockStr);
            if (stock < 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Stock Inválido (V8)", "El stock no puede ser un número negativo.");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Formato Inválido (V7)", "El stock debe ser un número entero válido.");
            return false;
        }

        try {
            if (productoDAO.existeCodigo(codigo, idExcluir)) {
                mostrarAlerta(Alert.AlertType.WARNING, "Código Duplicado (V9)", "El código '" + codigo.toUpperCase() + "' ya se encuentra registrado.");
                return false;
            }
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Validación", "Error al verificar duplicados: " + e.getMessage());
            return false;
        }

        return true;
    }

    private Producto extraerProductoFormulario(Integer id) {
        return new Producto(
                id,
                txtCodigo.getText().trim().toUpperCase(),
                txtNombre.getText().trim(),
                cmbCategoria.getValue(),
                new BigDecimal(txtPrecio.getText().trim()),
                Integer.parseInt(txtStock.getText().trim())
        );
    }

    private void manejarExcepcionSQL(SQLException e) {
        String state = e.getSQLState();
        if ("23514".equals(state)) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Integridad (E2/E3)", "El registro no cumple con los valores permitidos por la base de datos.");
        } else if ("23505".equals(state)) {
            mostrarAlerta(Alert.AlertType.ERROR, "Código Duplicado (E4)", "Ya existe un registro con el mismo código.");
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Base de Datos (E1)", "Ocurrió un error inesperado: " + e.getMessage());
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}