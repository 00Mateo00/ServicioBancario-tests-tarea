package com.mateoossa.banco.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Pruebas de la clase Banco")
class BancoTest {

    @Nested
    @DisplayName("Pruebas del constructor")
    class ConstructorTests {

        @Test
        @DisplayName("Debería lanzar IllegalArgumentException cuando el nombre es vacío")
        void deberiaLanzarExcepcionCuandoNombreEsVacio() {
            String nombre = "";
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Banco(nombre));
            assertEquals("El nombre del banco no puede ser nulo o vacío.", exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar IllegalArgumentException cuando el nombre es nulo")
        void deberiaLanzarExcepcionCuandoNombreEsNulo() {
            String nombre = null;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Banco(nombre));
            assertEquals("El nombre del banco no puede ser nulo o vacío.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Pruebas del método agregarCuenta")
    class AgregarCuentaTests {

        @Test
        @DisplayName("Debería agregar una cuenta correctamente")
        void deberiaAgregarCuentaCorrectamente() {
            Banco banco = new Banco("J.P. Morgan");
            Cuenta cuenta = new Cuenta("Mateo", 2000.0, banco);
            banco.agregarCuenta(cuenta);
            assertTrue(banco.getCuentas().contains(cuenta),
                    "La cuenta debería estar en la lista de cuentas del banco.");
        }

        @Test
        @DisplayName("Debería lanzar IllegalArgumentException al intentar agregar una cuenta duplicada")
        void deberiaLanzarExcepcionAlAgregarCuentaDuplicada() {
            Banco banco = new Banco("J.P. Morgan");
            Cuenta cuenta = new Cuenta("Mateo", 2000.0, banco);
            banco.agregarCuenta(cuenta);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> banco.agregarCuenta(cuenta));
            assertEquals("La cuenta ya está registrada en el banco.", exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar IllegalArgumentException al intentar agregar una cuenta nula")
        void deberiaLanzarExcepcionAlAgregarCuentaNull() {
            Banco banco = new Banco("J.P. Morgan");
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> banco.agregarCuenta(null));
            assertEquals("La cuenta no puede ser nula.", exception.getMessage());
        }
    }
}