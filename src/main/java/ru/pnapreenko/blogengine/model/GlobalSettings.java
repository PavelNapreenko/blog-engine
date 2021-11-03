package ru.pnapreenko.blogengine.model;

import lombok.*;
import org.hibernate.annotations.NaturalId;
import ru.pnapreenko.blogengine.enums.SettingsCodeAndValue;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "global_settings")
@Data
@NoArgsConstructor(force = true)
@EqualsAndHashCode(callSuper = true)
public class GlobalSettings extends AbstractEntity {

    @NaturalId
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private SettingsCodeAndValue.Code code;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 5, nullable = false)
    private SettingsCodeAndValue.Value value;
}
