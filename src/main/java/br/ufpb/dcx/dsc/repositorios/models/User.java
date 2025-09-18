package br.ufpb.dcx.dsc.repositorios.models;

import jakarta.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "nome")
    private String nome;

    @Column(name = "email")
    private String email;

    @OneToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @ManyToMany
    @JoinTable(name = "org_user",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "org_id"))
    private Collection<Organizacao> organizacaos;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public Collection<Organizacao> getOrganizacaos() {
        return organizacaos;
    }

    public void setOrganizacaos(Collection<Organizacao> organizacaos) {
        this.organizacaos = organizacaos;
    }
}
