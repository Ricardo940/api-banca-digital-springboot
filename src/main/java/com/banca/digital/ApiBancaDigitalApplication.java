package com.banca.digital;

import com.banca.digital.dto.ClienteDTO;
import com.banca.digital.dto.CuentaAhorroDTO;
import com.banca.digital.dto.CuentaBancariaDTO;
import com.banca.digital.dto.CuentaCorrienteDTO;
import com.banca.digital.entity.*;
import com.banca.digital.enums.EstadoCuenta;
import com.banca.digital.enums.TipoOperacion;
import com.banca.digital.exceptions.ClienteNotFoundException;
import com.banca.digital.exceptions.CuentaBancariaNotFoundException;
import com.banca.digital.reposistory.ClienteRepository;
import com.banca.digital.reposistory.CuentaBancariaRepository;
import com.banca.digital.reposistory.OperacionCuentaRepository;
import com.banca.digital.service.BancoService;
import com.banca.digital.service.CuentaBancariaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class ApiBancaDigitalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiBancaDigitalApplication.class, args);
	}

	//@Bean
	CommandLineRunner commandLineRunner(BancoService bancoService){
		return args -> {
			bancoService.consultar();
		};
	}
	//@Bean
	CommandLineRunner start (CuentaBancariaService cuentaBancariaService){
		return args -> {
			Stream.of("Ricardo", "Maye", "Zeus", "Monroe").forEach(nombre -> {
				ClienteDTO cliente = new ClienteDTO();
				cliente.setNombre(nombre);
				cliente.setEmail(nombre+"@gmail.com");
				cuentaBancariaService.saveCliente(cliente);
			});

			cuentaBancariaService.listClientes().forEach(cliente -> {
				try{
					cuentaBancariaService.saveCuentaBancariaCorriente(Math.random()*90000, 9000, cliente.getId());
					cuentaBancariaService.saveCuentaBancariaAhorro(Math.random()*100000, 5.5, cliente.getId());

					List<CuentaBancariaDTO> cuentasBancarias = cuentaBancariaService.listCuentasBancarias();

					for(CuentaBancariaDTO cuentaBancaria: cuentasBancarias){
						for (int i = 0; i < 10; i++) {
							String cuentaID;

							if(cuentaBancaria instanceof CuentaAhorroDTO){
								cuentaID = ((CuentaAhorroDTO) cuentaBancaria).getId();
							}else {
								cuentaID = ((CuentaCorrienteDTO) cuentaBancaria).getId();
							}
							cuentaBancariaService.credit(cuentaID,Math.random()*120000 + 1000, "Credito");
							cuentaBancariaService.debit(cuentaID,Math.random()*120000 + 1000, "Debito");

						}
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			});
		};
	}


}
