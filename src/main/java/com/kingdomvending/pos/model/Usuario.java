package com.kingdomvending.pos.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String nombreEstablecimiento;
    private String tipoSuscripcion; 
    private double precioSuscripcion;
    private LocalDate fechaVencimiento;
    private String tokenRecuperacion;

    // Relación: Un usuario tiene muchos ítems de pedido
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itemsPedidos;

    public Usuario() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNombreEstablecimiento() { return nombreEstablecimiento; }
    public void setNombreEstablecimiento(String nombreEstablecimiento) { this.nombreEstablecimiento = nombreEstablecimiento; }

    public String getTipoSuscripcion() { return tipoSuscripcion; }
    public void setTipoSuscripcion(String tipoSuscripcion) { this.tipoSuscripcion = tipoSuscripcion; }

    public double getPrecioSuscripcion() { return precioSuscripcion; }
    public void setPrecioSuscripcion(double precioSuscripcion) { this.precioSuscripcion = precioSuscripcion; }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public String getTokenRecuperacion() { return tokenRecuperacion; }
    public void setTokenRecuperacion(String tokenRecuperacion) { this.tokenRecuperacion = tokenRecuperacion; }

    public List<ItemPedido> getItemsPedidos() { return itemsPedidos; }
    public void setItemsPedidos(List<ItemPedido> itemsPedidos) { this.itemsPedidos = itemsPedidos; }
}