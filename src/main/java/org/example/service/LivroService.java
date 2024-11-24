package org.example.service;

import io.javalin.http.ConflictResponse;
import org.example.model.Livro;
import org.example.repository.AluguelRepository;
import org.example.repository.AutorRepository;
import org.example.repository.CategoriaRepository;
import org.example.repository.LivroRepository;


public class LivroService {

  private final LivroRepository livroRepo;

  private final CategoriaRepository categoriaRepo;

  private final AutorRepository autorRepo;

  private final AluguelRepository aluguelRepo;

  public LivroService() {
    livroRepo = new LivroRepository();
    categoriaRepo = new CategoriaRepository();
    autorRepo = new AutorRepository();
    aluguelRepo = new AluguelRepository();
  }

  public Livro criarLivro(Livro data) {
    checkDataToMaintain(data);
    return livroRepo.criarLivro(data);
  }

  public Livro atualizarLivro(int id, Livro data) {
    Livro record = livroRepo.acharPeloId(id);
    System.out.println("AAAAAAAAAAAAAAAA" + id);
    Livro dataUpdate = new Livro(
        id,
        data.autorId() == null ? record.autorId() : data.autorId(),
        data.categoriaId() == null ? record.categoriaId() : data.categoriaId(),
        data.titulo() == null ? record.titulo() : data.titulo(),
        data.descricao() == null ? record.descricao() : data.descricao(),
        data.linkImage() == null ? record.linkImage() : data.linkImage(),
        data.dataLancamento() == null ? record.dataLancamento() : data.dataLancamento(),
        data.dataCriacao() == null ? record.dataCriacao() : data.dataCriacao(),
        data.quantidadePaginas() == null ? record.quantidadePaginas() : data.quantidadePaginas(),
        data.valorDiaAlugado() == null ? record.valorDiaAlugado() : data.valorDiaAlugado(),
        data.valorDiaMulta() == null ? record.valorDiaMulta() : data.valorDiaMulta()
    );
    checkDataToMaintain(dataUpdate);
    return livroRepo.atualizarAluguel(dataUpdate);
  }

  private void checkDataToMaintain(Livro data) {
    categoriaRepo.acharPeloId(data.categoriaId());
    autorRepo.acharPeloId(data.autorId());
  }

  public void deletarLivro(Integer id) {
    livroRepo.acharPeloId(id);
    System.out.println("sla meu");

    if (aluguelRepo.checkAluguelExisteByLivroId(id))
      throw new ConflictResponse("Não foi possível deletar o Livro: Há Alugueis registrados");

    livroRepo.deletarLivro(id);
  }
}
