package com.ead.course.service;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;

import java.util.UUID;

public interface CourseUserService {
    boolean existByCourseAndUserId(CourseModel courseModel, UUID userId);
    CourseUserModel saveAndSubscriptUserInCourse(CourseUserModel courseUserModel);
    boolean existsByUserId(UUID userId);
    void deleteAllByUserId(UUID userId);
}
