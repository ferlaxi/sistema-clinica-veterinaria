package com.clinica_veterinaria.veterinaria.repository;

import com.clinica_veterinaria.veterinaria.entity.Turno;
import com.clinica_veterinaria.veterinaria.entity.enums.EstadoTurno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ITurnoRepository extends JpaRepository<Turno, Long> {

    List<Turno> findByMascotaId(Long mascotaId);
    List<Turno> findByMascotaDuenioId(Long idDuenio);
    List<Turno> findByVeterinarioId(Long veterinarioId);
    List<Turno> findByEstado(EstadoTurno estado);
    List<Turno> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
}
