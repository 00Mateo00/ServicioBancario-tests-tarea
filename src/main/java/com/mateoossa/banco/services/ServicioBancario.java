package com.mateoossa.banco.services;

import com.mateoossa.banco.exceptions.SaldoInsuficienteException;
import com.mateoossa.banco.models.Cuenta;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ServicioBancario {
    public void depositar(Cuenta cuenta, Double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto a depositar debe ser positivo.");
        } else {
            cuenta.credito(monto);

        }
    }

    public void retirar(Cuenta cuenta, Double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto a retirar debe ser positivo.");
        } else if (monto > cuenta.getSaldo()) {
            throw new SaldoInsuficienteException("El saldo es insuficiente");
        } else {
            cuenta.debito(monto);
        }
    }

    public String consultarEstado(Cuenta cuenta) {
        return cuenta.toString();
    }

    public void transferir(Cuenta cuentaOriginen, Cuenta cuentaDestino, Double monto) {
        if (cuentaDestino == null || cuentaOriginen == null) {
            throw new IllegalArgumentException("Las cuentas no pueden ser nulas");
        }
        if (cuentaOriginen.hashCode() == cuentaDestino.hashCode()) {
            throw new IllegalArgumentException("No se puede transferir a la misma cuenta");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto a transferir debe ser positivo.");
        }
        try {
            this.retirar(cuentaOriginen, monto);
            this.depositar(cuentaDestino, monto);
        } catch (Exception e) {
            System.err.println("Error en la transferencia: " + e.getMessage());
        }
    }

}
