package br.ufpb.dcx.dsc.repositorios.repository;

import br.ufpb.dcx.dsc.repositorios.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}