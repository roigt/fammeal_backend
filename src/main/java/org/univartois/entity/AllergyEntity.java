package org.univartois.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private long id;

    @Column(nullable = false)
    private String name;
}
