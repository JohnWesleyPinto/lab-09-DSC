package br.ufpb.dcx.dsc.repositorios.services;

import br.ufpb.dcx.dsc.repositorios.models.Photo;
import br.ufpb.dcx.dsc.repositorios.models.Organizacao;
import br.ufpb.dcx.dsc.repositorios.models.User;
import br.ufpb.dcx.dsc.repositorios.repository.OrganizacaoRepository;
import br.ufpb.dcx.dsc.repositorios.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizacaoService {
    private OrganizacaoRepository organizacaoRepository;
    private UserRepository userRepository;
    public OrganizacaoService( UserRepository userRepository, OrganizacaoRepository organizacaoRepository){
        this.organizacaoRepository = organizacaoRepository;
        this.userRepository = userRepository;
    }

    public List<Organizacao> listOrganizacoes() {
        return organizacaoRepository.findAll();
    }
    public Organizacao getOrganizacao(Long orgId) {
        if(orgId != null)
            return organizacaoRepository.getReferenceById(orgId);
        return null;
    }

    public Organizacao createOrganizacao(Organizacao org, Long userId){
        Optional<User> uOpt = userRepository.findById(userId);
        if(uOpt.isPresent()){
            Organizacao o = organizacaoRepository.save(org);
            User u = uOpt.get();
            u.getOrganizacaos().add(org);
            userRepository.save(u);
            return o;
        }
        return null;
    }

    public Organizacao updateOrganizacao(Long orgId, Organizacao o) {
        Optional<Organizacao> orgOpt = organizacaoRepository.findById(orgId);
        if(orgOpt.isPresent()){
            Organizacao org = orgOpt.get();
            org.setNome(o.getNome());
            return organizacaoRepository.save(org);
        }
        return null;
    }

    public void deleteOrganizacao(Long orgId) {
        Optional<Organizacao> oOpt = organizacaoRepository.findById(orgId);
        if(oOpt.isPresent()){
            Organizacao o = oOpt.get();

            o.getUsers().stream().forEach( user -> {
                user.getOrganizacaos().remove(o);
                userRepository.save(user);
            });
            o.getUsers().removeAll(o.getUsers());
            organizacaoRepository.delete(o);
        }
    }
}
