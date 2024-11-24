package org.example.controller;

import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import org.example.model.Autor;
import org.example.repository.AutorRepository;
import org.example.service.AutorService;

import javax.management.BadAttributeValueExpException;
import java.sql.SQLException;
import java.util.List;


public class AutorController {

  AutorService service;

  AutorRepository repository;

  Gson gson;

  public AutorController(Javalin server) {
    this.repository = new AutorRepository();
    this.service = new AutorService();
    this.gson = new Gson();

    // "/estudante"
    server.get("/autor", this::listarAutores);
    server.post("/autor", this::criarAutor);

    // "/estudante/:id"
    server.get("/autor/{id}", this::acharAutor);
    server.put("/autor/{id}", this::atualizarAutor);
    server.delete("/autor/{id}", this::deletarAutor);
  }

  private void criarAutor(Context ctx) throws SQLException, BadAttributeValueExpException {
    Autor data;
    try {
      data = ctx.bodyAsClass(Autor.class);
    } catch (Throwable e) {
      throw new BadRequestResponse("payload invalido");
    }

    System.out.println(data);
    Autor result = service.criarAutor(data);
    String resultJson = gson.toJson((result));
    ctx.status(201).json(resultJson);
  }

  private void atualizarAutor(Context ctx) throws BadAttributeValueExpException, SQLException {
    Autor data;
    int autorId;
    try {
      data = ctx.bodyAsClass(Autor.class);
      autorId = Integer.parseInt(ctx.pathParam("id"));
    } catch (Throwable e) {
      throw new BadRequestResponse("payload invalido");
    }

    Autor result = service.atualizarAutor(autorId, data);
    String resultJson = gson.toJson((result));
    ctx.status(200).json(resultJson);
  }

  private void listarAutores(Context ctx) {
    List<Autor> result = repository.listarAutores();
    String resultJson = gson.toJson((result));
    ctx.status(200).json(resultJson);
  }

  private void acharAutor(Context ctx) {
    Integer id = Integer.valueOf(ctx.pathParam("id"));
    Autor result = repository.acharPeloId(id);
    String resultJson = gson.toJson((result));
    ctx.status(200).json(resultJson);
  }

  private void deletarAutor(Context ctx) {
    Integer id = Integer.valueOf(ctx.pathParam("id"));
    service.deletarAutor(id);
    ctx.status(204);
  }
}
