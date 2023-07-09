package com.banca.digital.web;

import com.banca.digital.dto.ClienteDTO;
import com.banca.digital.entity.Cliente;
import com.banca.digital.exceptions.ClienteNotFoundException;
import com.banca.digital.reposistory.ClienteRepository;
import com.banca.digital.service.CuentaBancariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ClienteController {

    @Autowired
    private CuentaBancariaService cuentaBancariaService;
    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/clientes")
    public List<ClienteDTO> listarClientes(){
        return cuentaBancariaService.listClientes();
    }

    @GetMapping("clientes/{id}")
    public ClienteDTO getDatosCliente(@PathVariable(name = "id") long clienteId) throws ClienteNotFoundException{
        return  cuentaBancariaService.getCliente(clienteId);
    }

    @GetMapping("clientes/search")
    public List<ClienteDTO> buscarCliente(@RequestParam(name = "keyword" , defaultValue = "") String keyword){
        return cuentaBancariaService.searchClientes("%"+keyword+"%");
    }

    @PostMapping("/clientes")
    public  ClienteDTO guardarCliente( @RequestBody ClienteDTO clienteDTO){
        return  cuentaBancariaService.saveCliente(clienteDTO);
    }

    @PutMapping("/clientes/{id}")
    public ClienteDTO actualizarCliente(@PathVariable Long id, @RequestBody ClienteDTO clienteDTO) throws ClienteNotFoundException {
        clienteDTO.setId(id);
        return cuentaBancariaService.updateCliente(clienteDTO);
    }

    @DeleteMapping("/clientes/{id}")
    public void eliminarCliente(@PathVariable(name = "id") Long clienteId) throws ClienteNotFoundException{
        clienteRepository.deleteById(clienteId);
    }


}
