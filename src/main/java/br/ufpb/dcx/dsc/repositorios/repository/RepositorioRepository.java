package br.ufpb.dcx.dsc.repositorios.repository;

import br.ufpb.dcx.dsc.repositorios.models.Repositorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositorioRepository extends JpaRepository<Repositorio, Long> {

}
