package app.pi_fisio.service;

import app.pi_fisio.dto.ExerciseDTO;
import app.pi_fisio.entity.*;
import app.pi_fisio.infra.exception.ExerciseNotFoundException;
import app.pi_fisio.infra.exception.NoJointIntensitiesException;
import app.pi_fisio.infra.exception.UserNotFoundException;
import app.pi_fisio.repository.ExerciseRepository;
import app.pi_fisio.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ExerciseServiceTest {

    @Autowired
    ExerciseService exerciseService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    ExerciseRepository exerciseRepository;

    void findByPersonSetup() {
        // User
        User user = new User();
        List<JointIntensity> jointIntensities = List.of(
                new JointIntensity(1L, Joint.SHOULDER, Intensity.HIGH, user),
                new JointIntensity(1L, Joint.KNEE, Intensity.MEDIUM, user)
        );
        user.setJointIntensities(jointIntensities);

        User user2 = new User();
        user2.setJointIntensities(List.of());

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(new User()));

        when(userRepository.findById(0L))
                .thenReturn(Optional.of(user2));

        // Exercicios
        Exercise exercise01 = new Exercise(1L, "Exercise 01", "Exercise 01 description", "3x", "https://videoUrl.com", Joint.SHOULDER, Intensity.HIGH);
        Exercise exercise02 = new Exercise(2L, "Exercise 02", "Exercise 02 description", "6x", "https://videoUrl.com", Joint.SHOULDER, Intensity.HIGH);
        List<Exercise> shoulderHighExercises = List.of(exercise02, exercise01);

        Exercise exercise03 = new Exercise(3L, "Exercise 03", "Exercise 03 description", "12x", "https://videoUrl.com", Joint.KNEE, Intensity.MEDIUM);
        List<Exercise> kneeMediumExercises = List.of(exercise03);

        when(exerciseRepository.findByJointAndIntensity(Joint.SHOULDER, Intensity.HIGH))
                .thenReturn(Optional.of(shoulderHighExercises));

        when(exerciseRepository.findByJointAndIntensity(Joint.KNEE, Intensity.MEDIUM))
                .thenReturn(Optional.of(kneeMediumExercises));
    }

    @BeforeEach
    void setup() {
        Exercise exercise = new Exercise(1L, "Exercise 01", "Exercise 01 description", "3x", "https://videoUrl.com", Joint.SHOULDER, Intensity.HIGH);
        when(exerciseRepository.save(any()))
                .thenReturn(exercise);

        when(exerciseRepository.findById(1L))
                .thenReturn(Optional.of(exercise));
        when(exerciseRepository.findById(0L))
                .thenReturn(Optional.empty());
        when(exerciseRepository.existsById(1L))
                .thenReturn(true);
        when(exerciseRepository.findAll())
                .thenReturn(List.of(exercise));
    }

//    @Test
//    void findByPerson01() throws Exception {
//        findByPersonSetup();
//        List<ExerciseDTO> response = exerciseService.findByPerson(1L);
//
//        assertEquals(3, response.size());
//    }
//
//    @Test
//    @DisplayName("Find by user with user joint intensity null & empty")
//    void findByPerson02() throws Exception {
//        findByPersonSetup();
//        assertThrows(NoJointIntensitiesException.class,
//                () -> exerciseService.findByPerson(2L));
//        assertThrows(NoJointIntensitiesException.class,
//                () -> exerciseService.findByPerson(0L));
//
//    }
//
//    @Test
//    @DisplayName("Find by user with invalid id")
//    void findByPerson03() throws Exception {
//        assertThrows(UserNotFoundException.class,
//                () -> exerciseService.findByPerson(0L));
//    }

    @Test
    void create() throws Exception {
        ExerciseDTO exerciseDTO = new ExerciseDTO(3L, "Exercise 03", "Exercise 03 description", "12x", "https://videoUrl.com", Joint.KNEE, Intensity.MEDIUM);
        ExerciseDTO response = exerciseService.create(exerciseDTO);
        assertNotNull(response);
    }

    @Test
    @DisplayName("Updating with a valid id")
    void update01() throws Exception {
        ExerciseDTO exerciseDTO = new ExerciseDTO(3L, "Exercise 03", "Exercise 03 description", "12x", "https://videoUrl.com", Joint.KNEE, Intensity.MEDIUM);
        ExerciseDTO response = exerciseService.update(1L, exerciseDTO);
        assertNotNull(response);
    }

    @Test
    @DisplayName("Updating with a invalid id")
    void update02() {
        assertThrows(ExerciseNotFoundException.class,
                () -> exerciseService.update(0L, new ExerciseDTO()));
    }

    @Test
    @DisplayName("Deleting a valid id")
    void delete01() {
        assertDoesNotThrow(() -> exerciseService.delete(1L));
    }

    @Test
    @DisplayName("Deleting with a invalid id")
    void delete02() {
        assertThrows(ExerciseNotFoundException.class,
                () -> exerciseService.delete(0L));
    }

    @Test
    @DisplayName("Finding by a valid id")
    void findById01() {
        ExerciseDTO response = exerciseService.findById(1L);
        assertNotNull(response);
    }

    @Test
    @DisplayName("Finding with a invalid id")
    void findById02() {
        assertThrows(ExerciseNotFoundException.class,
                () -> exerciseService.findById(0L));
    }

//    @Test
//    void findAll() {
//        List<ExerciseDTO> response = exerciseService.findAll();
//        assertNotNull(response);
//    }
}
