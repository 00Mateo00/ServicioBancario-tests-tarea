package com.mateoossa.banco.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

@DisplayName("Pruebas de la clase Banco")
class BancoTest {

  private Banco bancoDePrueba;

  @BeforeEach
  void setUp() {
    bancoDePrueba = Banco.newBanco("Banco Central de Pruebas");
  }


  @Nested
  @DisplayName("Pruebas del constructor")
  class ConstructorTests {

    @Test
    @DisplayName("Debería lanzar IllegalArgumentException cuando el nombre es vacío")
    void deberiaLanzarExcepcionCuandoNombreEsVacio() {
      String nombre = "";
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Banco.newBanco(nombre));
      assertEquals("El nombre del banco no puede ser nulo o vacío.", exception.getMessage(), () -> "El mensaje de excepción no coincide para nombre vacío.");
    }

    @Test
    @DisplayName("Debería lanzar IllegalArgumentException cuando el nombre es nulo")
    void deberiaLanzarExcepcionCuandoNombreEsNulo() {
      String nombre = null;
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Banco.newBanco(nombre));
      assertEquals("El nombre del banco no puede ser nulo o vacío.", exception.getMessage(), () -> "El mensaje de excepción no coincide para nombre nulo.");
    }
  }

  @Nested
  @DisplayName("Pruebas del método agregarCuenta")
  class AgregarCuentaTests {

    @Test
    @DisplayName("Debería agregar una cuenta correctamente")
    void deberiaAgregarCuentaCorrectamente() {
      Banco banco = Banco.newBanco("J.P. Morgan");
      Cuenta cuenta = new Cuenta("Mateo", 2000.0, banco);
      banco.agregarCuenta(cuenta);
      assertTrue(banco.getCuentas().contains(cuenta), () -> "La cuenta debería estar en la lista de cuentas del banco.");
    }

    @Test
    @DisplayName("Debería lanzar IllegalArgumentException al intentar agregar una cuenta duplicada")
    void deberiaLanzarExcepcionAlAgregarCuentaDuplicada() {
      Banco banco = Banco.newBanco("J.P. Morgan");
      Cuenta cuenta = new Cuenta("Mateo", 2000.0, banco);
      banco.agregarCuenta(cuenta);
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> banco.agregarCuenta(cuenta));
      assertEquals("La cuenta ya está registrada en el banco.", exception.getMessage(), () -> "El mensaje de excepción no coincide para cuenta duplicada.");
    }

    @Test
    @DisplayName("Debería lanzar IllegalArgumentException al intentar agregar una cuenta nula")
    void deberiaLanzarExcepcionAlAgregarCuentaNull() {
      Banco banco = Banco.newBanco("J.P. Morgan");
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> banco.agregarCuenta(null));
      assertEquals("La cuenta no puede ser nula.", exception.getMessage(), () -> "El mensaje de excepción no coincide para cuenta nula.");
    }
  }

  @Nested
  @DisplayName("pruebas del método crearCopia")
  class crearCopiaTests {

    @Test
    @DisplayName("Debe crear una copia no nula del banco original")
    void debeCrearCopiaNoNula() {
      Banco copiaBanco = Banco.crearCopia(bancoDePrueba);
      assertNotNull(copiaBanco, () -> "La copia del banco no debería ser nula.");
    }


    @Test
    @DisplayName("la copia debe tener el mismo contenido que la original")
    void copiaDebeTenerElMismoContenido(){
      bancoDePrueba.agregarCuenta(new Cuenta("Cuenta1", 100.0, Banco.newBanco("Banco Temp")));
      bancoDePrueba.agregarCuenta(new Cuenta("Cuenta2", 200.0, Banco.newBanco("Banco Temp")));
      Banco copiaBanco = Banco.crearCopia(bancoDePrueba);
      assertEquals(copiaBanco,bancoDePrueba, ()->"el contenido de los atributos debería ser el mismo");
      assertEquals(bancoDePrueba.getCuentas().size(), copiaBanco.getCuentas().size(), () -> "El número de cuentas en la copia no coincide con el del original.");

    }


    @Test
    @DisplayName("La copia debe ser independiente del original (defensive copy de cuentas)")
    void copiaDebeSerIndependiente() {
      bancoDePrueba.agregarCuenta(new Cuenta("CuentaA", 500.0, Banco.newBanco("Banco Temp")));
      Banco copiaBanco = Banco.crearCopia(bancoDePrueba);
      bancoDePrueba.agregarCuenta(new Cuenta("CuentaB", 600.0, Banco.newBanco("Banco Temp")));

      assertNotSame(bancoDePrueba,copiaBanco, ()->"deben ser referencias distintas");

      assertEquals(1, copiaBanco.getCuentas().size(), () -> "La lista de cuentas de la copia no debe cambiar si el original se modifica");
      assertEquals(2, bancoDePrueba.getCuentas().size(), () -> "El original debe haber cambiado");

      copiaBanco.agregarCuenta(new Cuenta("CuentaC", 700.0, Banco.newBanco("Banco Temp")));

      assertEquals(2, copiaBanco.getCuentas().size(), () -> "La copia debe haber cambiado");
      assertEquals(2, bancoDePrueba.getCuentas().size(), () -> "El original no debe cambiar si la copia se modifica");
    }

    @Test
    @DisplayName("Debe lanzar NullPointerException si el banco original es nulo")
    void debeLanzarExcepcionSiOriginalEsNulo() {
      assertThrows(NullPointerException.class, () -> {
        Banco.crearCopia(null);
      }, () -> "Debería lanzar NullPointerException si el banco original es nulo.");
    }
  }

  @Nested
  @DisplayName("pruebas del método setCuentas")
  class setCuentasTests {

    @Test
    @DisplayName("Debe establecer una lista de cuentas no nula")
    void debeEstablecerListaCuentasNoNula() {
      List<Cuenta> nuevasCuentas = new ArrayList<>();
      nuevasCuentas.add(new Cuenta("Cuenta1", 100.0, Banco.newBanco("Banco Temp")));
      nuevasCuentas.add(new Cuenta("Cuenta2", 200.0, Banco.newBanco("Banco Temp")));

      bancoDePrueba.setCuentas(nuevasCuentas);

      assertNotNull(bancoDePrueba.getCuentas(), () -> "La lista de cuentas del banco no debe ser nula.");
      assertEquals(2, bancoDePrueba.getCuentas().size(), () -> "La lista de cuentas debe tener 2 elementos.");
      assertTrue(bancoDePrueba.getCuentas().containsAll(nuevasCuentas), () -> "La lista de cuentas debe contener todas las cuentas esperadas.");
    }

    @Test
    @DisplayName("Debe manejar una lista de cuentas nula estableciendo una lista vacía")
    void debeManejarListaCuentasNula() {
      bancoDePrueba.setCuentas(null);
      assertNotNull(bancoDePrueba.getCuentas(), () -> "La lista de cuentas no debería ser nula después de establecer null.");
      assertTrue(bancoDePrueba.getCuentas().isEmpty(), () -> "La lista de cuentas debería estar vacía después de establecer null.");
    }

    @Test
    @DisplayName("La lista de cuentas establecida debe ser una copia defensiva")
    void listaEstablecidaDebeSerCopiaDefensiva() {
      List<Cuenta> listaExterna = new ArrayList<>();
      listaExterna.add(new Cuenta("CuentaExterna", 100.0, Banco.newBanco("Banco Temp")));

      bancoDePrueba.setCuentas(listaExterna);

      listaExterna.add(new Cuenta("OtraCuentaExterna", 200.0, Banco.newBanco("Banco Temp")));

      assertEquals(1, bancoDePrueba.getCuentas().size(), () -> "La lista interna debe ser una copia independiente");
      assertEquals("CuentaExterna", bancoDePrueba.getCuentas().get(0).getNombre(), () -> "El nombre de la primera cuenta no coincide.");
    }

    @Test
    @DisplayName("Establecer una lista vacía debe resultar en una lista vacía en el banco")
    void debeEstablecerListaVacia() {
      List<Cuenta> listaVacia = new ArrayList<>();
      bancoDePrueba.setCuentas(listaVacia);
      assertNotNull(bancoDePrueba.getCuentas(), () -> "La lista de cuentas no debería ser nula al establecer una lista vacía.");
      assertTrue(bancoDePrueba.getCuentas().isEmpty(), () -> "La lista de cuentas debería estar vacía al establecer una lista vacía.");
    }
  }
}