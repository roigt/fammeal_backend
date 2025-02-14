package org.univartois.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
@Table(name = "homes")
@Entity
public class HomeEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private boolean vegetarian;

    private boolean lunchAutomaticGeneration;

    private boolean dinerAutomaticGeneration;


}
