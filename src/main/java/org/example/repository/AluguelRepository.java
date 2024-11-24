package org.example.repository;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.ConflictResponse;
import org.example.db.DatabaseConnection;
import org.example.model.Aluguel;
import org.example.model.Livro;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;


public class AluguelRepository {

  private final Connection connection;

  private final String sqlQuerySelectByAluguelId = """
          SELECT
              id,
              livro_id,
              estudante_matricula,
              inicio_reserva,
              dias,
              fim_estimado,
              fim_reserva,
              valor_estimado,
              valor_total
          FROM aluguel
          WHERE id = ? ;
      """;

  private final String sqlQuerySelectByEstudanteMatricula = """
          SELECT
              id,
              livro_id,
              estudante_matricula,
              inicio_reserva,
              dias,
              fim_estimado,
              fim_reserva,
              valor_estimado,
              valor_total
          FROM aluguel
          WHERE estudanteMatricula = ? ;
      """;

  private final String sqlQuerySelectMany = """
          SELECT
              id,
              livro_id,
              estudante_matricula,
              inicio_reserva,
              dias,
              fim_estimado,
              fim_reserva,
              valor_estimado,
              valor_total
          FROM aluguel
          ORDER BY id;
      """;

  private final String sqlQueryInsertOne = """
          INSERT INTO aluguel (
              livro_id,
              estudante_matricula,
              inicio_reserva,
              dias,
              fim_estimado,
              fim_reserva,
              valor_estimado,
              valor_total
          )
          VALUES (?, ?, ?, ?, ?, ?, ?, ?);
      """;

  private final String sqlUpdateById = """
          UPDATE aluguel
          SET livro_id            = ?,
              estudante_matricula = ?,
              inicio_reserva      = ?,
              dias                = ?,
              fim_estimado        = ?,
              fim_reserva         = ?,
              valor_estimado      = ?,
              valor_total         = ?
          WHERE id = ? ;
      """;

  private final String sqlQueryCountByEstudanteId = """
          SELECT
            COUNT(id) as count
          FROM aluguel
          WHERE estudante_matricula = ? ;
      """;

  private final String sqlQueryCountByLivroId = """
          SELECT
            COUNT(id) as count
           FROM aluguel
          WHERE livro_id = ? ;
      """;

  private final String sqlQueryDeleteByAluguelId = """
          DELETE FROM aluguel
          WHERE id = ? ;
      """;

  public AluguelRepository() {
    this.connection = DatabaseConnection.getConnection();
  }

  public List<Aluguel> listarAlugueis() {
    List<Aluguel> result = new LinkedList<>();

    try (Statement stmt = connection.createStatement()) {
      ResultSet rs = stmt.executeQuery(sqlQuerySelectMany);

      while (rs.next()) {
        Integer id = rs.getInt("id");
        Integer livroId = rs.getInt("livro_id");
        Integer estudanteMatricula = rs.getInt("estudante_matricula");
        Date inicioReserva = rs.getDate("inicio_reserva");
        int dias = rs.getInt("dias");
        Date fimEstimado = rs.getDate("fim_estimado");
        Date fimReserva = rs.getDate("fim_reserva");
        float valorEstimado = rs.getFloat("valor_estimado");
        float valorTotal = rs.getFloat("valor_total");

        result.add(
            new Aluguel(
                id,
                livroId,
                estudanteMatricula,
                inicioReserva,
                dias,
                fimEstimado,
                fimReserva,
                valorEstimado,
                valorTotal
            )
        );
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return result;
  }

  public Aluguel acharPeloId(Integer id) {
    Livro result = null;

    try (PreparedStatement stmt = connection.prepareStatement(sqlQuerySelectByAluguelId)) {
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        Integer livroId = rs.getInt("livro_id");
        Integer estudanteMatricula = rs.getInt("estudante_matricula");
        Date inicioReserva = rs.getDate("inicio_reserva");
        int dias = rs.getInt("dias");
        Date fimEstimado = rs.getDate("fim_estimado");
        Date fimReserva = rs.getDate("fim_reserva");
        float valorEstimado = rs.getFloat("valor_estimado");
        float valorTotal = rs.getFloat("valor_total");

        return new Aluguel(
            id,
            livroId,
            estudanteMatricula,
            inicioReserva,
            dias,
            fimEstimado,
            fimReserva,
            valorEstimado,
            valorTotal
        );
      } else throw new ConflictResponse("Livro não encontrado. id: " + id);

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Aluguel criarAluguel(Aluguel data) throws SQLException {
    try (PreparedStatement stmt = connection.prepareStatement(sqlQueryInsertOne, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setInt(1, data.livroId());
      stmt.setInt(2, data.estudanteMatricula());
      stmt.setDate(3, data.inicioReserva());
      stmt.setInt(4, data.dias());
      stmt.setDate(5, data.fimEstimado());
      stmt.setDate(6, data.fimReserva());
      stmt.setFloat(7, data.valorEstimado());
      stmt.setFloat(8, data.valorTotal());

      stmt.executeUpdate();
      ResultSet rs = stmt.getGeneratedKeys();

      if (rs.next()) {
        Integer id = rs.getInt(1);
        return new Aluguel(
            id,
            data.livroId(),
            data.estudanteMatricula(),
            data.inicioReserva(),
            data.dias(),
            data.fimEstimado(),
            data.fimReserva(),
            data.valorEstimado(),
            data.valorTotal()
        );
      }

    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Não foi possível cadastrar o Aluguel: " + data);
    }
    return null;
  }

  public Aluguel atualizarAluguel(Aluguel data) {
    try (PreparedStatement stmt = connection.prepareStatement(sqlUpdateById, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setInt(1, data.livroId());
      stmt.setInt(2, data.estudanteMatricula());
      stmt.setDate(3, data.inicioReserva());
      stmt.setInt(4, data.dias());
      stmt.setDate(5, data.fimEstimado());
      stmt.setDate(6, data.fimReserva());
      stmt.setFloat(7, data.valorEstimado());
      stmt.setFloat(8, data.valorTotal());
      stmt.setInt(9, data.id());

      stmt.executeUpdate();
      return acharPeloId(data.id());
    } catch (SQLException e) {
      e.printStackTrace();
      throw new BadRequestResponse("Não foi possível atualizar o Aluguel: " + data.id());
    }
  }

  public void deletarAluguel(Integer id) {
    try (PreparedStatement stmt = connection.prepareStatement(sqlQueryDeleteByAluguelId)) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
    } catch (Throwable e) {
      e.printStackTrace();
      throw new ConflictResponse("Nãofoi possível deletar o livro. id: " + id);
    }
  }

  public boolean checkAluguelExisteByLivroId(Integer livroId) {
    try (PreparedStatement stmt = connection.prepareStatement(sqlQueryCountByLivroId)) {
      stmt.setInt(1, livroId);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        int count = rs.getInt("count");
        return count > 0;
      } else return true;

    } catch (SQLException e) {
      e.printStackTrace();
      return true;
    }
  }

  public boolean checkAluguelExisteByEstudanteId(Integer estudanteId) {
    try (PreparedStatement stmt = connection.prepareStatement(sqlQueryCountByEstudanteId)) {
      stmt.setInt(1, estudanteId);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        int count = rs.getInt("count");
        return count > 0;
      } else return true;

    } catch (SQLException e) {
      e.printStackTrace();
      return true;
    }
  }
}
