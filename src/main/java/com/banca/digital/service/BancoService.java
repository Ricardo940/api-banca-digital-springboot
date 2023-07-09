package com.banca.digital.service;

import com.banca.digital.entity.CuentaAhorro;
import com.banca.digital.entity.CuentaBancaria;
import com.banca.digital.entity.CuentaCorriente;
import com.banca.digital.reposistory.CuentaBancariaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BancoService {

    @Autowired
    private CuentaBancariaRepository cuentaBancariaRepository;

    public void consultar(){
        CuentaBancaria cuentaBancariaBBDD = cuentaBancariaRepository.findById("4c40b880-b410-4f2d-9bf8-772a0505d9e1").orElse(null);
        if(cuentaBancariaBBDD != null){
            System.out.println("****************************");
            System.out.println("ID : " + cuentaBancariaBBDD.getId());
            System.out.println("Balance de la cuenta : " +cuentaBancariaBBDD.getBalance());
            System.out.println("Estado : " + cuentaBancariaBBDD.getEstadoCuenta());
            System.out.println("Fecha de creación : " + cuentaBancariaBBDD.getFechaCreacion());
            System.out.println("Cliente : " + cuentaBancariaBBDD.getCliente().getNombre());
            System.out.println("Nombre de la clase : " + cuentaBancariaBBDD.getClass().getName());
        }
        if(cuentaBancariaBBDD instanceof CuentaCorriente){
            System.out.println("Sobregiro : " +  ((CuentaCorriente) cuentaBancariaBBDD).getSobregiro());
        }
        if(cuentaBancariaBBDD instanceof CuentaAhorro){
            System.out.println("Tasa de interes : " + ((CuentaAhorro) cuentaBancariaBBDD).getTasaDeInteres());
        }

        cuentaBancariaBBDD.getOperacionesCuenta().forEach(operacionCuenta -> {
            System.out.println("----------------------------------");
            System.out.println("Tipo de operación : " + operacionCuenta.getTipoOperacion());
            System.out.println("Fecha de operación : " + operacionCuenta.getFechaOperacion());
            System.out.println("Monto : " + operacionCuenta.getMonto());
        });
    }
}
