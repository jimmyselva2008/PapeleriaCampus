--Creacion de la base de datos
CREATE DATABASE papeleria_campus;

--Crear el SCHEMA 
CREATE SCHEMA catalogos;

--Creación de la tabla producto
CREATE TABLE catalogos.producto
(
    id_producto INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    codigo VARCHAR(10) NOT NULL UNIQUE,
    nombre VARCHAR(80) NOT NULL,
    categoria VARCHAR(20) NOT NULL,
    precio NUMERIC(10, 2) NOT NULL,
    stock INTEGER NOT NULL,

    --Restricciones CHECK exigidas por la rubrica
    CONSTRAINT chk_producto_codigo_no_vacio 
        CHECK (length(trim(codigo)) > 0),

    CONSTRAINT chk_producto_nombre_longitud 
        CHECK (length(trim(nombre)) BETWEEN 3 AND 80),

    CONSTRAINT chk_producto_categoria_valida 
        CHECK (categoria IN ('Papelería', 'Tecnología', 'Accesorios')),

    CONSTRAINT chk_producto_precio_positivo 
        CHECK (precio > 0),

    CONSTRAINT chk_producto_stock_no_negativo 
        CHECK (stock >= 0)
);

