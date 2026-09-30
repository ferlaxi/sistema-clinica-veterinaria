package com.clinica_veterinaria.veterinaria.config;

import com.clinica_veterinaria.veterinaria.entity.Usuario;
import com.clinica_veterinaria.veterinaria.entity.enums.Rol;
import com.clinica_veterinaria.veterinaria.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminUsername = "admin";
        String adminPassword = "admin";

        boolean existeAdmin = usuarioRepository.existsByUsername(adminUsername);

        if (!existeAdmin) {
            Usuario usuarioAdmin = Usuario.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .rol(Rol.ROLE_ADMIN)
                    .activo(true)
                    .build();

            usuarioRepository.save(usuarioAdmin);
        }
    }
}
