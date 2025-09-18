package br.ufpb.dcx.dsc.repositorios.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class ConfiguracaoSeguranca {

    private final FiltroAutenticacaoJwt filtroAutenticacaoJwt;
    private final ServicoDetalhesUsuario servicoDetalhesUsuario;
    private final FiltroLimiteTaxa filtroLimiteTaxa;

    public ConfiguracaoSeguranca(FiltroAutenticacaoJwt filtroAutenticacaoJwt,
                                 ServicoDetalhesUsuario servicoDetalhesUsuario,
                                 FiltroLimiteTaxa filtroLimiteTaxa) {
        this.filtroAutenticacaoJwt = filtroAutenticacaoJwt;
        this.servicoDetalhesUsuario = servicoDetalhesUsuario;
        this.filtroLimiteTaxa = filtroLimiteTaxa;
    }

    @Bean
    public SecurityFilterChain cadeiaSeguranca(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessao -> sessao.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(autorizacao -> autorizacao
                        .requestMatchers("/api/login", "/api/change-password").permitAll()
                        .anyRequest().authenticated())
                .authenticationProvider(provedorAutenticacao())
                .addFilterBefore(filtroLimiteTaxa, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(filtroAutenticacaoJwt, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider provedorAutenticacao() {
        DaoAuthenticationProvider provedor = new DaoAuthenticationProvider();
        provedor.setUserDetailsService(servicoDetalhesUsuario);
        provedor.setPasswordEncoder(codificadorSenha());
        return provedor;
    }

    @Bean
    public PasswordEncoder codificadorSenha() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager gerenciadorAutenticacao(AuthenticationConfiguration configuracao) throws Exception {
        return configuracao.getAuthenticationManager();
    }
}