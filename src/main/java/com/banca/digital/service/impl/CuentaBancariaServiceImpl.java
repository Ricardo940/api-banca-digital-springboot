package com.banca.digital.service.impl;

import com.banca.digital.dto.*;
import com.banca.digital.entity.*;
import com.banca.digital.enums.EstadoCuenta;
import com.banca.digital.enums.TipoOperacion;
import com.banca.digital.exceptions.BalanceInsuficienteException;
import com.banca.digital.exceptions.ClienteNotFoundException;
import com.banca.digital.exceptions.CuentaBancariaNotFoundException;
import com.banca.digital.mapper.ClienteMapperImp;
import com.banca.digital.mapper.CuentaMapperImp;
import com.banca.digital.reposistory.ClienteRepository;
import com.banca.digital.reposistory.CuentaBancariaRepository;
import com.banca.digital.reposistory.OperacionCuentaRepository;
import com.banca.digital.service.CuentaBancariaService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CuentaBancariaServiceImpl implements CuentaBancariaService {

    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    CuentaBancariaRepository cuentaBancariaRepository;
    @Autowired
    OperacionCuentaRepository operacionCuentaRepository;
    @Autowired
    ClienteMapperImp clienteMapperImp;
    @Autowired
    CuentaMapperImp cuentaMapperImp;

    @Override
    public ClienteDTO saveCliente(ClienteDTO clienteDTO) {
        log.info("Guardando un nuevo cliente");
        Cliente cliente = clienteMapperImp.mapearDeClienteDTO(clienteDTO);
        Cliente clienteBBDD = clienteRepository.save(cliente);
        return clienteMapperImp.mapearDeCliente(clienteBBDD);
    }

    @Override
    public ClienteDTO getCliente(Long clienteId) throws ClienteNotFoundException {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado"));
        return clienteMapperImp.mapearDeCliente(cliente);
    }

    @Override
    public ClienteDTO updateCliente(ClienteDTO clienteDTO) throws ClienteNotFoundException {
        log.info("Actualizando cliente");
        Cliente cliente = clienteMapperImp.mapearDeClienteDTO(clienteDTO);
        Cliente clienteBBDD = clienteRepository.save(cliente);
        return clienteMapperImp.mapearDeCliente(clienteBBDD);
    }

    @Override
    public void deleteCliente(Long clienteId) throws ClienteNotFoundException {
        clienteRepository.deleteById(clienteId);
    }

    @Override
    public List<ClienteDTO> listClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        List<ClienteDTO> clienteDTOS = clientes.stream()
                .map(cliente -> clienteMapperImp.mapearDeCliente(cliente))
                .collect(Collectors.toList());
        return clienteDTOS;
    }

    @Override
    public List<ClienteDTO> searchClientes(String keyword) {
        List<Cliente> clientes = clienteRepository.searchClientes(keyword);
        List<ClienteDTO> clientesDTOS = clientes.stream().map(cliente -> clienteMapperImp.mapearDeCliente(cliente)).collect(Collectors.toList());
        return clientesDTOS;
    }

    @Override
    public CuentaCorrienteDTO saveCuentaBancariaCorriente(double balanceInicial, double sobregiro, Long clienteId) throws ClienteNotFoundException {
        Cliente cliente = clienteRepository.findById(clienteId).orElse(null);

        if(cliente == null){
            throw new ClienteNotFoundException("Cliente no encontrado");
        }

        CuentaCorriente cuentaCorriente = new CuentaCorriente();
        cuentaCorriente.setId(UUID.randomUUID().toString());
        cuentaCorriente.setFechaCreacion(new Date());
        cuentaCorriente.setBalance(balanceInicial);
        cuentaCorriente.setSobregiro(sobregiro);
        cuentaCorriente.setCliente(cliente);
        cuentaCorriente.setEstadoCuenta(EstadoCuenta.CREADA);

        CuentaCorriente cuentaCorrienteBBDD = cuentaBancariaRepository.save(cuentaCorriente);

        return cuentaMapperImp.mappearDeCuentaCorriente(cuentaCorrienteBBDD);
    }

    @Override
    public CuentaAhorroDTO saveCuentaBancariaAhorro(double balanceInicial, double tasaInteres, Long clienteId) throws ClienteNotFoundException {
        Cliente cliente = clienteRepository.findById(clienteId).orElse(null);

        if(cliente == null){
            throw new ClienteNotFoundException("Cliente no encontrado");
        }

        CuentaAhorro cuentaAhorro = new CuentaAhorro();
        cuentaAhorro.setId(UUID.randomUUID().toString());
        cuentaAhorro.setFechaCreacion(new Date());
        cuentaAhorro.setBalance(balanceInicial);
        cuentaAhorro.setTasaDeInteres(tasaInteres);
        cuentaAhorro.setCliente(cliente);
        cuentaAhorro.setEstadoCuenta(EstadoCuenta.CREADA);

        CuentaAhorro cuentaAhorroBBDD = cuentaBancariaRepository.save(cuentaAhorro);

        return cuentaMapperImp.mappearDeCuentaAHorro(cuentaAhorroBBDD);
    }



    @Override
    public CuentaBancariaDTO getCuentaBancaria(String cuentaId) throws CuentaBancariaNotFoundException {
        CuentaBancaria cuentaBancaria = cuentaBancariaRepository.findById(cuentaId).orElseThrow(()->
            new CuentaBancariaNotFoundException("Cuenta bancaria no encontrada"));
        if(cuentaBancaria instanceof CuentaAhorro){
            CuentaAhorro cuentaAhorro = (CuentaAhorro) cuentaBancaria;
            return cuentaMapperImp.mappearDeCuentaAHorro(cuentaAhorro);
        }else {
            CuentaCorriente cuentaCorriente = (CuentaCorriente) cuentaBancaria;
            return cuentaMapperImp.mappearDeCuentaCorriente(cuentaCorriente);
        }
    }

    @Override
    public void debit(String cuentaId, double monto, String descripcion) throws CuentaBancariaNotFoundException, BalanceInsuficienteException {

        CuentaBancaria cuentaBancaria = cuentaBancariaRepository.findById(cuentaId).orElseThrow(()->
                new CuentaBancariaNotFoundException("Cuenta bancaria no encontrada"));

        if(cuentaBancaria.getBalance()<monto){
            throw new BalanceInsuficienteException("Balance insuficiente");
        }

        OperacionCuenta operacionCuenta = new OperacionCuenta();
        operacionCuenta.setTipoOperacion(TipoOperacion.DEBITO);
        operacionCuenta.setFechaOperacion(new Date());
        operacionCuenta.setMonto(monto);
        operacionCuenta.setDescripcion(descripcion);
        operacionCuenta.setCuentaBancaria(cuentaBancaria);

        operacionCuentaRepository.save(operacionCuenta);
        cuentaBancaria.setBalance(cuentaBancaria.getBalance() - monto);
        cuentaBancariaRepository.save(cuentaBancaria);

    }

    @Override
    public void credit(String cuentaId, double monto, String descripcion) throws CuentaBancariaNotFoundException {
        CuentaBancaria cuentaBancaria = cuentaBancariaRepository.findById(cuentaId)
                .orElseThrow(()-> new CuentaBancariaNotFoundException("Cuenta bancaria no encontrada"));

        OperacionCuenta operacionCuenta = new OperacionCuenta();
        operacionCuenta.setTipoOperacion(TipoOperacion.CREDITO);
        operacionCuenta.setFechaOperacion(new Date());
        operacionCuenta.setMonto(monto);
        operacionCuenta.setDescripcion(descripcion);
        operacionCuenta.setCuentaBancaria(cuentaBancaria);

        operacionCuentaRepository.save(operacionCuenta);
        cuentaBancaria.setBalance(cuentaBancaria.getBalance() + monto);
        cuentaBancariaRepository.save(cuentaBancaria);
    }

    @Override
    public void transfer(String cuentaIdpropietario, String cuentaIdDestinatario, double monto) throws CuentaBancariaNotFoundException, BalanceInsuficienteException {
        debit(cuentaIdpropietario, monto, "Transferenci a : " + cuentaIdDestinatario);
        credit(cuentaIdDestinatario, monto, "Transferencia de : " +  cuentaIdpropietario);
    }

    @Override
    public List<CuentaBancariaDTO> listCuentasBancarias() {
        List<CuentaBancaria> cuentasBancarias = cuentaBancariaRepository.findAll();
        List<CuentaBancariaDTO> cuentasBancariaDTOS = cuentasBancarias.stream().map(cuentaBancaria -> {
            if(cuentaBancaria instanceof CuentaAhorro){
                CuentaAhorro cuentaAhorro = (CuentaAhorro) cuentaBancaria;
                return cuentaMapperImp.mappearDeCuentaAHorro(cuentaAhorro);
            }else {
                CuentaCorriente cuentaCorriente = (CuentaCorriente) cuentaBancaria;
                return cuentaMapperImp.mappearDeCuentaCorriente(cuentaCorriente);
            }
        }).collect(Collectors.toList());
        return cuentasBancariaDTOS;
    }

    @Override
    public List<OperacionCuentaDTO> listHistorialCuenta(String cuentaId) {
        List<OperacionCuenta> operacionesDeCuenta = operacionCuentaRepository.findByCuentaBancariaId(cuentaId);
        return operacionesDeCuenta.stream().map(operacionCuenta ->
                        cuentaMapperImp.mappearDeOperacionCuenta(operacionCuenta)
                ).collect(Collectors.toList());
    }

    @Override
    public HistorialCuentaDTO getHistorialCuenta(String cuentaId, int page, int size) throws CuentaBancariaNotFoundException {
        CuentaBancaria cuentaBancaria = cuentaBancariaRepository.findById(cuentaId)
                .orElse(null);

        if(cuentaBancaria == null){
            throw new CuentaBancariaNotFoundException("Cuenta no encontrada");
        }

        Page<OperacionCuenta> operacionesCuenta = operacionCuentaRepository.findByCuentaBancariaIdOrderByFechaOperacionDesc(cuentaId, PageRequest.of(page,size));
        HistorialCuentaDTO historialCuentaDTO = new HistorialCuentaDTO();
        List<OperacionCuentaDTO> operacionCuentasDTO = operacionesCuenta.getContent().stream().map(operacionCuenta -> cuentaMapperImp.mappearDeOperacionCuenta(operacionCuenta)).collect(Collectors.toList());
        historialCuentaDTO.setOperacionCuentaDTOS(operacionCuentasDTO);
        historialCuentaDTO.setCuentaId(cuentaBancaria.getId());
        historialCuentaDTO.setBalance(cuentaBancaria.getBalance());
        historialCuentaDTO.setCurrentPage(page);
        historialCuentaDTO.setPageSize(size);
        historialCuentaDTO.setTotalPage(operacionesCuenta.getTotalPages());
        return  historialCuentaDTO;
    }


}
