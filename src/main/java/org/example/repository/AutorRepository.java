package org.example.repository;

import io.javalin.http.ConflictResponse;
import io.javalin.http.NoContentResponse;
import io.javalin.http.NotFoundResponse;
import org.example.db.DatabaseConnection;
import org.example.model.Autor;
import org.example.model.Estudante;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;


public class AutorRepository {

  private final Connection connection;

  private final String sqlQuerySelectById = """
          SELECT id, nome
          FROM autor
          WHERE id = ? ;
      """;

  private final String sqlQuerySelectByname = """
          SELECT id, nome
          FROM autor
          WHERE nome = ?
      """;

  private final String sqlQuerySelectMany = """
          SELECT id, nome
          FROM autor
          ORDER BY id
      """;

  private final String sqlQueryInsertOne = """
          INSERT INTO autor (nome)
          VALUES (?)
      """;

  private final String sqlUpdateById = """
          UPDATE autor
          SET nome = ?
          WHERE id = ?;
      """;

  private final String sqlDeleteEstudanteById = """
          DELETE FROM autor
          WHERE id = ? ;
      """;

  public AutorRepository() {
    this.connection = DatabaseConnection.getConnection();
  }

  public List<Autor> listarAutores() {
    List<Autor> result = new LinkedList<>();

    try (Statement stmt = connection.createStatement()) {
      ResultSet rs = stmt.executeQuery(sqlQuerySelectMany);
      ResultSetMetaData metaData = rs.getMetaData();
      int columnCount = metaData.getColumnCount();

      while (rs.next()) {
        Integer id = rs.getInt("id");
        String nome = rs.getString("nome");

        result.add(new Autor(id, nome));
      }

    } catch (SQLException e) {
      e.printStackTrace();
      throw new NoContentResponse();
    }

    return result;
  }

  public Autor acharPeloId(Integer id) throws NotFoundResponse {
    Estudante result = null;

    try (PreparedStatement stmt = connection.prepareStatement(sqlQuerySelectById)) {
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        String nome = rs.getString("nome");

        return new Autor(id, nome);
      } else throw new ConflictResponse("Autor não encontrado. id: " + id);

    } catch (SQLException e) {
      e.printStackTrace();
      throw new ConflictResponse("Erro na consulta do estudante: " + id);
    }
  }

  public Boolean autorExistePeloNome(String nome, Integer id) throws NotFoundResponse {
    Estudante result = null;
    boolean idEnviado = id != null && id > 0;

    String sql = idEnviado ? sqlQuerySelectByname + """
            AND id <> ?
        """ : sqlQuerySelectByname;

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, nome);
      if (idEnviado) stmt.setInt(2, id);
      ResultSet rs = stmt.executeQuery();

      return rs.next();

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public Autor criarAutor(Autor data) {
    try (PreparedStatement stmt = connection.prepareStatement(sqlQueryInsertOne, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setString(1, data.nome());

      stmt.executeUpdate();
      ResultSet resultSet = stmt.getGeneratedKeys();

      if (resultSet.next()) {
        Integer id = resultSet.getInt(1);
        return acharPeloId(id);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  public Autor atualizarAutor(Autor data) {
    try (PreparedStatement stmt = connection.prepareStatement(sqlUpdateById, Statement.RETURN_GENERATED_KEYS)) {
      assert data.id() != null;
      stmt.setString(1, data.nome());
      stmt.setInt(2, data.id());

      stmt.executeUpdate();
      ResultSet resultSet = stmt.getGeneratedKeys();

      return acharPeloId(data.id());
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  public void deletarAutor(Integer id) {
    try (PreparedStatement stmt = connection.prepareStatement(sqlDeleteEstudanteById)) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
    } catch (Throwable e) {
      e.printStackTrace();
      throw new ConflictResponse("Não foi possível deletar o estudante: " + id);
    }
  }
}
