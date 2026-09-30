package com.clinica_veterinaria.veterinaria.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFoundError(NoHandlerFoundException ex, Model model) {
        model.addAttribute("errorStatus", "404");
        model.addAttribute("errorTitle", "Página no encontrada");
        model.addAttribute("errorMessage", "Lo sentimos, no pudimos encontrar la página que estás buscando. Puede haber sido eliminada o la URL es incorrecta.");
        return "error/error-page";
    }

    @ExceptionHandler(Exception.class)
    public String handleGlobalError(Exception ex, Model model) {
        ex.printStackTrace(); 
        model.addAttribute("errorStatus", "500");
        model.addAttribute("errorTitle", "Error Interno del Servidor");
        model.addAttribute("errorMessage", "Ha ocurrido un problema inesperado en el sistema. Soporte técnico ha sido notificado.");
        model.addAttribute("errorDetail", ex.getMessage());
        return "error/error-page";
    }
}
