package com.kingdomvending.pos.controller;

import com.kingdomvending.pos.model.Mesa;
import com.kingdomvending.pos.model.ItemPedido;
import com.kingdomvending.pos.repository.MesaRepository;
import com.kingdomvending.pos.repository.ItemPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
@CrossOrigin(origins = "*")
public class MesaController {

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    // 1. Obtener todas las mesas que le pertenecen a un usuario específico
    @GetMapping("/usuario/{usuarioId}")
    public List<Mesa> obtenerMesasPorUsuario(@PathVariable Long usuarioId) {
        return mesaRepository.findByUsuarioId(usuarioId);
    }

    // 2. Obtener mesas según la zona, filtradas también por el usuario
    @GetMapping("/usuario/{usuarioId}/zona/{nombreZona}")
    public List<Mesa> obtenerPorZonaYUsuario(@PathVariable Long usuarioId, @PathVariable String nombreZona) {
        return mesaRepository.findByUsuarioIdAndZona(usuarioId, nombreZona);
    }

    // 3. Obtener el detalle completo y total de consumos de una mesa por su ID
    @GetMapping("/{id}")
    public Mesa obtenerDetalleMesa(@PathVariable Long id) {
        return mesaRepository.findById(id).orElse(null);
    }

    // 4. Agregar un producto a la mesa seleccionada
    @PostMapping("/{id}/agregar-producto")
    public Mesa agregarProducto(@PathVariable Long id, @RequestBody ItemPedido nuevoItem) {
        Mesa mesa = mesaRepository.findById(id).orElseThrow();
        
        mesa.getProductos().add(nuevoItem);
        mesa.setOcupada(true);
        
        return mesaRepository.save(mesa);
    }

    // 5. Actualizar el precio o la cantidad de un producto ya guardado
    @PutMapping("/productos/{productoId}")
    public ItemPedido actualizarProducto(@PathVariable Long productoId, @RequestBody ItemPedido datosActualizados) {
        ItemPedido item = itemPedidoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        item.setNombreProducto(datosActualizados.getNombreProducto());
        item.setCantidad(datosActualizados.getCantidad());
        item.setPrecioUnitario(datosActualizados.getPrecioUnitario());
        
        return itemPedidoRepository.save(item);
    }

    // 6. Eliminar un producto guardado
    @DeleteMapping("/productos/{productoId}")
    public void eliminarProducto(@PathVariable Long productoId) {
        itemPedidoRepository.deleteById(productoId);
    }
}