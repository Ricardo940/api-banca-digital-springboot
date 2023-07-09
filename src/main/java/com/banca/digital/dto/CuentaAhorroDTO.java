package com.banca.digital.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CuentaAhorroDTO extends CuentaBancariaDTO{

    private String id;
    private double balance;
    private Date fechaCreacion;
    private ClienteDTO cliente;
    private double tasaDeInteres;

}
