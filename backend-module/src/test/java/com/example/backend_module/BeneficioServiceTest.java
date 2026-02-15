package com.example.backend_module;

import com.example.backend_module.entities.Beneficio;
import com.example.backend_module.repositories.BeneficioRepository;
import com.example.backend_module.services.BeneficioService;
import com.example.backend_module.dto.TransferenciaRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeneficioServiceTest {

    @Mock
    private BeneficioRepository beneficioRepository;

    @InjectMocks
    private BeneficioService beneficioService;

    private Beneficio beneficio1;
    private Beneficio beneficio2;

    @BeforeEach
    void setUp() {
        beneficio1 = new Beneficio("Benefício A", "Descrição A", new BigDecimal("1000.00"));
        beneficio1.setId(1L);
        beneficio1.setAtivo(true);

        beneficio2 = new Beneficio("Benefício B", "Descrição B", new BigDecimal("500.00"));
        beneficio2.setId(2L);
        beneficio2.setAtivo(true);
    }

    @Test
    void criar_DeveRetornarBeneficioSalvo() {
        when(beneficioRepository.save(any(Beneficio.class))).thenReturn(beneficio1);

        Beneficio resultado = beneficioService.criar(beneficio1);

        assertNotNull(resultado);
        assertEquals("Benefício A", resultado.getNome());
        assertTrue(resultado.getAtivo());
        verify(beneficioRepository).save(beneficio1);
    }

    @Test
    void buscarPorId_QuandoExiste_DeveRetornarBeneficio() {
        when(beneficioRepository.findById(1L)).thenReturn(Optional.of(beneficio1));

        Beneficio resultado = beneficioService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Benefício A", resultado.getNome());
    }

    @Test
    void buscarPorId_QuandoNaoExiste_DeveLancarExcecao() {
        when(beneficioRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            beneficioService.buscarPorId(999L);
        });

        assertTrue(exception.getMessage().contains("Benefício não encontrado"));
    }

    @Test
    void listarTodos_DeveRetornarTodosBeneficios() {
        when(beneficioRepository.findAll()).thenReturn(Arrays.asList(beneficio1, beneficio2));

        var resultado = beneficioService.listarTodos();

        assertEquals(2, resultado.size());
    }

    @Test
    void atualizar_DeveRetornarBeneficioAtualizado() {
        when(beneficioRepository.findById(1L)).thenReturn(Optional.of(beneficio1));
        when(beneficioRepository.save(any(Beneficio.class))).thenReturn(beneficio1);

        Beneficio atualizado = new Beneficio("Novo Nome", "Nova Descrição", new BigDecimal("1500.00"));
        atualizado.setAtivo(false);

        Beneficio resultado = beneficioService.atualizar(1L, atualizado);

        assertEquals("Novo Nome", resultado.getNome());
        assertEquals("Nova Descrição", resultado.getDescricao());
        assertEquals(new BigDecimal("1500.00"), resultado.getValor());
        assertFalse(resultado.getAtivo());
    }

    @Test
    void deletar_QuandoExiste_DeveDeletarBeneficio() {
        when(beneficioRepository.findById(1L)).thenReturn(Optional.of(beneficio1));
        doNothing().when(beneficioRepository).delete(beneficio1);

        assertDoesNotThrow(() -> beneficioService.deletar(1L));
        verify(beneficioRepository).delete(beneficio1);
    }
}