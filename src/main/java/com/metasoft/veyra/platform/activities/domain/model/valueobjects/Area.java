package com.metasoft.veyra.platform.activities.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Area {

    private final String areaCode;

    protected Area() {
        this.areaCode = null;
    }

    public Area(String areaCode) {
        if (areaCode == null || areaCode.isBlank()) {
            throw new IllegalArgumentException("Area code cannot be null or blank");
        }
        this.areaCode = areaCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Area area = (Area) o;
        return Objects.equals(areaCode, area.areaCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(areaCode);
    }
}