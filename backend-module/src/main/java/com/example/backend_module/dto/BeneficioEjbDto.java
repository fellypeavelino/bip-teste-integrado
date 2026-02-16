package com.example.backend_module.dto;

import java.math.BigDecimal;

public class BeneficioEjbDto {
    
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private Long versao;
    
    public BeneficioEjbDto() {
    }
    
    public BeneficioEjbDto(String descricao, BigDecimal valor) {
        this.descricao = descricao;
        this.valor = valor;
    }
    
    public BeneficioEjbDto(Long id, String descricao, BigDecimal valor, Long versao) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.versao = versao;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public BigDecimal getValor() {
        return valor;
    }
    
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
    public Long getVersao() {
        return versao;
    }
    
    public void setVersao(Long versao) {
        this.versao = versao;
    }
}
