package org.example.service;

import org.example.model.Aluguel;
import org.example.repository.AluguelRepository;
import org.example.repository.EstudanteRepository;
import org.example.repository.LivroRepository;

import javax.management.BadAttributeValueExpException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;


public class AluguelService {

  AluguelRepository repository;

  EstudanteRepository estudanteRepository;

  LivroRepository livroRepository;

  public AluguelService() {
    this.repository = new AluguelRepository();
    this.estudanteRepository = new EstudanteRepository();
    this.livroRepository = new LivroRepository();
  }

  private static Date calculateFinalDate(Date initialDate, int dias) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(initialDate);
    calendar.add(Calendar.DAY_OF_MONTH, dias);
    return new Date(calendar.getTimeInMillis());
  }

  public void deletarAluguel(Integer id) {
    repository.acharPeloId(id);
    repository.deletarAluguel(id);
  }

  public Aluguel criarAluguel(Aluguel data) throws SQLException, BadAttributeValueExpException {
    estudanteRepository.acharPelaMatricula(data.estudanteMatricula(), false);
    livroRepository.acharPeloId(data.livroId());

    if (data.inicioReserva().getTime() >= data.fimReserva().getTime())
      throw new BadAttributeValueExpException("Conflito de datas");

    return repository.criarAluguel(data);
  }

  public Aluguel atualizarEstudante(Integer aluguelId, Aluguel data) throws BadAttributeValueExpException, SQLException {
    Aluguel record = repository.acharPeloId(aluguelId);

    float valorFinal;
    if (data.fimReserva() != null) {
      if (data.inicioReserva().getTime() >= data.fimReserva().getTime()) {
        throw new BadAttributeValueExpException("Conflito de datas");
      }
      long diasDeDiferenca = ChronoUnit.DAYS.between(data.inicioReserva().toLocalDate(), data.fimReserva().toLocalDate());
    }

    Aluguel dataUpdate = new Aluguel(
        aluguelId,
        record.id(),
        record.estudanteMatricula(),
        data.inicioReserva() == null ? record.inicioReserva() : data.inicioReserva(),
        data.dias() == 0 ? record.dias() : data.dias(),
        data.fimEstimado() == null ? record.fimEstimado() : data.fimEstimado(),
        data.fimReserva() == null ? record.fimReserva() : data.fimReserva(),
        record.valorEstimado(),
        data.valorTotal() == 0 ? record.valorTotal() : data.valorTotal()

    );

    return repository.atualizarAluguel(dataUpdate);
  }
}
