package br.ufpb.dcx.dsc.repositorios.controller;


import br.ufpb.dcx.dsc.repositorios.dto.OrganizacaoDTO;
import br.ufpb.dcx.dsc.repositorios.dto.OrganizacaoDTO;
import br.ufpb.dcx.dsc.repositorios.models.Organizacao;
import br.ufpb.dcx.dsc.repositorios.models.Organizacao;
import br.ufpb.dcx.dsc.repositorios.services.OrganizacaoService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class OrganizacaoController {

    private final OrganizacaoService organizacaoService;
    private final ModelMapper modelMapper;

    public OrganizacaoController(OrganizacaoService organizacaoService, ModelMapper modelMapper) {
        this.organizacaoService = organizacaoService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(path = "/organizacao")
    List<Organizacao> listOrgs(){
        return organizacaoService.listOrganizacoes();
    }

    @GetMapping("/organizacao/{orgId}")
    public OrganizacaoDTO getOrganizacao(@PathVariable Long orgId){
        Organizacao org = organizacaoService.getOrganizacao(orgId);
        return convertToDTO(org);
    }


    @PostMapping(path = "/user/{userId}/organizacao")
    OrganizacaoDTO createOrganizacao(@Valid  @RequestBody OrganizacaoDTO orgDTO, @PathVariable Long userId){
        Organizacao o = convertToEntity(orgDTO);
        Organizacao saved = organizacaoService.createOrganizacao(o, userId);
        return convertToDTO(saved);
    }

    @PutMapping("/organizacao/{orgId}")
    public OrganizacaoDTO updateTask(@PathVariable Long orgId, @RequestBody OrganizacaoDTO orgDTO){

        Organizacao o = convertToEntity(orgDTO);
        Organizacao orgUpdated = organizacaoService.updateOrganizacao(orgId, o);
        return convertToDTO(orgUpdated);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/organizacao/{userId}")
    public void deleteOrganizacao(@PathVariable Long userId){
        organizacaoService.deleteOrganizacao(userId);
    }

/*    @GetMapping("/board/{boardId}/user/{userId}/share")
    public AlbumDTO addFig(@PathVariable Long userId, @PathVariable Long boardId){
        Album u = organizacaoService.share (boardId, userId);
        return convertToDTO(u);
    }
    @DeleteMapping("/board/{boardId}/user/{userId}/share")
    public OrganizacaoDTO removeFig(@PathVariable Long userId, @PathVariable Long boardId){
        Organizacao u = organizacaoService.unshare(boardId, userId);
        return convertToDTO(u);
    }*/

    private OrganizacaoDTO convertToDTO(Organizacao o) {
        return modelMapper.map(o, OrganizacaoDTO.class);
    }

    private Organizacao convertToEntity(OrganizacaoDTO orgDTO) {
        return modelMapper.map(orgDTO, Organizacao.class);
    }
}

