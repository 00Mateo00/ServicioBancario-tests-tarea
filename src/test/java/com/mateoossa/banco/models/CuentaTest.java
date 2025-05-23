package com.mateoossa.banco.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
    @DisplayName("Debe establecer un banco no nulo y ser una copia defensiva")
    void debeEstablecerBancoNoNuloYCopiaDefensiva() {
      Banco nuevoBancoExterno = Banco.newBanco("Nuevo Banco Externo");

      cuenta.setBanco(nuevoBancoExterno);

      Assertions.assertNotNull(cuenta.getBanco());
      Assertions.assertNotSame(nuevoBancoExterno, cuenta.getBanco());
      Assertions.assertEquals(nuevoBancoExterno.getNombre(), cuenta.getBanco().getNombre());
    }

    @Test
    @DisplayName("Debe establecer un banco nulo")
    void debeEstablecerBancoNulo() {
      cuenta.setBanco(null);
      Assertions.assertNull(cuenta.getBanco());
    }

    @Test
    @DisplayName("Modificar el banco original (externo) no debe afectar el banco interno de la cuenta")
    void modificarBancoOriginalNoDebeAfectarCuentaBanco() {
      Banco bancoModificableExterno = Banco.newBanco("Banco Modificable Externo");

      Cuenta cuentaInternaParaBancoExterno = new Cuenta("Cuenta Externa", 0.0, bancoModificableExterno);
      bancoModificableExterno.agregarCuenta(cuentaInternaParaBancoExterno);

      cuenta.setBanco(bancoModificableExterno);

      Cuenta segundaCuentaInterna = new Cuenta("Otra Cuenta Externa", 0.0, bancoModificableExterno);
      bancoModificableExterno.agregarCuenta(segundaCuentaInterna);

      Assertions.assertEquals(1, cuenta.getBanco().getCuentas().size());
      Assertions.assertEquals("Cuenta Externa", cuenta.getBanco().getCuentas().get(0).getNombre());
      Assertions.assertEquals(2, bancoModificableExterno.getCuentas().size());
    }
  }

  @Nested
  @DisplayName("pruebas del método getBanco")
  class GetBancoTests {

    @Test
    @DisplayName("Debe devolver una copia no nula del banco interno")
    void debeDevolverCopiaNoNulaDeBancoInterno() {
      Banco bancoRecuperado = cuenta.getBanco();

      Assertions.assertNotNull(bancoRecuperado);
      Assertions.assertNotSame(bancoDePruebaInicial, bancoRecuperado);
      Assertions.assertEquals(bancoDePruebaInicial.getNombre(), bancoRecuperado.getNombre());
    }

    @Test
    @DisplayName("Debe devolver nulo si el banco interno es nulo")
    void debeDevolverNuloSiBancoInternoEsNulo() {
      Cuenta cuentaSinBanco = new Cuenta("Cuenta sin Banco", 0.0, null);
      Assertions.assertNull(cuentaSinBanco.getBanco());
    }

    @Test
    @DisplayName("Modificar el banco devuelto no debe afectar el banco interno de la cuenta")
    void modificarBancoDevueltoNoDebeAfectarBancoInterno() {
      Banco bancoRecuperadoCopia = cuenta.getBanco();
      Cuenta nuevaCuentaEnCopia = new Cuenta("Nueva Cuenta en Copia", 0.0, bancoRecuperadoCopia);
      bancoRecuperadoCopia.agregarCuenta(nuevaCuentaEnCopia);

      Assertions.assertEquals(1, cuenta.getBanco().getCuentas().size(),
          "El banco interno de la cuenta no debe cambiar si la copia devuelta se modifica");
      Assertions.assertEquals("Cuenta test1", cuenta.getBanco().getCuentas().get(0).getNombre());

      Assertions.assertEquals(2, bancoRecuperadoCopia.getCuentas().size(),
          "La copia devuelta debe tener los dos elementos.");
    }
  }
}