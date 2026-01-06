/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.ejb;

/**
 *
 * @author Usuario
 */
import java.math.BigDecimal;
import java.util.List;

import com.example.entity.Beneficio;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;

@Stateless
public class BeneficioEjbService {

    @PersistenceContext
    private EntityManager em;

    public void transferOriginal(Long fromId, Long toId, BigDecimal amount) {
        Beneficio from = em.find(Beneficio.class, fromId);
        Beneficio to   = em.find(Beneficio.class, toId);

        // BUG: sem validações, sem locking, pode gerar saldo negativo e lost update
        from.setValor(from.getValor().subtract(amount));
        to.setValor(to.getValor().add(amount));

        em.merge(from);
        em.merge(to);
    }

    /* ===== CRUD ===== */

    public Beneficio criar(Beneficio b) {
        em.persist(b);
        return b;
    }

    public Beneficio buscar(Long id) {
        return em.find(Beneficio.class, id);
    }

    public List<Beneficio> listar() {
        return em.createQuery("from Beneficio", Beneficio.class).getResultList();
    }

    public Beneficio atualizar(Long id, BigDecimal valor) {
        Beneficio b = buscar(id);
        if (b == null) throw new IllegalArgumentException("Benefício não encontrado");
        b.setValor(valor);
        return b;
    }

    public void remover(Long id) {
        Beneficio b = buscar(id);
        if (b != null) em.remove(b);
    }

    /* ===== TRANSFERÊNCIA SEGURA ===== */

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void transferir(Long origemId, Long destinoId, BigDecimal valor) {

        if (valor.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Valor inválido");

        Beneficio origem = em.find(
            Beneficio.class, origemId, LockModeType.OPTIMISTIC);
        Beneficio destino = em.find(
            Beneficio.class, destinoId, LockModeType.OPTIMISTIC);

        if (origem == null || destino == null)
            throw new IllegalArgumentException("Benefício inexistente");

        if (origem.getValor().compareTo(valor) < 0)
            throw new IllegalStateException("Saldo insuficiente");

        origem.setValor(origem.getValor().subtract(valor));
        destino.setValor(destino.getValor().add(valor));
        // Flush implícito + rollback automático em erro
    }
}

