package ru.pnapreenko.blogengine.model;

import lombok.*;
import ru.pnapreenko.blogengine.enums.SettingsCodeAndValue;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "global_settings")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GlobalSettings extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private SettingsCodeAndValue.Code code;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private SettingsCodeAndValue.Value value;
}
