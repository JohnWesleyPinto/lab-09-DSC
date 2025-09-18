package br.ufpb.dcx.dsc.repositorios.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FiltroLimiteTaxa extends OncePerRequestFilter {

    private final Map<String, LimitadorSimples> limitadores = new ConcurrentHashMap<>();
    private final int maximoRequisicoes;
    private final Duration janela;

    public FiltroLimiteTaxa(@Value("${rate.limit.requests:20}") int maximoRequisicoes,
                            @Value("${rate.limit.window.seconds:60}") long janelaEmSegundos) {
        this.maximoRequisicoes = maximoRequisicoes;
        this.janela = Duration.ofSeconds(janelaEmSegundos);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest requisicao,
                                    HttpServletResponse resposta,
                                    FilterChain cadeiaFiltros) throws ServletException, IOException {
        String chave = requisicao.getRemoteAddr();
        LimitadorSimples limitador = limitadores.computeIfAbsent(chave, k -> new LimitadorSimples(maximoRequisicoes, janela));
        if (!limitador.permitirRequisicao()) {
            resposta.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            resposta.setHeader("Retry-After", String.valueOf(janela.getSeconds()));
            resposta.setContentType(MediaType.APPLICATION_JSON_VALUE);
            resposta.setCharacterEncoding("UTF-8");
            resposta.getWriter().write("{\"status\":429,\"message\":\"Muitas requisições. Tente novamente mais tarde.\"}");
            resposta.getWriter().flush();
            return;
        }
        cadeiaFiltros.doFilter(requisicao, resposta);
    }

    private static final class LimitadorSimples {
        private final int maximoRequisicoes;
        private final Duration janela;
        private Instant inicioJanela;
        private int quantidadeRequisicoes;

        private LimitadorSimples(int maximoRequisicoes, Duration janela) {
            this.maximoRequisicoes = maximoRequisicoes;
            this.janela = janela;
            this.inicioJanela = Instant.now();
            this.quantidadeRequisicoes = 0;
        }

        private synchronized boolean permitirRequisicao() {
            Instant agora = Instant.now();
            if (agora.isAfter(inicioJanela.plus(janela))) {
                inicioJanela = agora;
                quantidadeRequisicoes = 0;
            }

            if (quantidadeRequisicoes >= maximoRequisicoes) {
                return false;
            }

            quantidadeRequisicoes++;
            return true;
        }
    }
}