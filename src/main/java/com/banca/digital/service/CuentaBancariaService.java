package com.banca.digital.service;

import com.banca.digital.dto.*;
import com.banca.digital.entity.Cliente;
import com.banca.digital.entity.CuentaAhorro;
import com.banca.digital.entity.CuentaBancaria;
import com.banca.digital.entity.CuentaCorriente;
import com.banca.digital.exceptions.BalanceInsuficienteException;
import com.banca.digital.exceptions.ClienteNotFoundException;
import com.banca.digital.exceptions.CuentaBancariaNotFoundException;

import java.util.List;

public interface CuentaBancariaService {

    ClienteDTO saveCliente(ClienteDTO clienteDTO);

    ClienteDTO getCliente(Long clienteId) throws ClienteNotFoundException;

    ClienteDTO updateCliente(ClienteDTO clienteDTO) throws  ClienteNotFoundException;

    void deleteCliente(Long clienteId) throws ClienteNotFoundException;

    List<ClienteDTO> listClientes();

    List<ClienteDTO> searchClientes(String keyword);

    CuentaCorrienteDTO saveCuentaBancariaCorriente(double balanceInicial, double sobregiro, Long clienteId) throws ClienteNotFoundException;

    CuentaAhorroDTO saveCuentaBancariaAhorro(double balanceInicial, double tasaInteres, Long clienteId) throws ClienteNotFoundException;

    CuentaBancariaDTO getCuentaBancaria(String cuentaId) throws CuentaBancariaNotFoundException;

    void debit(String cuentaId, double monto, String descripcion) throws CuentaBancariaNotFoundException, BalanceInsuficienteException;

    void credit(String cuentaId, double monto, String descripcion) throws CuentaBancariaNotFoundException;

    void transfer(String cuentaIdpropietario, String cuentaIdDestinatario, double monto) throws CuentaBancariaNotFoundException, BalanceInsuficienteException;

    List<CuentaBancariaDTO> listCuentasBancarias();

    List<OperacionCuentaDTO> listHistorialCuenta(String cuentaId);

    HistorialCuentaDTO getHistorialCuenta(String cuentaId, int page, int size) throws CuentaBancariaNotFoundException;

}
