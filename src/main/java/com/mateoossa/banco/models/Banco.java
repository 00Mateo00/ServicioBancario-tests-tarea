  package com.mateoossa.banco.models;

  import java.util.ArrayList;
  import java.util.List;
  import java.util.Objects;

  import lombok.EqualsAndHashCode;
  import lombok.Getter;
  import lombok.Setter;

  @Getter
  @Setter
  @EqualsAndHashCode
  public class Banco {

    private String nombre;
    private List<Cuenta> cuentas;

    private Banco(String nombre) {
      this.nombre = nombre;
      this.cuentas = new ArrayList<>();
    }

    public Banco (Banco banco){
      this.nombre = banco.nombre;
      this.cuentas = new ArrayList<>(banco.cuentas);
    }

    public static Banco newBanco(String nombre) {
      if (nombre == null || nombre.trim().isEmpty()) {
        throw new IllegalArgumentException("El nombre del banco no puede ser nulo o vacío.");
      }
      return new Banco(nombre);
    }

    public static Banco crearCopia(Banco original) {
      Objects.requireNonNull(original, "Original Banco cannot be null for copying.");
      return new Banco(original);
    }

    public void agregarCuenta(Cuenta cuenta) {
      if (cuenta == null) {
        throw new IllegalArgumentException("La cuenta no puede ser nula.");
      }
      if (this.cuentas.contains(cuenta)) {
        throw new IllegalArgumentException("La cuenta ya está registrada en el banco.");
      }

      this.cuentas.add(cuenta);
    }

    public List<Cuenta> getCuentas() {
      return new ArrayList<>(this.cuentas);
    }

    public void setCuentas(List<Cuenta> nuevasCuentas) {
      if (nuevasCuentas != null) {
        this.cuentas = new ArrayList<>(nuevasCuentas);
      } else {
        this.cuentas = new ArrayList<>();
      }
    }

  }
