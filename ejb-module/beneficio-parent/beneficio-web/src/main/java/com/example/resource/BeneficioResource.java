/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.resource;

/**
 *
 * @author Usuario
 */

import com.example.ejb.BeneficioEjbService;
import com.example.entity.Beneficio;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.List;

@Path("/beneficios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BeneficioResource {
    @Inject
    private BeneficioEjbService service;

    @POST
    public Beneficio criar(Beneficio b) {
        return service.criar(b);
    }

    @GET
    public List<Beneficio> listar() {
        return service.listar();
    }

    @GET
    @Path("/{id}")
    public Beneficio buscar(@PathParam("id") Long id) {
        return service.buscar(id);
    }

    @PUT
    @Path("/{id}")
    public Beneficio atualizar(@PathParam("id") Long id, BigDecimal valor) {
        return service.atualizar(id, valor);
    }

    @DELETE
    @Path("/{id}")
    public void remover(@PathParam("id") Long id) {
        service.remover(id);
    }

    @POST
    @Path("/transferir")
    public void transferir(
        @QueryParam("origem") Long origem,
        @QueryParam("destino") Long destino,
        @QueryParam("valor") BigDecimal valor) {

        service.transferir(origem, destino, valor);
    }
}

