package com.banca.digital.reposistory;

import com.banca.digital.entity.CuentaBancaria;
import com.banca.digital.entity.OperacionCuenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuentaBancariaRepository  extends JpaRepository<CuentaBancaria, String> {

}
