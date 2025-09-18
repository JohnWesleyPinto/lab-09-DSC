package br.ufpb.dcx.dsc.repositorios.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class UtilitarioJwt {

    @Value("${jwt.secret}")
    private String segredo;

    @Value("${jwt.expiration}")
    private long expiracao;

    public String extrairNomeUsuario(String token) {
        return extrairDeclaracao(token, Claims::getSubject);
    }

    public Date extrairDataExpiracao(String token) {
        return extrairDeclaracao(token, Claims::getExpiration);
    }

    public <T> T extrairDeclaracao(String token, Function<Claims, T> resolvedorDeclaracoes) {
        final Claims declaracoes = extrairTodasDeclaracoes(token);
        return resolvedorDeclaracoes.apply(declaracoes);
    }

    public String gerarToken(UserDetails detalhesUsuario) {
        String papel = detalhesUsuario.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");
        Map<String, Object> declaracoes = Map.of("papel", papel);
        return criarToken(declaracoes, detalhesUsuario.getUsername());
    }

    public boolean validarToken(String token, UserDetails detalhesUsuario) {
        final String nomeUsuario = extrairNomeUsuario(token);
        return nomeUsuario.equals(detalhesUsuario.getUsername()) && !tokenExpirado(token);
    }

    private boolean tokenExpirado(String token) {
        return extrairDataExpiracao(token).before(new Date());
    }

    private String criarToken(Map<String, Object> declaracoes, String sujeito) {
        Date agora = new Date();
        Date dataExpiracao = new Date(agora.getTime() + expiracao);
        return Jwts.builder()
                .setClaims(declaracoes)
                .setSubject(sujeito)
                .setIssuedAt(agora)
                .setExpiration(dataExpiracao)
                .signWith(obterChaveAssinatura(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extrairTodasDeclaracoes(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(obterChaveAssinatura())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key obterChaveAssinatura() {
        return Keys.hmacShaKeyFor(segredo.getBytes(StandardCharsets.UTF_8));
    }
}