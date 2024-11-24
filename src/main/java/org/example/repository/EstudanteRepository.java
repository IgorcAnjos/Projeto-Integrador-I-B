package org.example.repository;

import io.javalin.http.ConflictResponse;
import io.javalin.http.NotFoundResponse;
import org.example.db.DatabaseConnection;
import org.example.model.Estudante;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;


public class EstudanteRepository {

  private final Connection connection;

  private final String sqlQuerySelectByMatricula = """
          SELECT matricula, nome, email, senha, data_criacao
          FROM estudante
          WHERE matricula = ? ;
      """;

  private final String sqlQuerySelectByEmail = """
          SELECT matricula, nome, email, data_criacao
          FROM estudante
          WHERE email = ?
      """;

  private final String sqlQuerySelectMany = """
          SELECT matricula, nome, email, data_criacao
          FROM estudante
          ORDER BY matricula;
      """;

  private final String sqlQueryInsertOne = """
          INSERT INTO estudante (nome, email, senha)
          VALUES (?, ?, ?)
      """;

  private final String sqlUpdateByMatricula = """
          UPDATE estudante
          SET nome = ?,
              email = ?,
              senha = ?
          WHERE matricula = ?;
      """;

  private final String sqlDeleteByEstudanteId = """
          DELETE FROM estudante
          WHERE matricula = ? ;
      """;

  public EstudanteRepository() {
    this.connection = DatabaseConnection.getConnection();
  }

  public List<Estudante> listarEstudantes() {
    List<Estudante> result = new LinkedList<>();

    try (Statement stmt = connection.createStatement()) {
      ResultSet rs = stmt.executeQuery(sqlQuerySelectMany);
      ResultSetMetaData metaData = rs.getMetaData();
      int columnCount = metaData.getColumnCount();

      while (rs.next()) {
        Integer matricula = rs.getInt("matricula");
        String nome = rs.getString("nome");
        String email = rs.getString("email");
        Date data_criacao = rs.getDate("data_criacao");

        result.add(new Estudante(matricula, nome, email, null, data_criacao));
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return result;
  }

  public Estudante acharPelaMatricula(Integer matricula, Boolean voltarSenha) throws NotFoundResponse {
    Estudante result = null;

    try (PreparedStatement stmt = connection.prepareStatement(sqlQuerySelectByMatricula)) {
      stmt.setInt(1, matricula);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        String nome = rs.getString("nome");
        String email = rs.getString("email");
        String senha = rs.getString("senha");
        Date dataCriacao = rs.getDate("data_criacao");

        return new Estudante(matricula, nome, email, voltarSenha ? senha : null, dataCriacao);
      } else throw new NotFoundResponse("Estudante não encontrado. matricula: " + matricula);

    } catch (SQLException e) {
      e.printStackTrace();
      throw new NotFoundResponse("Erro na consulta do estudante: " + matricula);
    }
  }

  public Boolean estudanteExiste(String email, @Nullable Integer matricula) {
    Estudante result = null;
    Boolean matriculaEnviada = matricula != null && matricula > 0;

    String sql = matriculaEnviada ? sqlQuerySelectByEmail + """
            AND matricula <> ?
        """ : sqlQuerySelectByEmail;

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, email);
      if (matriculaEnviada) stmt.setInt(2, matricula);
      ResultSet rs = stmt.executeQuery();

      return rs.next();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public Estudante criarEstudante(@NotNull Estudante data) {
    try (PreparedStatement stmt = connection.prepareStatement(sqlQueryInsertOne, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setString(1, data.nome());
      stmt.setString(2, data.email());
      stmt.setString(3, data.senha());

      stmt.executeUpdate();
      ResultSet resultSet = stmt.getGeneratedKeys();

      if (resultSet.next()) {
        Integer matricula = resultSet.getInt(1);
        return acharPelaMatricula(matricula, false);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  public Estudante atualizarEstudante(@NotNull Estudante data) {
    try (PreparedStatement stmt = connection.prepareStatement(sqlUpdateByMatricula, Statement.RETURN_GENERATED_KEYS)) {
      assert data.matricula() != null;
      stmt.setString(1, data.nome());
      stmt.setString(2, data.email());
      stmt.setString(3, data.senha());
      stmt.setInt(4, data.matricula());

      stmt.executeUpdate();
      ResultSet resultSet = stmt.getGeneratedKeys();

      return acharPelaMatricula(data.matricula(), false);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  public void deletarEstudante(Integer id) {
    try (PreparedStatement stmt = connection.prepareStatement(sqlDeleteByEstudanteId)) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
    } catch (Throwable e) {
      e.printStackTrace();
      throw new ConflictResponse("Não foi possível deletar o estudante. id: " + id);
    }
  }
}
