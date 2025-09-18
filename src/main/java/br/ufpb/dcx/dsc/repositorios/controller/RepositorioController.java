package br.ufpb.dcx.dsc.repositorios.controller;

import br.ufpb.dcx.dsc.repositorios.dto.RepositorioDTO;
import br.ufpb.dcx.dsc.repositorios.models.Repositorio;
import br.ufpb.dcx.dsc.repositorios.services.RepositorioService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api")
public class RepositorioController {

    private ModelMapper modelMapper;
    private RepositorioService repositorioService;

    public RepositorioController(RepositorioService repositorioService, ModelMapper modelMapper) {
        this.repositorioService = repositorioService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(path = "/repositorio/{repoId}")
    public RepositorioDTO getRepositorio(@PathVariable Long repoId) {
        Repositorio t = repositorioService.getRepositorio(repoId);
        return convertToDTO(t);
    }

    @GetMapping("/repositorio")
    public List<RepositorioDTO> getFilteredRepositorios() {
        List<Repositorio> repositorios = repositorioService.listRepositorios();
        return repositorios.stream().map(task -> convertToDTO(task)).collect(Collectors.toList());
    }

    @PostMapping("/organizacao/{orgId}/repositorio")
    public RepositorioDTO createRepositorio(@Valid @PathVariable Long orgId, @RequestBody RepositorioDTO repoDTO) {
        Repositorio r = convertToEntity(repoDTO);
        Repositorio repoCreated = repositorioService.saveRepositorio(r, orgId);
        System.out.println(repoCreated);
        return convertToDTO(repoCreated);
    }

    @PutMapping("/repositorio/{repoId}")
    public RepositorioDTO updateRepositorio(@PathVariable Long repoId, @RequestBody RepositorioDTO repoDTO) {
        Repositorio t = convertToEntity(repoDTO);
        Repositorio taskCreated = repositorioService.updateRepositorio(repoId, t);
        return convertToDTO(taskCreated);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/repositorio/{repoId}")
    public void deleteRepositorio(@PathVariable Long repoId) {
        repositorioService.deleteRepositorio(repoId);
    }


    private RepositorioDTO convertToDTO(Repositorio t) {
        return modelMapper.map(t, RepositorioDTO.class);
    }

    private Repositorio convertToEntity(RepositorioDTO taskDTO) {
        return modelMapper.map(taskDTO, Repositorio.class);
    }
}
