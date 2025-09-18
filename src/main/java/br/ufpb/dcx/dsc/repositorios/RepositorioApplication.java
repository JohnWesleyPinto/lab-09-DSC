package br.ufpb.dcx.dsc.repositorios;

import br.ufpb.dcx.dsc.repositorios.models.User;
import br.ufpb.dcx.dsc.repositorios.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RepositorioApplication {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


    @Bean
    public CommandLineRunner carregarAdministrador(UserService servicoUsuario) {
        return args -> servicoUsuario.buscarPorNomeUsuario("admin").orElseGet(() -> {
            User administrador = new User();
            administrador.setNome("Administrador");
            administrador.setEmail("admin@example.com");
            administrador.setUsername("admin");
            administrador.setPassword("admin123");
            administrador.setRole("ROLE_ADMIN");
            return servicoUsuario.createUser(administrador);
        });
    }
    public static void main(String[] args) {
        SpringApplication.run(RepositorioApplication.class, args);
    }
}
