package br.ufpb.dcx.dsc.repositorios.repository;

import br.ufpb.dcx.dsc.repositorios.models.Organizacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizacaoRepository extends JpaRepository<Organizacao, Long> {
}
