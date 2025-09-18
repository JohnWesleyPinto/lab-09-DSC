package br.ufpb.dcx.dsc.repositorios.models;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_repositorio")
public class Repositorio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "repo_id")
    private Long repoId;

    @Column(name = "nome")
    private String nome;

    @Column(name = "is_private")
    private boolean isPrivate;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organizacao organizacao;

    public Repositorio(){
    }

    public Long getRepoId() {
        return repoId;
    }

    public void setRepoId(Long repoId) {
        this.repoId = repoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Organizacao getOrganizacao() {
        return organizacao;
    }

    public void setOrganizacao(Organizacao organizacao) {
        this.organizacao = organizacao;
    }
}
