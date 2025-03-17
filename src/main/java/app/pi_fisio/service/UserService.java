package app.pi_fisio.service;

import app.pi_fisio.auth.JwtService;
import app.pi_fisio.dto.UserDTO;
import app.pi_fisio.dto.UserPageDTO;
import app.pi_fisio.entity.JointIntensity;
import app.pi_fisio.entity.User;
import app.pi_fisio.helper.CopyPropertiesUtil;
import app.pi_fisio.infra.exception.UserNotFoundException;
import app.pi_fisio.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
@Log4j2
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtService jwtService;

    public UserDTO create(UserDTO userDTO) {
        User user = new User(userDTO);
        for (JointIntensity jointIntensity : user.getJointIntensities()) {
            jointIntensity.setUser(user);
        }
        log.info("Criando novo usuário: {}", user.getEmail());
        return new UserDTO(userRepository.save(user));
    }

    public UserDTO update(Long id, UserDTO userDTO) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("id", id.toString());
        }
        User user = new User(userDTO);
        user.setId(id);
        log.info("Usuário atualizado com sucesso (ID: {})", id);
        return new UserDTO(userRepository.save(user));
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            log.warn("Tentativa de exclusão falhou - Usuário não encontrado (ID: {})", id);
            throw new UserNotFoundException("id", id.toString());
        }
        log.info("Usuário deletado com sucesso (ID: {})", id);
        userRepository.deleteById(id);
    }

    public UserPageDTO findAll(int page,int size) {
        log.info("Buscando usuários - Página: {}, Tamanho: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAll(pageable);
        List<UserDTO> users = userPage.get().map(UserDTO::new).toList();
        log.info("Lista de usuários retornada com {} registros.", userPage.getTotalElements());
        return new UserPageDTO(users, userPage.getTotalElements(), userPage.getTotalPages());
    }

    public UserDTO findById(Long id) {
        log.info("Buscando usuário por ID: {}", id);
        return userRepository.findById(id)
                .map(UserDTO::new)
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado (ID: {})", id);
                    return new UserNotFoundException("id", id.toString());
                });
    }
    public UserDTO findUserByJwt(String jwt) {
        String email = jwtService.validateToken(jwt);
        log.info("Buscando usuário por JWT - Email: {}", email);
        return userRepository.findByEmail(email)
                .map(UserDTO::new)
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado pelo JWT (Email: {})", email);
                    return new UserNotFoundException("email", email);
                });    }

    public UserDTO findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserDTO::new)
                .orElseThrow(() -> new UserNotFoundException("email", email));
    }

    public UserDTO patchUpdate(UserDTO userDTO,String jwt) throws Exception{
        String email = jwtService.validateToken(jwt);
        log.info("Patch update do usuário autenticado - Email: {}", email);
        User currentUser = userRepository.findByEmail (email)
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado para patch update (Email: {})", email);
                    return new UserNotFoundException("email", email);
                });
        User patchUser = new User(userDTO);
        patchUser.setRole(null);
        patchUser.setId(null);
        patchUser.setEmail(null);

        if(!ObjectUtils.isEmpty(patchUser.getJointIntensities())){
            List<JointIntensity> currentUserJointIntensities = replaceJointIntensities(currentUser, patchUser);
            currentUser.setJointIntensities(currentUserJointIntensities);
            patchUser.setJointIntensities(null);
        }

        CopyPropertiesUtil.copyNonNullProperties(patchUser, currentUser);
        log.info("Patch update realizado com sucesso para o usuário: {}", email);
        return new UserDTO(userRepository.save(currentUser));
    }

    private  List<JointIntensity> replaceJointIntensities(User currentUser, User patchUser) {
        List<JointIntensity> currentUserJointIntensities = currentUser.getJointIntensities();
        currentUserJointIntensities.clear();

        for (JointIntensity jointIntensities : patchUser.getJointIntensities() ) {
            JointIntensity jointIntensity = new JointIntensity(
                    null,
                    jointIntensities.getJoint(),
                    jointIntensities.getIntensity(),
                    currentUser);
            currentUserJointIntensities.add(jointIntensity);
        }
        return currentUserJointIntensities;
    }
}
