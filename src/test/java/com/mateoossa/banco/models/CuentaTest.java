package com.mateoossa.banco.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class CuentaTest {

  private Cuenta cuenta;
  private Banco bancoDePruebaInicial;

  @BeforeEach
  void setUp() {
    bancoDePruebaInicial = Banco.newBanco("Banco Inicial de Pruebas");
    Cuenta cuentaoriginal1 = new Cuenta("Cuenta test1", 10000, bancoDePruebaInicial);
    bancoDePruebaInicial.agregarCuenta(cuentaoriginal1);
    cuenta = new Cuenta("Cuenta Test", 100.0, bancoDePruebaInicial);
    bancoDePruebaInicial.agregarCuenta(cuenta);
  }

  @Nested
  @DisplayName("pruebas del método setBanco")
  class SetBancoTests {

    @Test
    @DisplayName("Debe establecer un banco no nulo y getBanco debe devolver una copia defensiva")
    void debeEstablecerBancoNoNuloYCopiaDefensiva() {
      Banco nuevoBancoExterno = Banco.newBanco("Nuevo Banco Externo");

      cuenta.setBanco(nuevoBancoExterno);

      Banco bancoObtenidoPorGet = cuenta.getBanco();

      Assertions.assertNotNull(bancoObtenidoPorGet, () -> "El banco obtenido por getBanco no debería ser nulo.");
      Assertions.assertNotSame(nuevoBancoExterno, bancoObtenidoPorGet, () -> "La instancia de banco devuelta por getBanco debería ser una copia defensiva, no la misma referencia.");
      Assertions.assertEquals(nuevoBancoExterno, bancoObtenidoPorGet, () -> "El contenido del banco obtenido por getBanco debería ser igual al nuevo banco externo.");
    }

    @Test
    @DisplayName("Debe establecer un banco nulo")
    void debeEstablecerBancoNulo() {
      cuenta.setBanco(null);
      Assertions.assertNull(cuenta.getBanco(), () -> "El banco de la cuenta debería ser nulo después de establecerlo a nulo.");
    }

    @Test
    @DisplayName("Modificar el banco original (externo) debe afectar el banco interno de la cuenta")
    void modificarBancoOriginalDebeAfectarCuentaBanco() {
      Banco bancoModificableExterno = Banco.newBanco("Banco Modificable Externo");

      cuenta.setBanco(bancoModificableExterno);

      Cuenta cuentaInternaParaBancoExterno = new Cuenta("Cuenta Externa", 0.0, bancoModificableExterno);
      bancoModificableExterno.agregarCuenta(cuentaInternaParaBancoExterno);

      Cuenta segundaCuentaInterna = new Cuenta("Otra Cuenta Externa", 0.0, bancoModificableExterno);
      bancoModificableExterno.agregarCuenta(segundaCuentaInterna);

      Assertions.assertEquals(2, cuenta.getBanco().getCuentas().size(), () -> "El banco referenciado por la cuenta debería tener 2 cuentas después de las adiciones.");
      Assertions.assertEquals(bancoModificableExterno, cuenta.getBanco(), () -> "el método equals deberia identificarlos como iguales ya que deberían tener los mismos atributos con los mismos datos");
      Assertions.assertEquals(2, bancoModificableExterno.getCuentas().size(), () -> "El banco externo modificable debería tener 2 cuentas.");

    }
  }

  @Nested
  @DisplayName("pruebas del método getBanco")
  class GetBancoTests {

    @Test
    @DisplayName("Debe devolver una copia no nula del banco interno")
    void debeDevolverCopiaNoNulaDeBancoInterno() {
      Banco bancoRecuperado = cuenta.getBanco();
      Assertions.assertNotNull(bancoRecuperado, () -> "El banco recuperado por getBanco no debería ser nulo.");
    }

    @Test
    @DisplayName("Debe devolver nulo si el banco interno es nulo")
    void debeDevolverNuloSiBancoInternoEsNulo() {
      Cuenta cuentaSinBanco = new Cuenta("Cuenta sin Banco", 0.0, null);
      Assertions.assertNull(cuentaSinBanco.getBanco(), () -> "getBanco debería devolver nulo si el banco interno de la cuenta es nulo.");
    }

    @Test
    @DisplayName("Modificar el banco devuelto no debe afectar el banco interno (referencia) de la cuenta")
    void modificarBancoDevueltoNoDebeAfectarBancoInterno() {
      Banco bancoRecuperadoCopia = cuenta.getBanco();

      Cuenta nuevaCuentaEnCopia = new Cuenta("Nueva Cuenta en Copia", 0.0, bancoRecuperadoCopia);
      bancoRecuperadoCopia.agregarCuenta(nuevaCuentaEnCopia);

      Assertions.assertNotSame(bancoRecuperadoCopia, bancoDePruebaInicial, () -> "Las referencias del banco de la copia y el banco de prueba inicial deberían ser distintas.");

      Assertions.assertEquals(2, cuenta.getBanco().getCuentas().size(), () -> "El banco interno de la cuenta (referencia) no debe cambiar si la copia devuelta se modifica");

      Assertions.assertEquals(3, bancoRecuperadoCopia.getCuentas().size(), () -> "La copia devuelta debe tener 3 elementos después de añadir uno.");
    }
  }
}