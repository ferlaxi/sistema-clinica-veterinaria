package com.clinica_veterinaria.veterinaria.entity;

import com.clinica_veterinaria.veterinaria.entity.enums.Especie;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mascotas")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Especie especie;

    @Column(length = 80)
    private String raza;

    private Integer edad;

    @Column(length = 20)
    private String sexo;

    @Column(length = 500)
    private String observaciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "duenio_id", nullable = false)
    private Duenio duenio;

    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Turno> turnos = new ArrayList<>();
}
