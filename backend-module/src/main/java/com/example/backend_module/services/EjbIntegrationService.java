package com.example.backend_module.services;

import com.example.backend_module.dto.BeneficioEjbDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class EjbIntegrationService {
    
    private static final Logger logger = LoggerFactory.getLogger(EjbIntegrationService.class);
    
    @Value("${ejb.rest.url}")
    private String ejbBaseUrl;
    
    private final RestTemplate restTemplate;
    
    public EjbIntegrationService() {
        this.restTemplate = new RestTemplate();
    }
    
    public boolean isEjbAvailable() {
        try {
            String healthUrl = ejbBaseUrl.replace("/api", "") + "/health";
            ResponseEntity<String> response = restTemplate.getForEntity(healthUrl, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            logger.debug("EJB não está disponível: {}", e.getMessage());
            return false;
        }
    }
    
    public void sincronizarCriacao(Long id, String descricao, BigDecimal valor) {
        if (!isEjbAvailable()) {
            logger.info("EJB não está disponível. Dados não sincronizados: id={}, descricao={}", id, descricao);
            return;
        }
        
        try {
            BeneficioEjbDto dto = new BeneficioEjbDto(descricao, valor);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<BeneficioEjbDto> request = new HttpEntity<>(dto, headers);
            
            String url = ejbBaseUrl + "/beneficios";
            ResponseEntity<BeneficioEjbDto> response = restTemplate.postForEntity(url, request, BeneficioEjbDto.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Benefício sincronizado com sucesso no EJB: id={}, descricao={}", id, descricao);
            } else {
                logger.warn("Falha ao sincronizar benefício no EJB. Status: {}", response.getStatusCode());
            }
        } catch (ResourceAccessException e) {
            logger.error("Erro de conexão ao sincronizar com EJB: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao sincronizar criação com EJB: {}", e.getMessage());
        }
    }
    
    public void sincronizarAtualizacao(Long id, String descricao, BigDecimal valor) {
        if (!isEjbAvailable()) {
            logger.info("EJB não está disponível. Atualização não sincronizada: id={}", id);
            return;
        }
        
        try {
            BeneficioEjbDto dto = new BeneficioEjbDto(descricao, valor);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<BeneficioEjbDto> request = new HttpEntity<>(dto, headers);
            
            String url = ejbBaseUrl + "/beneficios/" + id;
            ResponseEntity<BeneficioEjbDto> response = restTemplate.exchange(
                url, HttpMethod.PUT, request, BeneficioEjbDto.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Benefício atualizado com sucesso no EJB: id={}", id);
            } else {
                logger.warn("Falha ao atualizar benefício no EJB. Status: {}", response.getStatusCode());
            }
        } catch (ResourceAccessException e) {
            logger.error("Erro de conexão ao atualizar no EJB: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao sincronizar atualização com EJB: {}", e.getMessage());
        }
    }
    
    public void sincronizarRemocao(Long id) {
        if (!isEjbAvailable()) {
            logger.info("EJB não está disponível. Remoção não sincronizada: id={}", id);
            return;
        }
        
        try {
            String url = ejbBaseUrl + "/beneficios/" + id;
            restTemplate.delete(url);
            logger.info("Benefício removido com sucesso no EJB: id={}", id);
        } catch (ResourceAccessException e) {
            logger.error("Erro de conexão ao remover no EJB: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao sincronizar remoção com EJB: {}", e.getMessage());
        }
    }
}
