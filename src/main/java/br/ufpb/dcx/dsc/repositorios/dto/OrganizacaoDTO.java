package br.ufpb.dcx.dsc.repositorios.dto;

import jakarta.validation.constraints.NotBlank;

public class OrganizacaoDTO {
    @NotBlank
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "OrganizacaoDTO{" +
                "nome='" + nome + '\'' +
                '}';
    }
}
