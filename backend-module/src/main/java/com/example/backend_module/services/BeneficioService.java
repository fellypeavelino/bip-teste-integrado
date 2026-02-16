package com.example.backend_module.services;

import com.example.backend_module.dto.TransferenciaRequest;
import com.example.backend_module.entities.Beneficio;
import com.example.backend_module.repositories.BeneficioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class BeneficioService {

    @Autowired
    private BeneficioRepository beneficioRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EjbIntegrationService ejbIntegrationService;

    public Beneficio criar(Beneficio beneficio) {
        beneficio.setAtivo(true);
        beneficio.setVersion(0L);
        Beneficio salvo = beneficioRepository.save(beneficio);
        
        Long id = salvo.getId();
        String descricao = salvo.getDescricao() != null ? salvo.getDescricao() : salvo.getNome();
        BigDecimal valor = salvo.getValor();
        
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                ejbIntegrationService.sincronizarCriacao(id, descricao, valor);
            }
        });
        
        return salvo;
    }

    public Beneficio buscarPorId(Long id) {
        return beneficioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Benefício não encontrado: " + id));
    }

    public List<Beneficio> listarTodos() {
        return beneficioRepository.findAll();
    }

    public List<Beneficio> listarAtivos() {
        return beneficioRepository.findByAtivoTrue();
    }

    public Beneficio atualizar(Long id, Beneficio beneficioAtualizado) {
        Beneficio beneficio = buscarPorId(id);
        
        beneficio.setNome(beneficioAtualizado.getNome());
        beneficio.setDescricao(beneficioAtualizado.getDescricao());
        beneficio.setValor(beneficioAtualizado.getValor());
        beneficio.setAtivo(beneficioAtualizado.getAtivo());
        
        Beneficio salvo = beneficioRepository.save(beneficio);
        
        String descricao = salvo.getDescricao() != null ? salvo.getDescricao() : salvo.getNome();
        BigDecimal valor = salvo.getValor();
        
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                ejbIntegrationService.sincronizarAtualizacao(id, descricao, valor);
            }
        });
        
        return salvo;
    }

    public void deletar(Long id) {
        Beneficio beneficio = buscarPorId(id);
        beneficioRepository.delete(beneficio);
        
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                ejbIntegrationService.sincronizarRemocao(id);
            }
        });
    }

    @Transactional
    public void transferir(TransferenciaRequest request) {
        if (request.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }

        if (request.getOrigemId().equals(request.getDestinoId())) {
            throw new IllegalArgumentException("Origem e destino não podem ser iguais");
        }

        Beneficio origem = entityManager.find(Beneficio.class, request.getOrigemId(), LockModeType.OPTIMISTIC);
        Beneficio destino = entityManager.find(Beneficio.class, request.getDestinoId(), LockModeType.OPTIMISTIC);

        if (origem == null || !origem.getAtivo()) {
            throw new IllegalArgumentException("Benefício de origem não encontrado ou inativo");
        }

        if (destino == null || !destino.getAtivo()) {
            throw new IllegalArgumentException("Benefício de destino não encontrado ou inativo");
        }

        if (origem.getValor().compareTo(request.getValor()) < 0) {
            throw new IllegalStateException("Saldo insuficiente para transferência");
        }

        try {
            origem.setValor(origem.getValor().subtract(request.getValor()));
            destino.setValor(destino.getValor().add(request.getValor()));

            entityManager.merge(origem);
            entityManager.merge(destino);
            entityManager.flush();
        } catch (OptimisticLockException e) {
            throw new IllegalStateException("Conflito de concorrência detectado. Tente novamente.");
        }
    }
}