package com.banca.digital.web;

import com.banca.digital.dto.*;
import com.banca.digital.entity.CuentaBancaria;
import com.banca.digital.exceptions.BalanceInsuficienteException;
import com.banca.digital.exceptions.CuentaBancariaNotFoundException;
import com.banca.digital.service.CuentaBancariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CuentaBancariaController {

    @Autowired
    CuentaBancariaService cuentaBancariaService;

    @GetMapping("/cuentas/{id}")
    public CuentaBancariaDTO obtenerCuentaBancaria(@PathVariable String id) throws CuentaBancariaNotFoundException {
        return cuentaBancariaService.getCuentaBancaria(id);
    }

    @GetMapping("/cuentas")
    public List<CuentaBancariaDTO> listarCuentasBancarias(){
        return cuentaBancariaService.listCuentasBancarias();
    }

    @GetMapping("/cuentas/{cuentaId}/operaciones")
    public List<OperacionCuentaDTO> obtenerHistorialCuenta(@PathVariable String cuentaId){
        return cuentaBancariaService.listHistorialCuenta(cuentaId);
    }

    @GetMapping("/cuentas/{cuentaId}/pageOperaciones")
    public HistorialCuentaDTO obtenerHistorialCuentaPaginada(@PathVariable String cuentaId, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "5") int size) throws CuentaBancariaNotFoundException {
        return cuentaBancariaService.getHistorialCuenta(cuentaId, page, size);
    }

    @PostMapping("/cuentas/debito")
    public DebitoDTO realizarDebito(@RequestBody DebitoDTO debitoDTO) throws CuentaBancariaNotFoundException, BalanceInsuficienteException {
        cuentaBancariaService.debit(debitoDTO.getCuentaId(), debitoDTO.getMonto(), debitoDTO.getDescripcion());
        return debitoDTO;
    }

    @PostMapping("/cuentas/credito")
    public CreditoDTO realizarCredito(@RequestBody CreditoDTO creditoDTO) throws CuentaBancariaNotFoundException {
        cuentaBancariaService.credit(creditoDTO.getCuentaId(), creditoDTO.getMonto(), creditoDTO.getDescripcion());
        return creditoDTO;
    }

    @PostMapping("cuentas/transferencia")
    public void realizarTransferencia(@RequestBody TransferenciaRequestDTO transferenciaRequestDTO) throws CuentaBancariaNotFoundException, BalanceInsuficienteException {
        cuentaBancariaService.transfer(transferenciaRequestDTO.getCuentaPropietario(), transferenciaRequestDTO.getCuentaDestinatario(), transferenciaRequestDTO.getMonto());
    }



}
