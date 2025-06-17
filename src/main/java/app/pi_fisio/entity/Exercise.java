package app.pi_fisio.entity;


import app.pi_fisio.dto.ExerciseDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.beans.BeanUtils;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Audited
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    private String name;

    @NotNull
    @Column(length=1024)
    private String description;

    private String reps;
    private String videoUrl;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Joint joint;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Intensity intensity;

    public Exercise(ExerciseDTO exerciseDTO){
        BeanUtils.copyProperties(exerciseDTO,this);
    }
}
