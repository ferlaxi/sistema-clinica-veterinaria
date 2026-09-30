package com.clinica_veterinaria.veterinaria.repository;

import com.clinica_veterinaria.veterinaria.entity.Mascota;
import com.clinica_veterinaria.veterinaria.entity.enums.Especie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMascotaRepository extends JpaRepository<Mascota, Long> {

    List<Mascota> findByDuenioId(Long idDuenio);
    List<Mascota> findByEspecie(Especie especie);
}
