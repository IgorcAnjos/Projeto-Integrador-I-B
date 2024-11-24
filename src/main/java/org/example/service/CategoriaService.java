package org.example.service;

import io.javalin.http.ConflictResponse;
import org.example.model.Categoria;
import org.example.repository.CategoriaRepository;
import org.example.repository.LivroRepository;
import org.jetbrains.annotations.Nullable;


public class CategoriaService {

  CategoriaRepository categoriaRepo;

  LivroRepository livroRepo;

  public CategoriaService() {
    this.categoriaRepo = new CategoriaRepository();
    this.livroRepo = new LivroRepository();
  }

  public Categoria atualizarCategoria(Integer id, Categoria data) {
    checkCategoriaExists(data.nome(), id);
    Categoria record = categoriaRepo.acharPeloId(id);

    Categoria updateData = new Categoria(id, data.nome());
    return categoriaRepo.atualizarCategoria(updateData);
  }

  public Categoria criarCategoria(Categoria data) {
    checkCategoriaExists(data.nome(), null);
    return categoriaRepo.criarCategoria(data);
  }

  private void checkCategoriaExists(String nome, @Nullable Integer id) {
    boolean exist = categoriaRepo.categoriaExistePeloNome(nome, id);
    if (exist) throw new ConflictResponse("Categoria " + nome + " já existe");
  }

  public void deletarCategoria(Integer id) {
    categoriaRepo.acharPeloId(id);
    if (livroRepo.checkLivroExisteByCategoriaId(id))
      throw new ConflictResponse("Não foi possícel deletar categoria: Há livros cadastrados.");
    
    categoriaRepo.deletarAluguel(id);
  }
}
