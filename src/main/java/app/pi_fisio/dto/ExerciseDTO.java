package app.pi_fisio.dto;


import app.pi_fisio.entity.Exercise;
import app.pi_fisio.entity.Intensity;
import app.pi_fisio.entity.Joint;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExerciseDTO {
    public ExerciseDTO(Exercise exercise){
        BeanUtils.copyProperties(exercise,this);
    }

    private Long id;
    private String name;
    private String description;
    private String reps;
    private String videoUrl;
    private Joint joint;
    private Intensity intensity;

}