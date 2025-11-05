package edu.csulb.cecs491b.studentnest.entity.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Major {
    COMPUTER_SCIENCE("Computer Science"),
    INFORMATION_SYSTEMS("Information Systems"),
    ELECTRICAL_ENGINEERING("Electrical Engineering"),
    MECHANICAL_ENGINEERING("Mechanical Engineering"),
    BUSINESS("Business"),
    UNDECLARED("Undeclared");

    public final String stringValue;

    public static Major fromString(String name) {
        for (Major major : Major.values()) {
            if (name.equalsIgnoreCase(major.stringValue))
                return major;
        }
        return null;
    }
}

