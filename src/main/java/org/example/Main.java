package org.example;

import io.javalin.Javalin;
import org.example.controller.*;
import org.example.db.DatabaseConnection;


public class Main {

  public static void main(String[] args) {
    Javalin app = Javalin.create().start(8080);
    DatabaseConnection.runMigration();
    DatabaseConnection.runSeed();
    
    new EstudanteController(app);
    new AluguelController(app);
    new AutorController(app);
    new CategoriaController(app);
    new LivroController(app);
  }
}
