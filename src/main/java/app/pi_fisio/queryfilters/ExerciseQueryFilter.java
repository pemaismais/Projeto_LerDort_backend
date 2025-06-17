package app.pi_fisio.queryfilters;

import app.pi_fisio.entity.Exercise;
import app.pi_fisio.entity.Intensity;
import app.pi_fisio.entity.Joint;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

import static app.pi_fisio.specifications.ExerciseSpec.*;

@Data
// video de ex: https://www.youtube.com/watch?v=1hV-HNdVkJw
public class ExerciseQueryFilter {
    private String name;
    private Set<Joint> joints;
    private Set<Intensity> intensities;

    public Specification<Exercise> toSpecification(){
        return nameContains(name)
                .and(hasIntensities(intensities))
                .and(hasJoints(joints));
    }
}
