package app.pi_fisio.dto;

import app.pi_fisio.entity.JointIntensity;
import app.pi_fisio.entity.User;
import app.pi_fisio.entity.UserRole;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.util.List;
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
        private Long id;
        private String pictureUrl;
        private String name;
        private LocalDate dateOfBirth;
        private List<String> classes;
        private List<JointIntensity> jointIntensities;
        
        public UserDTO(User user){
                BeanUtils.copyProperties(user, this);
        }
}
