package app.pi_fisio.entity;


import app.pi_fisio.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private LocalDate dateOfBirth;
    private String pictureUrl;

    @Column(unique = true, name = "keycloak_id")
    private String keycloakId;

    @ElementCollection
    @CollectionTable(name = "user_classes", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "class")
    private List<String> classes;

    @OneToMany(mappedBy = "user",fetch= FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<JointIntensity> jointIntensities;

    public User(UserDTO userDTO){
        BeanUtils.copyProperties(userDTO, this);
    }
}
