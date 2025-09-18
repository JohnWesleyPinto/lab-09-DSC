package br.ufpb.dcx.dsc.repositorios.controller;

import br.ufpb.dcx.dsc.repositorios.dto.RequisicaoAlteracaoSenha;
import br.ufpb.dcx.dsc.repositorios.dto.RequisicaoLogin;
import br.ufpb.dcx.dsc.repositorios.dto.RespostaLogin;
import br.ufpb.dcx.dsc.repositorios.security.UtilitarioJwt;
import br.ufpb.dcx.dsc.repositorios.services.UserService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api")
@Validated
public class AuthController {

    private final AuthenticationManager gerenciadorAutenticacao;
    private final UtilitarioJwt utilitarioJwt;
    private final UserService servicoUsuario;

    public AuthController(AuthenticationManager gerenciadorAutenticacao,
                          UtilitarioJwt utilitarioJwt,
                          UserService servicoUsuario) {
        this.gerenciadorAutenticacao = gerenciadorAutenticacao;
        this.utilitarioJwt = utilitarioJwt;
        this.servicoUsuario = servicoUsuario;
    }

    @PostMapping("/login")
    public ResponseEntity<RespostaLogin> realizarLogin(@Valid @RequestBody RequisicaoLogin requisicao) {
        try {
            Authentication autenticacao = gerenciadorAutenticacao.authenticate(
                    new UsernamePasswordAuthenticationToken(requisicao.getNomeUsuario(), requisicao.getSenha())
            );
            UserDetails detalhesUsuario = (UserDetails) autenticacao.getPrincipal();
            String token = utilitarioJwt.gerarToken(detalhesUsuario);
            return ResponseEntity.ok(new RespostaLogin(token, detalhesUsuario.getUsername()));
        } catch (AuthenticationException excecao) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas", excecao);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> alterarSenha(@Valid @RequestBody RequisicaoAlteracaoSenha requisicao) {
        servicoUsuario.alterarSenha(requisicao.getNomeUsuario(), requisicao.getSenhaAtual(), requisicao.getNovaSenha());
        return ResponseEntity.ok().build();
    }
}