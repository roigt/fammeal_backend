package org.univartois.entity;


import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter @Setter
public abstract class SoftDeletableEntity {

    protected boolean deleted = false;

}
