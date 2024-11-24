package org.example.model;

import org.jetbrains.annotations.Nullable;

import java.sql.Date;


public record Aluguel(
        @Nullable Integer id,
        Integer livroId,
        Integer estudanteMatricula,
        Date inicioReserva,
        int dias,
        Date fimEstimado,
        Date fimReserva,
        float valorEstimado,
        float valorTotal
) {
}
