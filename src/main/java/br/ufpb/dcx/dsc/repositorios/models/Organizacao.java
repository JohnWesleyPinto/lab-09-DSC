package br.ufpb.dcx.dsc.repositorios.models;


import jakarta.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "tb_organizacao")
public class Organizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "nome")
    private String nome;

    @OneToMany(mappedBy = "organizacao")
    private Collection<Repositorio> repositorios;

    @ManyToMany(mappedBy = "organizacaos")
    Collection<User> users;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Collection<Repositorio> getRepositorios() {
        return repositorios;
    }

    public void setRepositorios(Collection<Repositorio> repositorios) {
        this.repositorios = repositorios;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }
}
