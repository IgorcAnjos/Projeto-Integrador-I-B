package org.example.db;

import java.sql.*;


public class DatabaseConnection {

  private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";

  private static final String USER = "root";

  private static final String PASSWORD = "dados@123";

  public static Connection getConnection() {
    try {
      return DriverManager.getConnection(URL, USER, PASSWORD);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("Erro ao conectar ao banco de dados");
    }
  }

  public static void runMigration() {
    try (Statement stmt = DatabaseConnection.getConnection().createStatement()) {
      String[] queries = Migration.initalizeDatabase.split(";");
      for (String query : queries) {
        if (!query.trim().isEmpty()) {
          stmt.execute(query.trim());
        }
      }
      System.out.println("Banco de dados inicializado com sucesso!");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static void runSeed() {
    boolean databaseIsEmpty = checkDatabaseIsEmpty();

    if (!databaseIsEmpty) {
      System.out.println("Banco de dados já populado!");
      return;
    }
    try (Statement stmt = DatabaseConnection.getConnection().createStatement()) {
      String[] queries = Migration.seedDatabase.split(";");

      for (String query : queries) {
        if (!query.trim().isEmpty()) {
          stmt.execute(query.trim());
        }
      }
      System.out.println("Banco de dados populado com sucesso!");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private static boolean checkDatabaseIsEmpty() {
    try (Statement stmt = DatabaseConnection.getConnection().createStatement()) {
      ResultSet rs = stmt.executeQuery("""
            SELECT CASE
               WHEN EXISTS (SELECT 1 FROM aluguel LIMIT 1)
                   OR EXISTS (SELECT 1 FROM autor LIMIT 1)
                   OR EXISTS (SELECT 1 FROM categoria LIMIT 1)
                   OR EXISTS (SELECT 1 FROM estudante LIMIT 1)
                   OR EXISTS (SELECT 1 FROM livro LIMIT 1)
               THEN 1
               ELSE 0
            END AS has_records;
          """);
      if (rs.next())
        return rs.getInt("has_records") == 0;
      else return false;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
}
