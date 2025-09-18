package br.ufpb.dcx.dsc.repositorios.dto;

public class RespostaLogin {

    private final String token;
    private final String nomeUsuario;

    public RespostaLogin(String token, String nomeUsuario) {
        this.token = token;
        this.nomeUsuario = nomeUsuario;
    }

    public String getToken() {
        return token;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }
}