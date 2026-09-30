package com.clinica_veterinaria.veterinaria.repository;

import com.clinica_veterinaria.veterinaria.entity.Usuario;
import com.clinica_veterinaria.veterinaria.entity.enums.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);
    boolean existsByUsername(String username);
    List<Usuario> findByRol(Rol rol);
}
