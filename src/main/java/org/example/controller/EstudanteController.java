package org.example.controller;

import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import org.example.model.Estudante;
import org.example.repository.EstudanteRepository;
import org.example.service.EstudanteService;

import java.util.List;


public class EstudanteController {

  EstudanteService service;

  EstudanteRepository repository;

  Gson gson;

  public EstudanteController(Javalin server) {
    this.repository = new EstudanteRepository();
    this.service = new EstudanteService();
    this.gson = new Gson();

    // "/estudante"
    server.get("/estudante", this::listarEstudante);
    server.post("/estudante", this::criarEstudante);

    // "/estudante/:id"
    server.get("/estudante/{matricula}", this::acharEstudante);
    server.put("/estudante/{matricula}", this::atualizarEstudante);
    server.delete("/estudante/{matricula}", this::deletarEstudante);
  }

  private void criarEstudante(Context ctx) {
    Estudante data;
    try {
      data = ctx.bodyAsClass(Estudante.class);
    } catch (NullPointerException error) {
      throw new BadRequestResponse("payload invalido");
    }

    Estudante result = service.criarEstudante(data);
    String resultJson = gson.toJson((result));
    ctx.status(201).json(resultJson);
  }

  private void atualizarEstudante(Context ctx) {
    Estudante data;
    int matricula;
    try {
      data = ctx.bodyAsClass(Estudante.class);
      matricula = Integer.parseInt(ctx.pathParam("matricula"));
    } catch (NullPointerException error) {
      throw new BadRequestResponse("payload invalido");
    }

    Estudante result = service.atualizarEstudante(matricula, data);
    String resultJson = gson.toJson((result));
    ctx.status(200).json(resultJson);
  }

  private void listarEstudante(Context ctx) {
    List<Estudante> result = repository.listarEstudantes();
    String resultJson = gson.toJson((result));
    ctx.status(200).json(resultJson);
  }

  private void acharEstudante(Context ctx) {
    Integer matricula = Integer.valueOf(ctx.pathParam("matricula"));
    Estudante result = repository.acharPelaMatricula(matricula, false);
    String resultJson = gson.toJson((result));
    ctx.status(200).json(resultJson);
  }

  private void deletarEstudante(Context ctx) {
    Integer id = Integer.valueOf(ctx.pathParam("matricula"));
    service.deletarEstudante(id);
    ctx.status(204);
  }
}

