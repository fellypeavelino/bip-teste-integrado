package com.example.backend_module.controllers;

import com.example.backend_module.dto.TransferenciaRequest;
import com.example.backend_module.entities.Beneficio;
import com.example.backend_module.services.BeneficioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/beneficios")
@Tag(name = "Benefícios", description = "API para gerenciamento de benefícios")
@CrossOrigin(origins = "*")
public class BeneficioController {

    @Autowired
    private BeneficioService beneficioService;

    @GetMapping
    @Operation(summary = "Listar todos os benefícios", description = "Retorna uma lista com todos os benefícios cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de benefícios retornada com sucesso")
    public ResponseEntity<List<Beneficio>> listarTodos() {
        return ResponseEntity.ok(beneficioService.listarTodos());
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar benefícios ativos", description = "Retorna uma lista com benefícios ativos")
    public ResponseEntity<List<Beneficio>> listarAtivos() {
        return ResponseEntity.ok(beneficioService.listarAtivos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar benefício por ID", description = "Retorna um benefício específico pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Benefício encontrado"),
        @ApiResponse(responseCode = "404", description = "Benefício não encontrado", content = @Content)
    })
    public ResponseEntity<Beneficio> buscarPorId(
            @Parameter(description = "ID do benefício") @PathVariable Long id) {
        return ResponseEntity.ok(beneficioService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Criar novo benefício", description = "Cria um novo benefício no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Benefício criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    public ResponseEntity<Beneficio> criar(
            @Valid @RequestBody @Parameter(description = "Dados do benefício", required = true) Beneficio beneficio) {
        Beneficio novo = beneficioService.criar(beneficio);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar benefício", description = "Atualiza os dados de um benefício existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Benefício atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Benefício não encontrado", content = @Content),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    public ResponseEntity<Beneficio> atualizar(
            @Parameter(description = "ID do benefício") @PathVariable Long id,
            @Valid @RequestBody @Parameter(description = "Dados atualizados", required = true) Beneficio beneficio) {
        return ResponseEntity.ok(beneficioService.atualizar(id, beneficio));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar benefício", description = "Remove um benefício do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Benefício deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Benefício não encontrado", content = @Content)
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do benefício") @PathVariable Long id) {
        beneficioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transferir")
    @Operation(summary = "Transferir valor entre benefícios", description = "Transfere um valor de um benefício para outro")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transferência realizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou saldo insuficiente", content = @Content),
        @ApiResponse(responseCode = "409", description = "Conflito de concorrência", content = @Content)
    })
    public ResponseEntity<Map<String, String>> transferir(
            @Valid @RequestBody @Parameter(description = "Dados da transferência", required = true) 
            TransferenciaRequest request) {
        beneficioService.transferir(request);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Transferência realizada com sucesso");
        return ResponseEntity.ok(response);
    }
}