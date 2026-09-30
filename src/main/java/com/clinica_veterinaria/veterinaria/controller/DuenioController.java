package com.clinica_veterinaria.veterinaria.controller;

import com.clinica_veterinaria.veterinaria.dto.DuenioDto;
import com.clinica_veterinaria.veterinaria.dto.UsuarioDto;
import com.clinica_veterinaria.veterinaria.entity.enums.Rol;
import com.clinica_veterinaria.veterinaria.service.IDuenioService;
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
@RequestMapping("/duenios")
public class DuenioController {

    @Autowired
    private IDuenioService duenioService;
    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO', 'CLIENTE')")
    public String listarTodos(Model model, Principal principal, @RequestParam(name = "dni", required = false) String dni) {
        UsuarioDto usuario = usuarioService.obtenerPorUsername(principal.getName());
        if (usuario.getRol() == Rol.ROLE_CLIENTE) {
            DuenioDto d = duenioService.obtenerPorUsuarioUsername(principal.getName());
            model.addAttribute("duenios", d != null ? List.of(d) : List.of());
        } else {
            if (dni != null && !dni.trim().isEmpty()) {
                model.addAttribute("duenios", duenioService.buscarPorDni(dni.trim()));
                model.addAttribute("busquedaDni", dni);
            } else {
                model.addAttribute("duenios", duenioService.listarTodos());
            }
        }
        return "duenios/lista";
    }

    @GetMapping("/nuevo")
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO', 'CLIENTE')")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("duenio", new DuenioDto());
        return "duenios/formulario";
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO', 'CLIENTE')")
    public String guardar(@Valid @ModelAttribute("duenio") DuenioDto dto, BindingResult result, Principal principal, Model model) {
        if (result.hasErrors()) {
            return "duenios/formulario";
        }
        try {
            UsuarioDto usuario = usuarioService.obtenerPorUsername(principal.getName());
            if (usuario.getRol() == Rol.ROLE_CLIENTE) {
                dto.setUsuarioId(usuario.getId());
            }
            duenioService.guardar(dto);
            return "redirect:/duenios";
        } catch (RuntimeException e) {
            model.addAttribute("errorDni", e.getMessage());
            return "duenios/formulario";
        }
    }

    @GetMapping("/{id}/editar")
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO', 'CLIENTE')")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("duenio", duenioService.obtenerPorId(id));
        return "duenios/formulario";
    }

    @PostMapping("/{id}/eliminar")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable Long id) {
        duenioService.eliminar(id);
        return "redirect:/duenios";
    }
}