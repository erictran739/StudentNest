package edu.csulb.cecs491b.studentnest.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum Building {
    ENGINEERING_AND_COMPUTER_SCIENCE("Engineering and Computer Science", "ECS");

    public final String name;
    public final String abbreviation;

    public static Building fromName(String name) {
        for (Building building : Building.values()) {
            if (name.equalsIgnoreCase(building.name))
                return building;
        }
        return null;
    }

    public static Building fromAbbreviation(String abbreviation) {
        for (Building building : Building.values()) {
            if (abbreviation.equalsIgnoreCase(building.abbreviation))
                return building;
        }
        return null;
    }
}

