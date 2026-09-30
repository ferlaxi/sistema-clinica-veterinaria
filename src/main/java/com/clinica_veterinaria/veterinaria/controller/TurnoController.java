package com.clinica_veterinaria.veterinaria.controller;

import com.clinica_veterinaria.veterinaria.dto.DuenioDto;
import com.clinica_veterinaria.veterinaria.dto.TurnoDto;
import com.clinica_veterinaria.veterinaria.dto.UsuarioDto;
import com.clinica_veterinaria.veterinaria.entity.enums.EstadoTurno;
import com.clinica_veterinaria.veterinaria.entity.enums.Rol;
import com.clinica_veterinaria.veterinaria.service.IDuenioService;
import com.clinica_veterinaria.veterinaria.service.IMascotaService;
import com.clinica_veterinaria.veterinaria.service.ITurnoService;
import com.clinica_veterinaria.veterinaria.service.IUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/turnos")
public class TurnoController {

    @Autowired
    private ITurnoService turnoService;

    @Autowired
    private IMascotaService mascotaService;

    @Autowired
    private IUsuarioService usuarioService;
    @Autowired
    private IDuenioService duenioService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO', 'CLIENTE')")
    public String listarTodos(Model model, Principal principal) {
        UsuarioDto usuario = usuarioService.obtenerPorUsername(principal.getName());
        List<TurnoDto> listaTurnos;
        if (usuario.getRol() == Rol.ROLE_CLIENTE) {
            DuenioDto duenio = duenioService.obtenerPorUsuarioUsername(principal.getName());
            listaTurnos = duenio != null ? turnoService.listarPorDuenio(duenio.getId()) : List.of();
        } else if (usuario.getRol() == Rol.ROLE_VETERINARIO) {
            listaTurnos = turnoService.listarPorVeterinario(usuario.getId());
        } else {
            listaTurnos = turnoService.listarTodos();
        }
        Map<String, List<TurnoDto>> turnosPorVeterinario = listaTurnos.stream()
            .collect(Collectors.groupingBy(t -> t.getNombreVeterinario() != null ? t.getNombreVeterinario() : "Sin Asignar"));
        model.addAttribute("turnosPorVeterinario", turnosPorVeterinario);
        model.addAttribute("listaTurnosJs", listaTurnos);
        return "turnos/lista";
    }

    @GetMapping("/nuevo")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'VETERINARIO')")
    public String mostrarFormularioCrear(Model model, Principal principal) {
        model.addAttribute("turno", new TurnoDto());
        cargarDatosFormulario(model, principal);
        return "turnos/formulario";
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'VETERINARIO')")
    public String guardar(@Valid @ModelAttribute("turno") TurnoDto dto, BindingResult result, Model model, Principal principal) {
        if (result.hasErrors()) {
            cargarDatosFormulario(model, principal);
            return "turnos/formulario";
        }
        UsuarioDto usuario = usuarioService.obtenerPorUsername(principal.getName());
        if (usuario.getRol() == Rol.ROLE_VETERINARIO) {
            dto.setVeterinarioId(usuario.getId());
        }
        if (dto.getId() == null) {
            turnoService.registrarTurno(dto);
        }
        return "redirect:/turnos";
    }

    @PostMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO')")
    public String cambiarEstado(@PathVariable Long id, @RequestParam("estado") EstadoTurno estado) {
        turnoService.cambiarEstado(id, estado);
        return "redirect:/turnos";
    }

    @PostMapping("/{id}/diagnostico")
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO')")
    public String actualizarDiagnostico(@PathVariable Long id, @RequestParam("diagnostico") String diagnostico) {
        turnoService.actualizarDiagnostico(id, diagnostico);
        return "redirect:/turnos";
    }

    private void cargarDatosFormulario(Model model, Principal principal) {
        UsuarioDto usuario = usuarioService.obtenerPorUsername(principal.getName());
        if (usuario.getRol() == Rol.ROLE_CLIENTE) {
            DuenioDto duenio = duenioService.obtenerPorUsuarioUsername(principal.getName());
            model.addAttribute("mascotas", duenio != null ? mascotaService.listarPorDuenio(duenio.getId()) : List.of());
        } else {
            model.addAttribute("mascotas", mascotaService.listarTodas());
        }
        List<UsuarioDto> veterinarios = usuarioService.listarTodos().stream()
                .filter(u -> u.getRol() == Rol.ROLE_VETERINARIO).collect(Collectors.toList());
        if (usuario.getRol() == Rol.ROLE_VETERINARIO) {
            model.addAttribute("veterinarios", List.of(usuario));
        } else {
            model.addAttribute("veterinarios", veterinarios);
        }
    }
}