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
    this.banco = banco;
  }


  public void credito(double monto) {
    this.saldo += monto;
  }

  public void debito(double monto) {
    this.saldo -= monto;
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
