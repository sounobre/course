package com.ead.course.dtos;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CourseRecordDto(
                              @NotBlank(message = "Name is mandatory")
                              String name,
                              @NotBlank(message = "description is mandatory")
                              String description,
                              @NotNull(message = "courseStatus is mandatory")
                              CourseStatus courseStatus,
                              @NotNull(message = "courseLevel is mandatory")
                              CourseLevel courseLevel,
                              @NotNull(message = "userInstructor is mandatory")
                              UUID userInstructor,

                              String imageUrl) {
}
