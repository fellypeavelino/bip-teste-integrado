package com.example.rest;

import com.example.ejb.BeneficioEjbService;
import com.example.entity.Beneficio;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

@Path("/beneficios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BeneficioRest {

    @EJB
    private BeneficioEjbService beneficioService;

    @POST
    public Response criar(Beneficio beneficio) {
        try {
            Beneficio novo = beneficioService.criar(beneficio);
            return Response.status(Response.Status.CREATED)
                    .entity(novo)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") Long id) {
        Beneficio beneficio = beneficioService.buscar(id);
        if (beneficio == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(beneficio).build();
    }

    @GET
    public Response listar() {
        List<Beneficio> beneficios = beneficioService.listar();
        return Response.ok(beneficios).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, Beneficio beneficio) {
        try {
            Beneficio atualizado = beneficioService.atualizar(id, beneficio);
            return Response.ok(atualizado).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") Long id) {
        beneficioService.remover(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/transferir")
    public Response transferir(
            @QueryParam("origem") Long origemId,
            @QueryParam("destino") Long destinoId,
            @QueryParam("valor") BigDecimal valor) {
        
        try {
            beneficioService.transferir(origemId, destinoId, valor);
            return Response.ok("Transferência realizada com sucesso").build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/transferir-original")
    public Response transferirOriginal(
            @QueryParam("origem") Long origemId,
            @QueryParam("destino") Long destinoId,
            @QueryParam("valor") BigDecimal valor) {
        
        try {
            beneficioService.transferOriginal(origemId, destinoId, valor);
            return Response.ok("Transferência original realizada").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }
}
