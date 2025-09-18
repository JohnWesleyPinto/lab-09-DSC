package br.ufpb.dcx.dsc.repositorios.services;

import br.ufpb.dcx.dsc.repositorios.models.Photo;
import br.ufpb.dcx.dsc.repositorios.models.User;
import br.ufpb.dcx.dsc.repositorios.repository.PhotoRepository;
import br.ufpb.dcx.dsc.repositorios.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PhotoRepository photoRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.photoRepository = photoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long userId) {
        if (userId != null) {
            return userRepository.getReferenceById(userId);
        }
        return null;
    }

    public User createUser(User user) {

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username é obrigatório");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username já cadastrado");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha é obrigatória");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("ROLE_USER");
        }

        if (user.getPhoto() == null) {
            Photo photo = new Photo("");
            photoRepository.save(photo);
            user.setPhoto(photo);
        } else if (user.getPhoto().getPhotoId() == null) {
            photoRepository.save(user.getPhoto());
        }

        return userRepository.save(user);
    }

    public User updateUser(Long userId, User u) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (u.getEmail() != null) user.setEmail(u.getEmail());
            if (u.getNome() != null) user.setNome(u.getNome());

            if (u.getUsername() != null && !u.getUsername().isBlank()
                    && !u.getUsername().equals(user.getUsername())) {
                if (userRepository.existsByUsername(u.getUsername())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Username já cadastrado");
                }
                user.setUsername(u.getUsername());
            }

            if (u.getRole() != null && !u.getRole().isBlank()) user.setRole(u.getRole());

            if (u.getPassword() != null && !u.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(u.getPassword()));
            }

            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(Long userId) {
        /* Implementação original comentada mantida como referência.
        Optional<User> uOpt = userRepository.findById(userId);
        User u = uOpt.get();
        if(uOpt.isPresent()){
            // Remove all boards shared with me
            u.getBoardsShared().removeAll(u.getBoardsShared());
            // Remove users who share my boards
            Collection<Board> myBoards = u.getBoards();
            myBoards.stream().forEach(board -> {
                Collection<User> users = board.getUsers();
                users.stream().forEach(user -> {
                    user.getBoardsShared().remove(board);
                    userRepository.save(user);
                });
                boardRepository.save(board);
            });
            userRepository.save(u);
            userRepository.delete(u);
        }
        */
    }

    public void alterarSenha(String nomeUsuario, String senhaAtual, String novaSenha) {
        if (senhaAtual == null || senhaAtual.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha atual é obrigatória");
        }
        if (novaSenha == null || novaSenha.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nova senha é obrigatória");
        }

        Optional<User> usuarioOptional = userRepository.findByUsername(nomeUsuario);
        if (usuarioOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }

        User usuario = usuarioOptional.get();

        if (!passwordEncoder.matches(senhaAtual, usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }

        usuario.setPassword(passwordEncoder.encode(novaSenha));
        userRepository.save(usuario);
    }

    public Optional<User> buscarPorNomeUsuario(String nomeUsuario) {
        return userRepository.findByUsername(nomeUsuario);
    }
}
