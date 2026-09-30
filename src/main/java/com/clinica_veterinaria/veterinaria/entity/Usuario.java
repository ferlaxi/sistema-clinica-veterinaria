package com.clinica_veterinaria.veterinaria.entity;

import com.clinica_veterinaria.veterinaria.entity.enums.Rol;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Rol rol;

    @Builder.Default
    @Column(nullable = false)
    private boolean activo = true;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Duenio duenio;
}
