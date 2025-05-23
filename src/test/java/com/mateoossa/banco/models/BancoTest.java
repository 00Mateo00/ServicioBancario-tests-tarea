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
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> Banco.newBanco(nombre));
      assertEquals("El nombre del banco no puede ser nulo o vacío.", exception.getMessage());
    }

    @Test
    @DisplayName("Debería lanzar IllegalArgumentException cuando el nombre es nulo")
    void deberiaLanzarExcepcionCuandoNombreEsNulo() {
      String nombre = null;
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> Banco.newBanco(nombre));
      assertEquals("El nombre del banco no puede ser nulo o vacío.", exception.getMessage());
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
      assertTrue(banco.getCuentas().contains(cuenta),
          "La cuenta debería estar en la lista de cuentas del banco.");
    }

    @Test
    @DisplayName("Debería lanzar IllegalArgumentException al intentar agregar una cuenta duplicada")
    void deberiaLanzarExcepcionAlAgregarCuentaDuplicada() {
      Banco banco = Banco.newBanco("J.P. Morgan");
      Cuenta cuenta = new Cuenta("Mateo", 2000.0, banco);
      banco.agregarCuenta(cuenta);
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> banco.agregarCuenta(cuenta));
      assertEquals("La cuenta ya está registrada en el banco.", exception.getMessage());
    }

    @Test
    @DisplayName("Debería lanzar IllegalArgumentException al intentar agregar una cuenta nula")
    void deberiaLanzarExcepcionAlAgregarCuentaNull() {
      Banco banco = Banco.newBanco("J.P. Morgan");
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> banco.agregarCuenta(null));
      assertEquals("La cuenta no puede ser nula.", exception.getMessage());
    }
  }

  @Nested
  @DisplayName("pruebas del método crearCopia")
  class crearCopiaTests {

    @Test
    @DisplayName("Debe crear una copia no nula del banco original")
    void debeCrearCopiaNoNula() {
      Banco copiaBanco = Banco.crearCopia(bancoDePrueba);
      assertNotNull(copiaBanco);
    }

    @Test
    @DisplayName("La copia debe tener el mismo nombre que el original")
    void copiaDebeTenerMismoNombre() {
      Banco copiaBanco = Banco.crearCopia(bancoDePrueba);
      assertEquals(bancoDePrueba.getNombre(), copiaBanco.getNombre());
    }

    @Test
    @DisplayName("La copia debe tener el mismo número de cuentas que el original")
    void copiaDebeTenerMismoNumeroCuentas() {
      bancoDePrueba.agregarCuenta(new Cuenta("Cuenta1", 100.0, Banco.newBanco("Banco Temp")));
      bancoDePrueba.agregarCuenta(new Cuenta("Cuenta2", 200.0, Banco.newBanco("Banco Temp")));
      Banco copiaBanco = Banco.crearCopia(bancoDePrueba);
      assertEquals(bancoDePrueba.getCuentas().size(), copiaBanco.getCuentas().size());
    }

    @Test
    @DisplayName("La copia debe ser independiente del original (defensive copy de cuentas)")
    void copiaDebeSerIndependiente() {
      bancoDePrueba.agregarCuenta(new Cuenta("CuentaA", 500.0, Banco.newBanco("Banco Temp")));
      Banco copiaBanco = Banco.crearCopia(bancoDePrueba);
      bancoDePrueba.agregarCuenta(new Cuenta("CuentaB", 600.0, Banco.newBanco("Banco Temp")));

      assertEquals(1, copiaBanco.getCuentas().size(), "La lista de cuentas de la copia no debe cambiar si el original se modifica");
      assertEquals(2, bancoDePrueba.getCuentas().size(), "El original debe haber cambiado");

      copiaBanco.agregarCuenta(new Cuenta("CuentaC", 700.0, Banco.newBanco("Banco Temp")));

      assertEquals(2, bancoDePrueba.getCuentas().size(), "El original no debe cambiar si la copia se modifica");
      assertEquals(2, copiaBanco.getCuentas().size(), "La copia debe haber cambiado");
    }

    @Test
    @DisplayName("Debe lanzar NullPointerException si el banco original es nulo")
    void debeLanzarExcepcionSiOriginalEsNulo() {
      assertThrows(NullPointerException.class, () -> {
        Banco.crearCopia(null);
      });
    }
  }

  @Nested
  @DisplayName("pruebas del método setCuentas")
  class setCuentasTests {

    private Banco bancoParaSetCuentas;

    @BeforeEach
    void setUpSetCuentas() {
      bancoParaSetCuentas = Banco.newBanco("Banco para SetCuentas");
    }

    @Test
    @DisplayName("Debe establecer una lista de cuentas no nula")
    void debeEstablecerListaCuentasNoNula() {
      List<Cuenta> nuevasCuentas = new ArrayList<>();
      nuevasCuentas.add(new Cuenta("Cuenta1", 100.0, Banco.newBanco("Banco Temp")));
      nuevasCuentas.add(new Cuenta("Cuenta2", 200.0, Banco.newBanco("Banco Temp")));

      bancoParaSetCuentas.setCuentas(nuevasCuentas);

      Assertions.assertNotNull(bancoParaSetCuentas.getCuentas());
      Assertions.assertEquals(2, bancoParaSetCuentas.getCuentas().size());
      Assertions.assertTrue(bancoParaSetCuentas.getCuentas().containsAll(nuevasCuentas));
    }

    @Test
    @DisplayName("Debe manejar una lista de cuentas nula estableciendo una lista vacía")
    void debeManejarListaCuentasNula() {
      bancoParaSetCuentas.setCuentas(null);
      Assertions.assertNotNull(bancoParaSetCuentas.getCuentas());
      Assertions.assertTrue(bancoParaSetCuentas.getCuentas().isEmpty());
    }

    @Test
    @DisplayName("La lista de cuentas establecida debe ser una copia defensiva")
    void listaEstablecidaDebeSerCopiaDefensiva() {
      List<Cuenta> listaExterna = new ArrayList<>();
      listaExterna.add(new Cuenta("CuentaExterna", 100.0, Banco.newBanco("Banco Temp")));

      bancoParaSetCuentas.setCuentas(listaExterna);

      listaExterna.add(new Cuenta("OtraCuentaExterna", 200.0, Banco.newBanco("Banco Temp")));

      Assertions.assertEquals(1, bancoParaSetCuentas.getCuentas().size(), "La lista interna debe ser una copia independiente");
      Assertions.assertEquals("CuentaExterna", bancoParaSetCuentas.getCuentas().get(0).getNombre());
    }

    @Test
    @DisplayName("Establecer una lista vacía debe resultar en una lista vacía en el banco")
    void debeEstablecerListaVacia() {
      List<Cuenta> listaVacia = new ArrayList<>();
      bancoParaSetCuentas.setCuentas(listaVacia);
      Assertions.assertNotNull(bancoParaSetCuentas.getCuentas());
      Assertions.assertTrue(bancoParaSetCuentas.getCuentas().isEmpty());
    }
  }
}