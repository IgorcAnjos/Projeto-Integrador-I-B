package org.example.controller;

import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import org.example.model.Aluguel;
import org.example.repository.AluguelRepository;
import org.example.service.AluguelService;

import javax.management.BadAttributeValueExpException;
import java.sql.SQLException;
import java.util.List;


public class AluguelController {

  AluguelService service;

  AluguelRepository repository;

  Gson gson;

  public AluguelController(Javalin server) {
    this.repository = new AluguelRepository();
    this.service = new AluguelService();
    this.gson = new Gson();

    // "/estudante"
    server.get("/aluguel", this::listarAlugueis);
    server.post("/aluguel", this::criarAluguel);

    //"/estudante/:id"
    server.get("/aluguel/{id}", this::acharAluguel);
    server.put("/aluguel/{id}", this::atualizarAluguel);
    server.delete("/aluguel/{id}", this::deletarAluguel);
  }

  private void criarAluguel(Context ctx) throws SQLException, BadAttributeValueExpException {
    Aluguel data;
    try {
      data = ctx.bodyAsClass(Aluguel.class);
    } catch (Throwable e) {
      throw new BadRequestResponse("payload invalido");
    }

    System.out.println(data);
    Aluguel result = service.criarAluguel(data);
    String resultJson = gson.toJson((result));
    ctx.status(201).json(resultJson);
  }

  private void atualizarAluguel(Context ctx) throws BadAttributeValueExpException, SQLException {
    Aluguel data;
    int aluguelId;
    try {
      data = ctx.bodyAsClass(Aluguel.class);
      aluguelId = Integer.parseInt(ctx.pathParam("id"));
    } catch (Throwable e) {
      throw new BadRequestResponse("payload invalido");
    }

    Aluguel result = service.atualizarEstudante(aluguelId, data);
    String resultJson = gson.toJson((result));
    ctx.status(200).json(resultJson);
  }

  private void listarAlugueis(Context ctx) {
    List<Aluguel> result = repository.listarAlugueis();
    String resultJson = gson.toJson((result));
    ctx.status(200).json(resultJson);
  }

  private void acharAluguel(Context ctx) {
    Integer id = Integer.valueOf(ctx.pathParam("id"));
    Aluguel result = repository.acharPeloId(id);
    String resultJson = gson.toJson((result));
    ctx.status(200).json(resultJson);
  }

  private void deletarAluguel(Context ctx) {
    Integer id = Integer.valueOf(ctx.pathParam("id"));
    service.deletarAluguel(id);
    ctx.status(204);
  }
}
