package com.clinica_veterinaria.veterinaria.service;

import com.clinica_veterinaria.veterinaria.dto.UsuarioDto;
import com.clinica_veterinaria.veterinaria.entity.Turno;
import com.clinica_veterinaria.veterinaria.entity.Usuario;
import com.clinica_veterinaria.veterinaria.entity.Duenio;
import com.clinica_veterinaria.veterinaria.entity.enums.Rol;
import com.clinica_veterinaria.veterinaria.repository.IDuenioRepository;
import com.clinica_veterinaria.veterinaria.repository.ITurnoRepository;
import com.clinica_veterinaria.veterinaria.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private IUsuarioRepository iUsuarioRepository;
    @Autowired
    private ITurnoRepository turnoRepository;

    @Autowired
    private IDuenioRepository duenioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDto> listarTodos() {
        return iUsuarioRepository.findAll().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDto obtenerPorId(Long id) {
        Usuario usuario = iUsuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        return convertirADto(usuario);
    }
    @Override
    @Transactional(readOnly = true)
    public UsuarioDto obtenerPorUsername(String username) {
        Usuario usuario = iUsuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
        return convertirADto(usuario);
    }

    @Override
    @Transactional
    public UsuarioDto registrar(UsuarioDto dto) {
        if (iUsuarioRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .rol(Rol.ROLE_CLIENTE) 
                .activo(true)
                .build();

        return convertirADto(iUsuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public UsuarioDto registrarPersonal(UsuarioDto dto) {
        if (iUsuarioRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .rol(dto.getRol()) 
                .activo(true)
                .build();

        return convertirADto(iUsuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public UsuarioDto actualizarPersonal(Long id, UsuarioDto dto) {
        Usuario usuario = iUsuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        if (!usuario.getUsername().equals(dto.getUsername()) && iUsuarioRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe en otra cuenta: " + dto.getUsername());
        }
        usuario.setUsername(dto.getUsername());
        usuario.setRol(dto.getRol());
        usuario.setActivo(dto.isActivo());

        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return convertirADto(iUsuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = iUsuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        List<Turno> turnosAsignados = turnoRepository.findByVeterinarioId(id);
        for (Turno turno : turnosAsignados) {
            turno.setVeterinario(null);
            turnoRepository.save(turno);
        }

        if (usuario.getDuenio() != null) {
            Duenio d = usuario.getDuenio();
            d.setUsuario(null);
            duenioRepository.save(d); 
            usuario.setDuenio(null);
        }

        iUsuarioRepository.delete(usuario);
    }

    private UsuarioDto convertirADto(Usuario usuario) {
        return UsuarioDto.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .rol(usuario.getRol())
                .activo(usuario.isActivo())
                .build();
    }
}
