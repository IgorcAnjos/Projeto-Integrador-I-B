package org.example.service;

import io.javalin.http.ConflictResponse;
import org.example.model.Autor;
import org.example.repository.AutorRepository;
import org.example.repository.LivroRepository;
import org.jetbrains.annotations.Nullable;


public class AutorService {

  AutorRepository autorRepo;

  LivroRepository livroRepo;

  public AutorService() {
    this.autorRepo = new AutorRepository();
    this.livroRepo = new LivroRepository();
  }

  public Autor atualizarAutor(Integer id, Autor data) {
    checkAutorExists(data.nome(), id);
    Autor record = autorRepo.acharPeloId(id);

    Autor updateData = new Autor(id, data.nome());
    return autorRepo.atualizarAutor(updateData);
  }

  public Autor criarAutor(Autor data) {
    checkAutorExists(data.nome(), null);
    return autorRepo.criarAutor(data);
  }

  private void checkAutorExists(String nome, @Nullable Integer id) {
    boolean exist = autorRepo.autorExistePeloNome(nome, id);
    if (exist) throw new ConflictResponse("Autor " + nome + " já existe");
  }

  public void deletarAutor(Integer id) {
    autorRepo.acharPeloId(id);
    if (livroRepo.checkLivroExisteByAutorId(id))
      throw new ConflictResponse("Não foi possícel deletar o autor: Há livros cadastrados.");

    autorRepo.deletarAutor(id);
  }
}
