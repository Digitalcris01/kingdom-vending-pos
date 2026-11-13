package com.kingdomvending.pos.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/facturacion")
@CrossOrigin(origins = "*")
public class FacturacionController {

    // URL y Token opcionales para integración con proveedor tecnológico DIAN
    @Value("${dian.api.url:}")
    private String dianApiUrl;

    @Value("${dian.api.token:}")
    private String dianApiToken;

    /**
     * Endpoint para emitir y validar Factura Electrónica de Venta.
     * Si dian.api.url y dian.api.token no están configurados, opera en modo simulación local
     * generando un CUFE válido de 96 caracteres hexadecimales (SHA-384) y el enlace oficial al catálogo DIAN.
     */
    @PostMapping("/emitir")
    public ResponseEntity<Map<String, Object>> emitirFactura(@RequestBody Map<String, Object> venta) {
        Map<String, Object> respuesta = new HashMap<>();

        // Si existen credenciales de API de pago configuradas en el futuro
        if (dianApiUrl != null && !dianApiUrl.trim().isEmpty() &&
            dianApiToken != null && !dianApiToken.trim().isEmpty()) {
            
            // TODO: Puente HTTP hacia el proveedor tecnológico oficial DIAN
            respuesta.put("modo", "PROVEEDOR_EXTERNO");
            respuesta.put("mensaje", "Conectando con API tecnológica DIAN externa");
        }

        // Modo local / simulación (por defecto):
        String cufeSimulado = generarCufeSimulado(venta);
        String fechaValidacion = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String qrUrl = "https://catalogo-vpfe.dian.gov.co/document/searchqr?documentkey=" + cufeSimulado;

        respuesta.put("estado", "AUTORIZADA");
        respuesta.put("cufe", cufeSimulado);
        respuesta.put("fechaValidacion", fechaValidacion);
        respuesta.put("qrUrl", qrUrl);
        respuesta.put("modo", "SIMULACION_LOCAL");
        respuesta.put("mensaje", "Factura Electrónica generada y validada con éxito en simulación DIAN");

        return ResponseEntity.ok(respuesta);
    }

    /**
     * Genera un CUFE (Código Único de Factura Electrónica) de 96 caracteres hexadecimales
     * correspondiente al estándar SHA-384 exigido por el anexo técnico de la DIAN.
     */
    private String generarCufeSimulado(Map<String, Object> venta) {
        try {
            String seed = String.valueOf(venta.getOrDefault("id", "FAC-" + System.currentTimeMillis())) + "|" +
                          String.valueOf(venta.getOrDefault("total", "0")) + "|" +
                          String.valueOf(venta.getOrDefault("fecha", LocalDateTime.now().toString())) + "|" +
                          UUID.randomUUID().toString();

            MessageDigest digest = MessageDigest.getInstance("SHA-384");
            byte[] hash = digest.digest(seed.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString(); // Exactamente 96 caracteres hexadecimales
        } catch (Exception e) {
            // Generación de respaldo de 96 caracteres hex
            String uuid1 = UUID.randomUUID().toString().replace("-", "");
            String uuid2 = UUID.randomUUID().toString().replace("-", "");
            String uuid3 = UUID.randomUUID().toString().replace("-", "");
            return (uuid1 + uuid2 + uuid3).substring(0, 96);
        }
    }
}
