package org.univartois.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "allergies")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AllergyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "allergies_generator")
    @SequenceGenerator(name = "allergies_generator", sequenceName = "allergies_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String name;
}
