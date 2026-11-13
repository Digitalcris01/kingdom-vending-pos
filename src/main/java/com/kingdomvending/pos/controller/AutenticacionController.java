package com.kingdomvending.pos.controller;

import com.kingdomvending.pos.model.Usuario;
import com.kingdomvending.pos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AutenticacionController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Registro de Usuario con Plan de Suscripción ($10.000 COP Mensual o $100.000 COP Anual)
    @PostMapping("/registro")
    public String registrarUsuario(@RequestBody Usuario usuario) {
        if ("ANUAL".equalsIgnoreCase(usuario.getTipoSuscripcion())) {
            usuario.setPrecioSuscripcion(100000.0);
            usuario.setFechaVencimiento(LocalDate.now().plusYears(1));
        } else {
            usuario.setTipoSuscripcion("MENSUAL");
            usuario.setPrecioSuscripcion(10000.0);
            usuario.setFechaVencimiento(LocalDate.now().plusMonths(1));
        }
        usuarioRepository.save(usuario);
        return "Usuario registrado correctamente con Plan " + usuario.getTipoSuscripcion() + " ($" + usuario.getPrecioSuscripcion() + " COP).";
    }

    // Solicitar correo de restauración de contraseña si se le olvidó
    @PostMapping("/olvide-password")
    public String solicitarRecuperacion(@RequestParam String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario != null) {
            String token = UUID.randomUUID().toString();
            usuario.setTokenRecuperacion(token);
            usuarioRepository.save(usuario);
            return "Se ha enviado un enlace de recuperación al correo: " + email;
        }
        return "El correo ingresado no está registrado en el sistema.";
    }
}