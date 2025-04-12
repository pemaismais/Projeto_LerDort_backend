package app.pi_fisio.service;

import app.pi_fisio.dto.ExerciseDTO;
import app.pi_fisio.dto.ExerciseFilterDTO;
import app.pi_fisio.dto.ExercisePageDTO;
import app.pi_fisio.entity.*;
import app.pi_fisio.infra.exception.ExerciseNotFoundException;
import app.pi_fisio.infra.exception.NoJointIntensitiesException;
import app.pi_fisio.infra.exception.UserNotFoundException;
import app.pi_fisio.queryfilters.ExerciseQueryFilter;
import app.pi_fisio.repository.ExerciseRepository;
import app.pi_fisio.repository.UserRepository;
import app.pi_fisio.specifications.ExerciseSpec;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ExerciseService {

    @Autowired
    ExerciseRepository exerciseRepository;
    @Autowired
    UserRepository userRepository;

    public ExerciseDTO create(ExerciseDTO exerciseDTO) throws Exception {
        Exercise exercise = new Exercise(exerciseDTO);
        ExerciseDTO savedExercise = new ExerciseDTO(exerciseRepository.save(exercise));
        log.info("Novo exercício criado com ID: {}", savedExercise.getId());
        return savedExercise;
    }

    public ExerciseDTO update(Long id, ExerciseDTO exerciseDTO) throws Exception{
        exerciseRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Tentativa de atualizar exercício inexistente: ID {}", id);
                    return new ExerciseNotFoundException();
                });

        Exercise exercise = new Exercise(exerciseDTO);
        exercise.setId(id);
        ExerciseDTO updatedExercise = new ExerciseDTO(exerciseRepository.save(exercise));
        log.info("Exercício atualizado com sucesso: ID {}", id);
        return updatedExercise;
    }

    public void delete(Long id)  {
        if (!exerciseRepository.existsById(id)) {
            log.warn("Tentativa de deletar exercício inexistente: ID {}", id);
            throw new ExerciseNotFoundException();
        }
        exerciseRepository.deleteById(id);
        log.info("Exercício deletado com sucesso: ID {}", id);
    }

    public ExercisePageDTO findAll(int page, int size, ExerciseQueryFilter filter ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Exercise> exercisePage = exerciseRepository.findAll(filter.toSpecification(), pageable);

        log.info("Busca paginada de exercícios: Página {}, Tamanho {}", page, size);

        List<ExerciseDTO> exercises = exercisePage.get().map(ExerciseDTO::new).toList();
        return new ExercisePageDTO(exercises, exercisePage.getTotalElements(), exercisePage.getTotalPages());
    }

    public ExerciseDTO findById(Long id) throws ExerciseNotFoundException {
        return exerciseRepository.findById(id)
                .map(exercise -> {
                    log.info("Exercício encontrado: ID {}", id);
                    return new ExerciseDTO(exercise);
                })
                .orElseThrow(() -> {
                    log.warn("Exercício não encontrado: ID {}", id);
                    return new ExerciseNotFoundException();
                });
    }
    public List<ExerciseDTO> findByJointAndIntensity(Joint joint, Intensity intensity) throws Exception {
        return exerciseRepository.findByJointAndIntensity(joint, intensity)
                .map(exercises -> {
                    log.info("Busca por exercícios - Articulação: {}, Intensidade: {}", joint, intensity);
                    return exercises.stream().map(ExerciseDTO::new).toList();
                })
                .orElseThrow(() -> {
                    log.warn("Nenhum exercício encontrado para Articulação: {}, Intensidade: {}", joint, intensity);
                    return new ExerciseNotFoundException();
                });
    }

    public List<ExerciseDTO> findByUser(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado: ID {}", userId);
                    return new UserNotFoundException("id", userId.toString());
                });

        List<JointIntensity> jointIntensities = user.getJointIntensities();

        if (jointIntensities == null || jointIntensities.isEmpty()) {
            log.warn("Usuário ID {} não possui intensidades articulares registradas.", userId);
            throw new NoJointIntensitiesException("User has no joint intensities.");
        }

        log.info("Buscando exercícios recomendados para o usuário ID {}", userId);

        List<ExerciseDTO> list = new ArrayList<>();
        for (JointIntensity jointIntensity : jointIntensities) {
            list.addAll(findByJointAndIntensity(jointIntensity.getJoint(), jointIntensity.getIntensity()));
        }
        return list;
    }

}
