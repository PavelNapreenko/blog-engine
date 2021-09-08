package ru.pnapreenko.blogengine.model;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
@NoArgsConstructor(force = true)
@EqualsAndHashCode(of = {"id"})
public abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
}
