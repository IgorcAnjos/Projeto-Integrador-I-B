package org.example.model;

import org.jetbrains.annotations.Nullable;

import java.sql.Date;


public record Livro(
    Integer id,
    Integer autorId,
    Integer categoriaId,
    String titulo,
    String descricao,
    String linkImage,
    Date dataLancamento,
    @Nullable Date dataCriacao,
    Integer quantidadePaginas,
    @Nullable Float valorDiaAlugado,
    @Nullable Float valorDiaMulta
) {

}
