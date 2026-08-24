package com.kingdomvending.pos.repository;

import com.kingdomvending.pos.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {

    // Método para buscar mesas por zona simple
    List<Mesa> findByZona(String zona);

    // 1. Buscar todas las mesas de un usuario específico
    List<Mesa> findByUsuarioId(Long usuarioId);

    // 2. Buscar mesas de un usuario filtradas por zona (con 'String' agregado)
    List<Mesa> findByUsuarioIdAndZona(Long usuarioId, String zona);
}