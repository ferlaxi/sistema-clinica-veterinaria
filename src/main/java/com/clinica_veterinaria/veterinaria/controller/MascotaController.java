package com.clinica_veterinaria.veterinaria.controller;

import com.clinica_veterinaria.veterinaria.dto.DuenioDto;
import com.clinica_veterinaria.veterinaria.dto.MascotaDto;
import com.clinica_veterinaria.veterinaria.dto.UsuarioDto;
import com.clinica_veterinaria.veterinaria.entity.enums.Rol;
import com.clinica_veterinaria.veterinaria.service.IDuenioService;
import com.clinica_veterinaria.veterinaria.service.IMascotaService;
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

@Controller
@RequestMapping("/mascotas")
public class MascotaController {

    @Autowired
    private IMascotaService mascotaService;

    @Autowired
    private IDuenioService duenioService;
    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO', 'CLIENTE')")
    public String listarTodas(Model model, Principal principal) {
        UsuarioDto usuario = usuarioService.obtenerPorUsername(principal.getName());
        if (usuario.getRol() == Rol.ROLE_CLIENTE) {
            DuenioDto duenio = duenioService.obtenerPorUsuarioUsername(principal.getName());
            model.addAttribute("mascotas", duenio != null ? mascotaService.listarPorDuenio(duenio.getId()) : List.of());
        } else {
            model.addAttribute("mascotas", mascotaService.listarTodas());
        }
        return "mascotas/lista";
    }

    @GetMapping("/nueva")
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO', 'CLIENTE')")
    public String mostrarFormularioCrear(Model model, Principal principal) {
        model.addAttribute("mascota", new MascotaDto());
        cargarDuenios(model, principal);
        return "mascotas/formulario";
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO', 'CLIENTE')")
    public String guardar(@Valid @ModelAttribute("mascota") MascotaDto dto, BindingResult result, Model model, Principal principal) {
        if (result.hasErrors()) {
            cargarDuenios(model, principal);
            return "mascotas/formulario";
        }
        mascotaService.guardar(dto);
        return "redirect:/mascotas";
    }

    @GetMapping("/{id}/editar")
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO', 'CLIENTE')")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, Principal principal) {
        model.addAttribute("mascota", mascotaService.obtenerPorId(id));
        cargarDuenios(model, principal);
        return "mascotas/formulario";
    }

    @PostMapping("/{id}/eliminar")
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO')")
    public String eliminar(@PathVariable Long id) {
        mascotaService.eliminar(id);
        return "redirect:/mascotas";
    }

    private void cargarDuenios(Model model, Principal principal) {
        UsuarioDto usuario = usuarioService.obtenerPorUsername(principal.getName());
        if (usuario.getRol() == Rol.ROLE_CLIENTE) {
            DuenioDto duenio = duenioService.obtenerPorUsuarioUsername(principal.getName());
            model.addAttribute("duenios", duenio != null ? List.of(duenio) : List.of());
        } else {
            model.addAttribute("duenios", duenioService.listarTodos());
        }
    }
}