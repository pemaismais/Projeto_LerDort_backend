package app.pi_fisio.entity;


import app.pi_fisio.dto.ExerciseDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@EntityListeners(AuditingEntityListener.class)
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


    @CreatedDate
    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdDate;
    @CreatedBy
    @Column(
            nullable = false,
            updatable = false
    )
    private String createdBy;


    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
    @LastModifiedBy
    @Column(insertable = false)
    private String lastModifiedBy;

    public Exercise(ExerciseDTO exerciseDTO){
        BeanUtils.copyProperties(exerciseDTO,this);
    }
}
