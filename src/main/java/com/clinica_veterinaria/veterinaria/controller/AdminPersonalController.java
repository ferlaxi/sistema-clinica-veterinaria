package com.clinica_veterinaria.veterinaria.controller;

import com.clinica_veterinaria.veterinaria.dto.UsuarioDto;
import com.clinica_veterinaria.veterinaria.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/personal")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPersonalController {

    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping
    public String listarPersonal(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "admin/personal/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        UsuarioDto dto = new UsuarioDto();
        dto.setActivo(true);
        model.addAttribute("usuario", dto);
        return "admin/personal/formulario";
    }

    @PostMapping("/guardar")
    public String guardarPersonal(@ModelAttribute("usuario") UsuarioDto dto) {
        if (dto.getId() == null) {
            usuarioService.registrarPersonal(dto);
        } else {
            usuarioService.actualizarPersonal(dto.getId(), dto);
        }
        return "redirect:/admin/personal";
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", usuarioService.obtenerPorId(id));
        return "admin/personal/formulario";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarPersonal(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return "redirect:/admin/personal";
    }
}
