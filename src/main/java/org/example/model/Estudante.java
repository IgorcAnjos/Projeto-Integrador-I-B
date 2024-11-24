package org.example.model;


import org.jetbrains.annotations.Nullable;

import java.util.Date;

public record Estudante(@Nullable Integer matricula, String nome, String email, @Nullable String senha, Date data_criacao) {
}
