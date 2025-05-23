package com.mateoossa.banco.models;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
public class Cuenta {

  private String nombre;
  private double saldo;
  private Banco banco;

  public Cuenta(String nombre, double saldo, Banco banco) {
    this.nombre = nombre;
    this.saldo = saldo;
    this.banco = makeBanco(banco);
  }


  public void credito(double monto) {
    this.saldo += monto;
  }

  public void debito(double monto) {
    this.saldo -= monto;
  }

  private Banco makeBanco(Banco banco) {
    if (banco != null) {
      return Banco.crearCopia(banco);
    }
    return null;
  }


  public void setBanco(Banco banco) {
    this.banco = makeBanco(banco);
  }

  public Banco getBanco() {
    if (this.banco == null) {
      return null;
    }
    return Banco.crearCopia(this.banco);
  }




  @Override

  public String toString() {
    return this.nombre + " " + this.saldo + " " + this.banco.getNombre();
  }


}
