package org.example.repository;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.ConflictResponse;
import io.javalin.http.NotFoundResponse;
import org.example.db.DatabaseConnection;
import org.example.model.Livro;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;


public class LivroRepository {

  private final Connection connection;

  private final String sqlQuerySelectByLivroId = """
          SELECT
               id,
               autor_id,
               categoria_id,
               titulo,
               descricao,
               link_image,
               data_lancamento,
               data_criacao,
               quantidade_paginas,
               valor_dia_alugado,
               valor_dia_multa
          FROM livro
          WHERE id = ? ;
      """;

  private final String sqlQuerySelectMany = """
          SELECT
               id,
               autor_id,
               categoria_id,
               titulo,
               descricao,
               link_image,
               data_lancamento,
               data_criacao,
               quantidade_paginas,
               valor_dia_alugado,
               valor_dia_multa
          FROM livro
          ORDER BY id;
      """;

  private final String sqlQueryCountLivroByAutorId = """
          SELECT
               COUNT(id) as count
          FROM livro
          WHERE autor_id = ? ;
      """;

  private final String sqlQueryCountLivroByCategoriaId = """
          SELECT
               COUNT(id) as count
          FROM livro
          WHERE categoria_id = ? ;
      """;

  private final String sqlQueryInsertOne = """
          INSERT INTO livro (
            autor_id,
            categoria_id,
            titulo,
            descricao,
            link_image,
            data_lancamento,
            quantidade_paginas,
            valor_dia_alugado,
            valor_dia_multa
          )
          VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
      """;

  private final String sqlUpdateById = """
          UPDATE livro
          SET
            autor_id           = ?,
            categoria_id       = ?,
            titulo             = ?,
            descricao          = ?,
            link_image         = ?,
            data_lancamento    = ?,
            data_criacao       = ?,
            quantidade_paginas = ?,
            valor_dia_alugado  = ?,
            valor_dia_multa    = ?
          WHERE id = ? ;
      """;

  private final String sqlDeleteByLivroId = """
          DELETE FROM livro
          WHERE id = ? ;
      """;

  public LivroRepository() {
    this.connection = DatabaseConnection.getConnection();
  }

  public Livro acharPeloId(Integer id) {

    try (PreparedStatement stmt = connection.prepareStatement(sqlQuerySelectByLivroId)) {
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        Integer autorId = rs.getInt("autor_id");
        Integer categoriaId = rs.getInt("categoria_id");
        String titulo = rs.getString("titulo");
        String descricao = rs.getString("descricao");
        String linkImage = rs.getString("link_image");
        Date dataLancamento = rs.getDate("data_lancamento");
        Date dataCriacao = rs.getDate("data_criacao");
        Integer quantidadePaginas = rs.getInt("quantidade_paginas");
        float valorDiaAlugado = rs.getFloat("valor_dia_alugado");
        float valorDiaMulta = rs.getFloat("valor_dia_multa");

        return new Livro(
            id,
            autorId,
            categoriaId,
            titulo,
            descricao,
            linkImage,
            dataLancamento,
            dataCriacao,
            quantidadePaginas,
            valorDiaAlugado,
            valorDiaMulta
        );
      } else throw new NotFoundResponse("Livro não encontrado. id: " + id);

    } catch (SQLException e) {
      e.printStackTrace();
      throw new NotFoundResponse("Livro não encontrado. id: " + id);
    }
  }

  public List<Livro> listarLivros() {
    List<Livro> result = new LinkedList<>();

    try (Statement stmt = connection.createStatement()) {
      ResultSet rs = stmt.executeQuery(sqlQuerySelectMany);

      while (rs.next()) {
        Integer id = rs.getInt("id");
        Integer autorId = rs.getInt("autor_id");
        Integer categoriaId = rs.getInt("categoria_id");
        String titulo = rs.getString("titulo");
        String descricao = rs.getString("descricao");
        String linkImage = rs.getString("link_image");
        Date dataLancamento = rs.getDate("data_lancamento");
        Date dataCriacao = rs.getDate("data_criacao");
        Integer quantidadePaginas = rs.getInt("quantidade_paginas");
        float valorDiaAlugado = rs.getFloat("valor_dia_alugado");
        float valorDiaMulta = rs.getFloat("valor_dia_multa");

        result.add(new Livro(
                id,
                autorId,
                categoriaId,
                titulo,
                descricao,
                linkImage,
                dataLancamento,
                dataCriacao,
                quantidadePaginas,
                valorDiaAlugado,
                valorDiaMulta
            )
        );
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return result;
  }

  public Livro criarLivro(Livro data) {
    try (PreparedStatement stmt = connection.prepareStatement(sqlQueryInsertOne, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setInt(1, data.autorId());
      stmt.setInt(2, data.categoriaId());
      stmt.setString(3, data.titulo());
      stmt.setString(4, data.descricao());
      stmt.setString(5, data.linkImage());
      stmt.setDate(6, data.dataLancamento());
      stmt.setInt(7, data.quantidadePaginas());
      stmt.setFloat(8, data.valorDiaAlugado() == null ? 0 : data.valorDiaAlugado());
      stmt.setFloat(9, data.valorDiaMulta() == null ? 0 : data.valorDiaMulta());

      stmt.executeUpdate();
      ResultSet rs = stmt.getGeneratedKeys();

      if (rs.next()) {
        Integer id = rs.getInt(1);
        return acharPeloId(id);
      } else {
        throw new BadRequestResponse("Não foi possível cadastrar livro");
      }

    } catch (Throwable e) {
      e.printStackTrace();
      throw new BadRequestResponse("Não foi possível cadastrar livro");
    }
  }

  public Livro atualizarAluguel(Livro data) {
    try (PreparedStatement stmt = connection.prepareStatement(sqlUpdateById)) {
      assert data.id() != null;
      stmt.setInt(1, data.autorId());
      stmt.setInt(2, data.categoriaId());
      stmt.setString(3, data.titulo());
      stmt.setString(4, data.descricao());
      stmt.setString(5, data.linkImage());
      stmt.setDate(6, data.dataLancamento());
      stmt.setDate(7, data.dataCriacao());
      stmt.setInt(8, data.quantidadePaginas());
      stmt.setFloat(9, data.valorDiaAlugado() == null ? 0 : data.valorDiaAlugado());
      stmt.setFloat(10, data.valorDiaMulta() == null ? 0 : data.valorDiaMulta());
      stmt.setInt(11, data.id());

      stmt.executeUpdate();
      return acharPeloId(data.id());
    } catch (Throwable e) {
      e.printStackTrace();
      throw new BadRequestResponse("Não foi possível atualizar livro");
    }
  }

  public void deletarLivro(Integer id) {
    try (PreparedStatement stmt = connection.prepareStatement(sqlDeleteByLivroId)) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
    } catch (Throwable e) {
      e.printStackTrace();
      throw new ConflictResponse("Não foi possível deletar o livro. id: " + id);
    }
  }

  public boolean checkLivroExisteByAutorId(Integer autorId) {
    try (PreparedStatement stmt = connection.prepareStatement(sqlQueryCountLivroByAutorId)) {
      stmt.setInt(1, autorId);
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

  public boolean checkLivroExisteByCategoriaId(Integer categoriaId) {
    try (PreparedStatement stmt = connection.prepareStatement(sqlQueryCountLivroByCategoriaId)) {
      stmt.setInt(1, categoriaId);
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
