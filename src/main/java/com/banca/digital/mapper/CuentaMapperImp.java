package com.banca.digital.mapper;

import com.banca.digital.dto.CuentaAhorroDTO;
import com.banca.digital.dto.CuentaCorrienteDTO;
import com.banca.digital.dto.OperacionCuentaDTO;
import com.banca.digital.entity.CuentaAhorro;
import com.banca.digital.entity.CuentaCorriente;
import com.banca.digital.entity.OperacionCuenta;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CuentaMapperImp {

    @Autowired
    ClienteMapperImp clienteMapperImp;

    public CuentaAhorroDTO mappearDeCuentaAHorro(CuentaAhorro cuentaAhorro){
        CuentaAhorroDTO cuentaAhorroDTO = new CuentaAhorroDTO();
        BeanUtils.copyProperties(cuentaAhorro,cuentaAhorroDTO);
        cuentaAhorroDTO.setCliente(clienteMapperImp.mapearDeCliente(cuentaAhorro.getCliente()));
        cuentaAhorroDTO.setTipo(cuentaAhorro.getClass().getSimpleName());
        return cuentaAhorroDTO;
    }

    public CuentaAhorro mappearDeCuentaAhorroDTO(CuentaAhorroDTO cuentaAhorroDTO){
        CuentaAhorro cuentaAhorro = new CuentaAhorro();
        BeanUtils.copyProperties(cuentaAhorroDTO, cuentaAhorro);
        cuentaAhorro.setCliente(clienteMapperImp.mapearDeClienteDTO(cuentaAhorroDTO.getCliente()));
        return cuentaAhorro;
    }

    public CuentaCorrienteDTO mappearDeCuentaCorriente(CuentaCorriente cuentaCorriente){
        CuentaCorrienteDTO cuentaCorrienteDTO = new CuentaCorrienteDTO();
        BeanUtils.copyProperties(cuentaCorriente,cuentaCorrienteDTO);
        cuentaCorrienteDTO.setCliente(clienteMapperImp.mapearDeCliente(cuentaCorriente.getCliente()));
        cuentaCorrienteDTO.setTipo(cuentaCorriente.getClass().getSimpleName());
        return cuentaCorrienteDTO;
    }

    public CuentaCorriente mappearDeCuentaCorrienteDTO(CuentaCorrienteDTO cuentaCorrienteDTO){
        CuentaCorriente cuentaCorriente = new CuentaCorriente();
        BeanUtils.copyProperties(cuentaCorrienteDTO, cuentaCorriente);
        cuentaCorriente.setCliente(clienteMapperImp.mapearDeClienteDTO(cuentaCorrienteDTO.getCliente()));
        return cuentaCorriente;
    }

    public OperacionCuentaDTO mappearDeOperacionCuenta(OperacionCuenta operacionCuenta){
        OperacionCuentaDTO operacionCuentaDTO = new OperacionCuentaDTO();
        BeanUtils.copyProperties(operacionCuenta, operacionCuentaDTO);
        return operacionCuentaDTO;
    }

}
