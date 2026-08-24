package com.kingdomvending.pos.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mesas")
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreMesa; // Ej: "Mesa 1", "Sofa Derecho"
    private String zona;       // Ej: "ZONA BAR", "ZONA ONCES", "CALLE"
    private boolean ocupada;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ItemPedido> productos = new ArrayList<>();

    // Relación: Muchas mesas pertenecen a un solo usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Suma el valor total de todos los productos en la mesa
    public double getPrecioTotal() {
        if (productos == null) return 0.0;
        return productos.stream().mapToDouble(ItemPedido::getSubtotal).sum();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreMesa() { return nombreMesa; }
    public void setNombreMesa(String nombreMesa) { this.nombreMesa = nombreMesa; }

    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }

    public boolean isOcupada() { return ocupada; }
    public void setOcupada(boolean ocupada) { this.ocupada = ocupada; }

    public List<ItemPedido> getProductos() { return productos; }
    public void setProductos(List<ItemPedido> productos) { this.productos = productos; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}