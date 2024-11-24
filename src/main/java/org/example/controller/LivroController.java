package org.example.controller;

import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import org.example.model.Livro;
import org.example.repository.LivroRepository;
import org.example.service.LivroService;

import javax.management.BadAttributeValueExpException;
import java.sql.SQLException;
import java.util.List;


public class LivroController {

  LivroService service;

  LivroRepository repository;

  Gson gson;

  public LivroController(Javalin server) {
    this.repository = new LivroRepository();
    this.service = new LivroService();
    this.gson = new Gson();

    // "/estudante"
    server.get("/livro", this::listarAlugueis);
    server.post("/livro", this::criarLivro);

//         "/estudante/:id"
    server.get("/livro/{id}", this::acharLivro);
    server.put("/livro/{id}", this::atualizarLivro);
    server.delete("/livro/{id}", this::deletarLivro);
  }

  private void criarLivro(Context ctx) throws SQLException, BadAttributeValueExpException {
    Livro data;
    try {
      data = ctx.bodyAsClass(Livro.class);
    } catch (Throwable e) {
      e.printStackTrace();
      throw new BadRequestResponse("payload invalido");
    }

    System.out.println(data);
    Livro result = service.criarLivro(data);
    String resultJson = gson.toJson((result));
    ctx.status(201).json(resultJson);
  }

  private void atualizarLivro(Context ctx) throws BadAttributeValueExpException, SQLException {
    Livro data;
    int livroId;
    try {
      data = ctx.bodyAsClass(Livro.class);
      livroId = Integer.parseInt(ctx.pathParam("id"));
    } catch (Throwable e) {
      throw new BadRequestResponse("payload invalido");
    }

    Livro result = service.atualizarLivro(livroId, data);
    String resultJson = gson.toJson((result));
    ctx.status(200).json(resultJson);
  }

  private void listarAlugueis(Context ctx) {
    List<Livro> result = repository.listarLivros();
    String resultJson = gson.toJson((result));
    ctx.status(200).json(resultJson);
  }

  private void acharLivro(Context ctx) {
    Integer id = Integer.valueOf(ctx.pathParam("id"));
    Livro result = repository.acharPeloId(id);
    String resultJson = gson.toJson((result));
    ctx.status(200).json(resultJson);
  }

  private void deletarLivro(Context ctx) {
    Integer id = Integer.valueOf(ctx.pathParam("id"));
    service.deletarLivro(id);
    ctx.status(204);
  }
}
