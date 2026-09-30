package com.clinica_veterinaria.veterinaria.repository;

import com.clinica_veterinaria.veterinaria.entity.Duenio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IDuenioRepository extends JpaRepository<Duenio, Long> {

    Optional<Duenio> findByDni(String dni);
    boolean existsByDni(String dni);
    Optional<Duenio> findByUsuarioId(Long usuarioId);
    Optional<Duenio> findByUsuarioUsername(String username);
    List<Duenio> findByDniContaining(String dni);
}
