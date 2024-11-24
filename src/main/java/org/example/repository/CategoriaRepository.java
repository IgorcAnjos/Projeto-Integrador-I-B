package org.example.repository;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.ConflictResponse;
import io.javalin.http.NoContentResponse;
import io.javalin.http.NotFoundResponse;
import org.example.db.DatabaseConnection;
import org.example.model.Categoria;
import org.example.model.Estudante;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;


public class CategoriaRepository {

  private final Connection connection;

  private final String sqlQuerySelectById = """
          SELECT id, nome
          FROM categoria
          WHERE id = ? ;
      """;

  private final String sqlQuerySelectByname = """
          SELECT id, nome
          FROM categoria
          WHERE nome = ?
      """;

  private final String sqlQuerySelectMany = """
          SELECT id, nome
          FROM categoria
          ORDER BY id
      """;

  private final String sqlQueryInsertOne = """
          INSERT INTO categoria (nome)
          VALUES (?)
      """;

  private final String sqlUpdateById = """
          UPDATE categoria
          SET nome = ?
          WHERE id = ?;
      """;

  private final String sqlDeleteCategoriaById = """
          DELETE FROM categoria
          WHERE id = ?;
      """;

  public CategoriaRepository() {
    this.connection = DatabaseConnection.getConnection();
  }

  public List<Categoria> listarCategorias() {
    List<Categoria> result = new LinkedList<>();

    try (Statement stmt = connection.createStatement()) {
      ResultSet rs = stmt.executeQuery(sqlQuerySelectMany);

      while (rs.next()) {
        Integer id = rs.getInt("id");
        String nome = rs.getString("nome");

        result.add(new Categoria(id, nome));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new NoContentResponse();
    }

    return result;
  }

  public Categoria acharPeloId(Integer id) throws NotFoundResponse {
    Estudante result;
    try (PreparedStatement stmt = connection.prepareStatement(sqlQuerySelectById)) {
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        String nome = rs.getString("nome");

        return new Categoria(id, nome);
      } else throw new ConflictResponse("Categoria não encontrado. id: " + id);

    } catch (SQLException e) {
      e.printStackTrace();
      throw new ConflictResponse("Erro na consulta da categoria: " + id);
    }
  }

  public Boolean categoriaExistePeloNome(String nome, Integer id) throws NotFoundResponse {
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

  public Categoria criarCategoria(Categoria data) {
    try (PreparedStatement stmt = connection.prepareStatement(sqlQueryInsertOne, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setString(1, data.nome());

      stmt.executeUpdate();
      ResultSet resultSet = stmt.getGeneratedKeys();

      if (resultSet.next()) {
        Integer id = resultSet.getInt(1);
        return acharPeloId(id);
      } else {
        throw new BadRequestResponse("Não foi possível criar a categoria: " + data);
      }

    } catch (SQLException e) {
      e.printStackTrace();
      throw new BadRequestResponse("Não foi possível criar a categoria: " + data);
    }

  }

  public Categoria atualizarCategoria(Categoria data) {
    try (PreparedStatement stmt = connection.prepareStatement(sqlUpdateById, Statement.RETURN_GENERATED_KEYS)) {
      assert data.id() != null;
      stmt.setString(1, data.nome());
      stmt.setInt(2, data.id());

      stmt.executeUpdate();
      ResultSet resultSet = stmt.getGeneratedKeys();

      return acharPeloId(data.id());
    } catch (SQLException e) {
      e.printStackTrace();
      throw new BadRequestResponse("Não foi possível atualizar categoria: " + data);
    }

  }

  public void deletarAluguel(Integer id) {
    try (PreparedStatement stmt = connection.prepareStatement(sqlDeleteCategoriaById)) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
    } catch (Throwable e) {
      e.printStackTrace();
      throw new ConflictResponse("Livro não encontrado. id: " + id);
    }
  }
}
