package com.kingdomvending.pos.repository;

import com.kingdomvending.pos.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    // Permite buscar todos los productos que pertenecen a un usuario específico
    List<ItemPedido> findByUsuarioId(Long usuarioId);
}