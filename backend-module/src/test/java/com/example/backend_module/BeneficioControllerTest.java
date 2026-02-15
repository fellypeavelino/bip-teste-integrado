package com.example.backend_module;

import com.example.backend_module.controllers.BeneficioController;
import com.example.backend_module.dto.TransferenciaRequest;
import com.example.backend_module.entities.Beneficio;
import com.example.backend_module.services.BeneficioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeneficioController.class)
class BeneficioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BeneficioService beneficioService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void listarTodos_DeveRetornarListaDeBeneficios() throws Exception {
        List<Beneficio> beneficios = Arrays.asList(beneficio1, beneficio2);
        when(beneficioService.listarTodos()).thenReturn(beneficios);

        mockMvc.perform(get("/api/v1/beneficios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nome").value("Benefício A"))
                .andExpect(jsonPath("$[1].nome").value("Benefício B"));

        verify(beneficioService).listarTodos();
    }

    @Test
    void buscarPorId_DeveRetornarBeneficio() throws Exception {
        when(beneficioService.buscarPorId(1L)).thenReturn(beneficio1);

        mockMvc.perform(get("/api/v1/beneficios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Benefício A"))
                .andExpect(jsonPath("$.valor").value(1000.00));

        verify(beneficioService).buscarPorId(1L);
    }

    @Test
    void criar_DeveRetornarBeneficioCriado() throws Exception {
        Beneficio novoBeneficio = new Beneficio("Benefício C", "Descrição C", new BigDecimal("750.00"));
        when(beneficioService.criar(any(Beneficio.class))).thenReturn(novoBeneficio);

        mockMvc.perform(post("/api/v1/beneficios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoBeneficio)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Benefício C"))
                .andExpect(jsonPath("$.valor").value(750.00));

        verify(beneficioService).criar(any(Beneficio.class));
    }

    @Test
    void atualizar_DeveRetornarBeneficioAtualizado() throws Exception {
        Beneficio atualizado = new Beneficio("Benefício A Atualizado", "Nova descrição", new BigDecimal("1200.00"));
        when(beneficioService.atualizar(eq(1L), any(Beneficio.class))).thenReturn(atualizado);

        mockMvc.perform(put("/api/v1/beneficios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Benefício A Atualizado"));

        verify(beneficioService).atualizar(eq(1L), any(Beneficio.class));
    }

    @Test
    void deletar_DeveRetornarNoContent() throws Exception {
        doNothing().when(beneficioService).deletar(1L);

        mockMvc.perform(delete("/api/v1/beneficios/1"))
                .andExpect(status().isNoContent());

        verify(beneficioService).deletar(1L);
    }

    @Test
    void transferir_DeveRetornarSucesso() throws Exception {
        doNothing().when(beneficioService).transferir(any(TransferenciaRequest.class));

        TransferenciaRequest request = new TransferenciaRequest();
        request.setOrigemId(1L);
        request.setDestinoId(2L);
        request.setValor(new BigDecimal("100.00"));

        mockMvc.perform(post("/api/v1/beneficios/transferir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Transferência realizada com sucesso"));

        verify(beneficioService).transferir(any(TransferenciaRequest.class));
    }

    @Test
    void criar_ComNomeVazio_DeveRetornarBadRequest() throws Exception {
        Beneficio beneficioInvalido = new Beneficio("", "Descrição", new BigDecimal("100.00"));

        mockMvc.perform(post("/api/v1/beneficios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beneficioInvalido)))
                .andExpect(status().isBadRequest());
    }
}