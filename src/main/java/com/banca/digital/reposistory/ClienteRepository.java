package com.banca.digital.reposistory;

import com.banca.digital.dto.ClienteDTO;
import com.banca.digital.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("SELECT c FROM Cliente c where c.nombre LIKE :kw")
    List<Cliente> searchClientes(@Param(value = "kw") String keyword);
}
