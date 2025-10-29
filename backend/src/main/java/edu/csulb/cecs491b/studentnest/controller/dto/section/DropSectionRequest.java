package edu.csulb.cecs491b.studentnest.controller.dto.section;

public record DropSectionRequest(
        int user_id,
        int section_id
        // Reason for drop?
        ) { }
