package com.example.backend_module.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class TransferenciaRequest {

    @NotNull(message = "ID de origem é obrigatório")
    private Long origemId;

    @NotNull(message = "ID de destino é obrigatório")
    private Long destinoId;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    @Digits(integer = 13, fraction = 2, message = "Valor inválido")
    private BigDecimal valor;

    public Long getOrigemId() {
        return origemId;
    }

    public void setOrigemId(Long origemId) {
        this.origemId = origemId;
    }

    public Long getDestinoId() {
        return destinoId;
    }

    public void setDestinoId(Long destinoId) {
        this.destinoId = destinoId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}