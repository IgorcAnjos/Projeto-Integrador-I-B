package org.example.service;

import io.javalin.http.ConflictResponse;
import org.example.model.Estudante;
import org.example.repository.AluguelRepository;
import org.example.repository.EstudanteRepository;
import org.example.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class EstudanteService {

  private final EstudanteRepository estudanteRepo;

  private final AluguelRepository aluguelRepo;

  public EstudanteService() {
    estudanteRepo = new EstudanteRepository();
    aluguelRepo = new AluguelRepository();
  }

  public List<Estudante> listarEstudantes() {
    return estudanteRepo.listarEstudantes();
  }

  public Estudante criarEstudante(@NotNull Estudante data) {
    assert data.senha() != null;
    checkEstudanteExiste(data.email(), data.matricula());
    Estudante dataInsert = new Estudante(null, data.nome(), data.email(), Utils.generateSHA265(data.senha()), null);
    return estudanteRepo.criarEstudante(dataInsert);
  }

  public Estudante atualizarEstudante(Integer matricula, Estudante data) {
    Estudante record = estudanteRepo.acharPelaMatricula(matricula, true);
    checkEstudanteExiste(data.email(), data.matricula());
    Estudante dataInsert = new Estudante(
        matricula,
        data.nome() == null ? record.nome() : data.nome(),
        data.email() == null ? record.email() : data.email(),
        data.senha() == null ? record.senha() : Utils.generateSHA265(data.senha()),
        null
    );
    return estudanteRepo.atualizarEstudante(dataInsert);
  }

  private void checkEstudanteExiste(String email, @Nullable Integer matricula) {
    Boolean existe = estudanteRepo.estudanteExiste(email, matricula);
    if (existe) throw new ConflictResponse("Já existe um estudante com esse email");
  }

  public void deletarEstudante(Integer matricula) {
    estudanteRepo.acharPelaMatricula(matricula, false);
    if (aluguelRepo.checkAluguelExisteByEstudanteId(matricula))
      throw new ConflictResponse("Não foi possível deletar Estudante: Há alugueis cadastrados");
    estudanteRepo.deletarEstudante(matricula);
  }
}
