package br.ufpb.dcx.dsc.repositorios.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FiltroAutenticacaoJwt extends OncePerRequestFilter {

    private final UtilitarioJwt utilitarioJwt;
    private final ServicoDetalhesUsuario servicoDetalhesUsuario;

    public FiltroAutenticacaoJwt(UtilitarioJwt utilitarioJwt, ServicoDetalhesUsuario servicoDetalhesUsuario) {
        this.utilitarioJwt = utilitarioJwt;
        this.servicoDetalhesUsuario = servicoDetalhesUsuario;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest requisicao,
                                    HttpServletResponse resposta,
                                    FilterChain cadeiaFiltros) throws ServletException, IOException {
        final String cabecalhoAutorizacao = requisicao.getHeader("Authorization");
        String nomeUsuario = null;
        String token = null;

        if (cabecalhoAutorizacao != null && cabecalhoAutorizacao.startsWith("Bearer ")) {
            token = cabecalhoAutorizacao.substring(7);
            try {
                nomeUsuario = utilitarioJwt.extrairNomeUsuario(token);
            } catch (Exception excecao) {
                logger.debug("Falha ao extrair usuário do token", excecao);
            }
        }

        if (nomeUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails detalhesUsuario = this.servicoDetalhesUsuario.loadUserByUsername(nomeUsuario);
            if (utilitarioJwt.validarToken(token, detalhesUsuario)) {
                UsernamePasswordAuthenticationToken autenticacao = new UsernamePasswordAuthenticationToken(
                        detalhesUsuario,
                        null,
                        detalhesUsuario.getAuthorities()
                );
                autenticacao.setDetails(new WebAuthenticationDetailsSource().buildDetails(requisicao));
                SecurityContextHolder.getContext().setAuthentication(autenticacao);
            }
        }

        cadeiaFiltros.doFilter(requisicao, resposta);
    }
}