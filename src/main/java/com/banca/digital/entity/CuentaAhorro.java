package com.banca.digital.entity;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@DiscriminatorValue("SA")
@NoArgsConstructor
@AllArgsConstructor
public class CuentaAhorro extends CuentaBancaria {
    private double tasaDeInteres;
}
