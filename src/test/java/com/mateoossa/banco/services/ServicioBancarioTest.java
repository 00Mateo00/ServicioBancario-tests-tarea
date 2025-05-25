package com.mateoossa.banco.services;

import com.mateoossa.banco.exceptions.SaldoInsuficienteException;
import com.mateoossa.banco.models.Banco;
import com.mateoossa.banco.models.Cuenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas de la clase ServicioBancario")
class ServicioBancarioTest {

  private Banco banco;
  private ServicioBancario servicioBancario;
  private Cuenta cuentaMateo;
  private Cuenta cuentaBelen;

  @BeforeEach
  void configurarEntornoDePrueba() {
    this.banco = Banco.newBanco("J.P. Morgan");
    this.servicioBancario = new ServicioBancario();
    this.cuentaMateo = new Cuenta("Mateo", 2000.0, banco);
    this.cuentaBelen = new Cuenta("Belen", 4000.0, banco);
  }

  @Nested
  @DisplayName("Pruebas del método depositar")
  class DepositarTests {
    @ParameterizedTest
    @CsvSource({
        "100.0",   // montoDeposito
        "500.0",
        "0.01",
        "1500.75"
    })
    @DisplayName("Debería aumentar el saldo de la cuenta al depositar un monto positivo")
    void deberiaAumentarSaldoAlDepositarMontoPositivo(double montoDeposito) {
      double saldoInicial = cuentaMateo.getSaldo(); // Se obtiene en cada ejecución
      double saldoEsperado = saldoInicial + montoDeposito;
      servicioBancario.depositar(cuentaMateo, montoDeposito);
      assertEquals(saldoEsperado, cuentaMateo.getSaldo(),
          () -> "El saldo de la cuenta debería aumentar después de un depósito positivo de " + montoDeposito + ".");
    }

    @Test
    @DisplayName("Debería lanzar IllegalArgumentException al intentar depositar un monto negativo")
    void deberiaLanzarExcepcionAlDepositarMontoNegativo() {
      double montoDeposito = -1000.0;
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> servicioBancario.depositar(cuentaMateo, montoDeposito), () -> "Se esperaba una IllegalArgumentException al depositar un monto negativo.");
      assertEquals("El monto a depositar debe ser positivo.", exception.getMessage(), () -> "El mensaje de la excepción para monto de depósito negativo debería ser correcto.");
    }

    @Test
    @DisplayName("Debería lanzar IllegalArgumentException al intentar depositar cero")
    void deberiaLanzarExcepcionAlDepositarCero() {
      double montoDeposito = 0.0;
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> servicioBancario.depositar(cuentaMateo, montoDeposito), () -> "Se esperaba una IllegalArgumentException al depositar cero.");
      assertEquals("El monto a depositar debe ser positivo.", exception.getMessage(), () -> "El mensaje de la excepción para depositar cero debería ser correcto.");
    }

    @Test
    @DisplayName("Debería lanzar NullPointerException al intentar depositar en una cuenta nula (comportamiento actual)")
    void deberiaLanzarNullPointerExceptionAlDepositarEnCuentaNula() {
      double montoDeposito = 500.0;
      Cuenta cuentaNula = null;
      assertThrows(NullPointerException.class, () -> servicioBancario.depositar(cuentaNula, montoDeposito), () -> "Se esperaba una NullPointerException al depositar en una cuenta nula con la implementación actual.");
    }
  }

  @Nested
  @DisplayName("Pruebas del método retirar")
  class RetirarTests {
    @Test
    @DisplayName("Debería disminuir el saldo de la cuenta al retirar un monto positivo con saldo suficiente")
    void deberiaDisminuirSaldoAlRetirarMontoPositivoConSaldoSuficiente() {
      double saldoInicial = cuentaMateo.getSaldo();
      double montoRetiro = 1000.0;
      double saldoEsperado = saldoInicial - montoRetiro;
      servicioBancario.retirar(cuentaMateo, montoRetiro);
      assertEquals(saldoEsperado, cuentaMateo.getSaldo(), () -> "El saldo de la cuenta debería disminuir después de un retiro positivo.");
    }

    @Test
    @DisplayName("Debería lanzar IllegalArgumentException al intentar retirar un monto negativo")
    void deberiaLanzarExcepcionAlRetirarMontoNegativo() {
      double montoRetiro = -1000.0;
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> servicioBancario.retirar(cuentaMateo, montoRetiro), () -> "Se esperaba una IllegalArgumentException al retirar un monto negativo.");
      assertEquals("El monto a retirar debe ser positivo.", exception.getMessage(), () -> "El mensaje de la excepción para monto de retiro negativo debería ser correcto.");
    }

    @Test
    @DisplayName("Debería lanzar IllegalArgumentException al intentar retirar cero")
    void deberiaLanzarExcepcionAlRetirarCero() {
      double montoRetiro = 0.0;
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> servicioBancario.retirar(cuentaMateo, montoRetiro), () -> "Se esperaba una IllegalArgumentException al retirar cero.");
      assertEquals("El monto a retirar debe ser positivo.", exception.getMessage(), () -> "El mensaje de la excepción para retirar cero debería ser correcto.");
    }

    @Test
    @DisplayName("Debería lanzar SaldoInsuficienteException al intentar retirar un monto mayor al saldo disponible")
    void deberiaLanzarSaldoInsuficienteExceptionAlRetirarSinSaldo() {
      double montoRetiro = 5000.0;
      double saldoAntesDeRetiro = cuentaMateo.getSaldo();
      SaldoInsuficienteException exception = assertThrows(SaldoInsuficienteException.class, () -> {
        servicioBancario.retirar(cuentaMateo, montoRetiro);
      }, () -> "Se esperaba una SaldoInsuficienteException al intentar retirar sin saldo suficiente.");
      assertEquals("El saldo es insuficiente", exception.getMessage(), () -> "El mensaje de la excepción de saldo insuficiente debería ser correcto.");
      assertEquals(saldoAntesDeRetiro, cuentaMateo.getSaldo(), () -> "El saldo de la cuenta no debería cambiar si la operación de retiro falla por saldo insuficiente.");
    }

    @Test
    @DisplayName("Debería dejar el saldo en cero al retirar exactamente el saldo disponible")
    void deberiaDejarSaldoEnCeroAlRetirarExactamenteElSaldo() {
      double saldoInicial = cuentaMateo.getSaldo();
      double montoRetiro = saldoInicial;
      double saldoEsperado = 0.0;
      servicioBancario.retirar(cuentaMateo, montoRetiro);
      assertEquals(saldoEsperado, cuentaMateo.getSaldo(), () -> "El saldo de la cuenta debería ser cero después de retirar el saldo completo.");
    }

    @Test
    @DisplayName("Debería lanzar NullPointerException al intentar retirar de una cuenta nula (comportamiento actual)")
    void deberiaLanzarNullPointerExceptionAlRetirarDeCuentaNula() {
      double montoRetiro = 500.0;
      Cuenta cuentaNula = null;
      assertThrows(NullPointerException.class, () -> servicioBancario.retirar(cuentaNula, montoRetiro), () -> "Se esperaba una NullPointerException al retirar de una cuenta nula con la implementación actual.");
    }
  }

  @Nested
  @DisplayName("Pruebas del método consultarEstado")
  class ConsultarEstadoTests {
    @Test
    @DisplayName("Debería retornar la representación en cadena de la cuenta")
    void deberiaRetornarRepresentacionEnCadenaDeLaCuenta() {
      String estadoEsperado = cuentaMateo.toString();
      String estadoActual = servicioBancario.consultarEstado(cuentaMateo);
      assertEquals(estadoEsperado, estadoActual, () -> "El estado consultado debería coincidir con la representación en cadena de la cuenta.");
    }

    @Test
    @DisplayName("Debería lanzar NullPointerException al intentar consultar estado de una cuenta nula (comportamiento actual)")
    void deberiaLanzarNullPointerExceptionAlConsultarEstadoDeCuentaNula() {
      Cuenta cuentaNula = null;
      assertThrows(NullPointerException.class, () -> servicioBancario.consultarEstado(cuentaNula), () -> "Se esperaba una NullPointerException al consultar estado de una cuenta nula con la implementación actual.");
    }
  }

  @Nested
  @DisplayName("Pruebas del método transferir")
  class TransferirTests {
    @Test
    @DisplayName("Debería transferir el monto correctamente entre dos cuentas con saldo suficiente")
    void deberiaTransferirMontoCorrectamenteEntreCuentas() {
      double saldoInicialMateo = cuentaMateo.getSaldo();
      double saldoInicialBelen = cuentaBelen.getSaldo();
      double montoTransferido = 1000.0;
      double saldoEsperadoMateo = saldoInicialMateo - montoTransferido;
      double saldoEsperadoBelen = saldoInicialBelen + montoTransferido;
      servicioBancario.transferir(cuentaMateo, cuentaBelen, montoTransferido);
      assertEquals(saldoEsperadoMateo, cuentaMateo.getSaldo(), () -> "El saldo de la cuenta de origen (Mateo) debería disminuir después de la transferencia.");
      assertEquals(saldoEsperadoBelen, cuentaBelen.getSaldo(), () -> "El saldo de la cuenta de destino (Belen) debería aumentar después de la transferencia.");
    }

    @Test
    @DisplayName("Debería lanzar IllegalArgumentException si la cuenta de destino es nula")
    void deberiaLanzarExcepcionSiCuentaDestinoEsNula() {
      double montoTransferido = 1000.0;
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> servicioBancario.transferir(cuentaMateo, null, montoTransferido), () -> "Se esperaba una IllegalArgumentException al transferir a una cuenta nula.");
      assertEquals("Las cuentas no pueden ser nulas", exception.getMessage(), () -> "El mensaje de la excepción para cuenta de destino nula debería ser correcto.");
    }

    @Test
    @DisplayName("Debería lanzar IllegalArgumentException si la cuenta de origen es nula")
    void deberiaLanzarExcepcionSiCuentaOrigenEsNula() {
      double montoTransferido = 1000.0;
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> servicioBancario.transferir(null, cuentaBelen, montoTransferido), () -> "Se esperaba una IllegalArgumentException al transferir desde una cuenta nula.");
      assertEquals("Las cuentas no pueden ser nulas", exception.getMessage(), () -> "El mensaje de la excepción para cuenta de origen nula debería ser correcto.");
    }

    @Test
    @DisplayName("Debería lanzar IllegalArgumentException si el monto a transferir es negativo")
    void deberiaLanzarExcepcionSiMontoEsNegativo() {
      double montoTransferido = -500.0;
      double saldoOriginalMateo = cuentaMateo.getSaldo();
      double saldoOriginalBelen = cuentaBelen.getSaldo();
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> servicioBancario.transferir(cuentaMateo, cuentaBelen, montoTransferido), () -> "Se esperaba una IllegalArgumentException al transferir un monto negativo.");
      assertEquals("El monto a transferir debe ser positivo.", exception.getMessage(), () -> "El mensaje de la excepción al transferir monto negativo debería ser el de depositar.");
      assertEquals(saldoOriginalMateo, cuentaMateo.getSaldo(), () -> "El saldo de la cuenta de origen no debería cambiar si la transferencia falla por monto negativo.");
      assertEquals(saldoOriginalBelen, cuentaBelen.getSaldo(), () -> "El saldo de la cuenta de destino no debería cambiar si la transferencia falla por monto negativo.");
    }

    @Test
    @DisplayName("Debería lanzar IllegalArgumentException al intentar transferir cero")
    void deberiaLanzarExcepcionAlTransferirCero() {
      double montoTransferido = 0.0;
      double saldoOriginalMateo = cuentaMateo.getSaldo();
      double saldoOriginalBelen = cuentaBelen.getSaldo();
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> servicioBancario.transferir(cuentaMateo, cuentaBelen, montoTransferido), () -> "Se esperaba una IllegalArgumentException al transferir cero.");
      assertEquals("El monto a transferir debe ser positivo.", exception.getMessage(), () -> "El mensaje de la excepción al transferir cero debería ser el de depositar.");
      assertEquals(saldoOriginalMateo, cuentaMateo.getSaldo(), () -> "El saldo de la cuenta de origen no debería cambiar si la transferencia falla por monto cero.");
      assertEquals(saldoOriginalBelen, cuentaBelen.getSaldo(), () -> "El saldo de la cuenta de destino no debería cambiar si la transferencia falla por monto cero.");
    }

    @Test
    @DisplayName("Debería lanzar SaldoInsuficienteException y no modificar saldos si la cuenta de origen no tiene saldo suficiente")
    void deberiaLanzarSaldoInsuficienteExceptionYNoModificarSaldosSiOrigenSinSaldo() {
      double montoTransferido = cuentaMateo.getSaldo() *2;
      double saldoOriginalMateo = cuentaMateo.getSaldo();
      double saldoOriginalBelen = cuentaBelen.getSaldo();

      SaldoInsuficienteException exception = assertThrows(SaldoInsuficienteException.class, () -> servicioBancario.transferir(cuentaMateo, cuentaBelen, montoTransferido), () -> "Se esperaba una SaldoInsuficienteException al intentar transferir sin saldo suficiente.");

      assertEquals("El saldo es insuficiente", exception.getMessage(), () -> "El mensaje de la excepción de saldo insuficiente debería ser correcto.");

      assertEquals(saldoOriginalMateo, cuentaMateo.getSaldo(), () -> "El saldo de la cuenta de origen no debería cambiar si la transferencia falla.");
      assertEquals(saldoOriginalBelen, cuentaBelen.getSaldo(), () -> "El saldo de la cuenta de destino no debería cambiar si la transferencia falla.");
    }

    @Test
    @DisplayName("Debería lanzar IllegalArgumentException si las cuentas de origen y destino son la misma")
    void deberiaLanzarExcepcionSiCuentasSonLaMisma() {
      double montoTransferido = 100.0;
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> servicioBancario.transferir(cuentaMateo, cuentaMateo, montoTransferido), () -> "Se esperaba una IllegalArgumentException al transferir entre la misma cuenta.");
      assertEquals("No se puede transferir a la misma cuenta", exception.getMessage(), () -> "El mensaje de la excepción para cuentas de origen y destino iguales debería ser correcto.");
    }
  }
}