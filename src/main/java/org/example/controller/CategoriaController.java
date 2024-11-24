package org.example.controller;

import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import org.example.model.Categoria;
import org.example.repository.CategoriaRepository;
import org.example.service.CategoriaService;

import javax.management.BadAttributeValueExpException;
import java.sql.SQLException;
import java.util.List;


public class CategoriaController {

  CategoriaService service;

  CategoriaRepository repository;

  Gson gson;

  public CategoriaController(Javalin server) {
    this.repository = new CategoriaRepository();
    this.service = new CategoriaService();
    this.gson = new Gson();

    // "/estudante"
    server.get("/categoria", this::listarAutores);
    server.post("/categoria", this::criarCategoria);

    // "/estudante/:id"
    server.get("/categoria/{id}", this::acharAutor);
    server.put("/categoria/{id}", this::atualizarAutor);
    server.delete("/categoria/{id}", this::deletarCategoria);
  }

  private void criarCategoria(Context ctx) throws SQLException, BadAttributeValueExpException {
    Categoria data;
    try {
      data = ctx.bodyAsClass(Categoria.class);
    } catch (Throwable e) {
      throw new BadRequestResponse("payload invalido");
    }

    System.out.println(data);
    Categoria result = service.criarCategoria(data);
    String resultJson = gson.toJson((result));
    ctx.status(201).json(resultJson);
  }

  private void atualizarAutor(Context ctx) throws BadAttributeValueExpException, SQLException {
    Categoria data;
    int autorId;
    try {
      data = ctx.bodyAsClass(Categoria.class);
      autorId = Integer.parseInt(ctx.pathParam("id"));
    } catch (Throwable e) {
      throw new BadRequestResponse("payload invalido");
    }

    Categoria result = service.atualizarCategoria(autorId, data);
    String resultJson = gson.toJson((result));
    ctx.status(200).json(resultJson);
  }

  private void listarAutores(Context ctx) {
    List<Categoria> result = repository.listarCategorias();
    String resultJson = gson.toJson((result));
    ctx.status(200).json(resultJson);
  }

  private void acharAutor(Context ctx) {
    Integer id = Integer.valueOf(ctx.pathParam("id"));
    Categoria result = repository.acharPeloId(id);
    String resultJson = gson.toJson((result));
    ctx.status(200).json(resultJson);
  }

  private void deletarCategoria(Context ctx) {
    Integer id = Integer.valueOf(ctx.pathParam("id"));
    service.deletarCategoria(id);
    ctx.status(204);
  }
}
