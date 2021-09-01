package ru.pnapreenko.blogengine.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "global_settings")
@ToString
@Getter
@Setter
@RequiredArgsConstructor
public class GlobalSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer settingId;

    @Column(name = "code", nullable = false)
    private String settingSystemName;

    @Column(name = "name", nullable = false)
    private String settingName;

    @Column(name = "value", nullable = false)
    private String settingValue;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GlobalSettings that = (GlobalSettings) o;
        return getSettingId().equals(that.getSettingId()) && getSettingSystemName().equals(that.getSettingSystemName())
                && getSettingName().equals(that.getSettingName()) && getSettingValue().equals(that.getSettingValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSettingId(), getSettingSystemName(), getSettingName(), getSettingValue());
    }
}
