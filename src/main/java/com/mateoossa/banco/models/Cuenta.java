package com.mateoossa.banco.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Cuenta {
    private String nombre;
    private double saldo;
    private Banco banco;

    public void credito(double monto) {
        this.saldo += monto;
    }

    public void debito(double monto) {
        this.saldo -= monto;
    }

}
