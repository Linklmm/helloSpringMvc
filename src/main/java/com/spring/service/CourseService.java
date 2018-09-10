package com.spring.service;

import com.spring.modle.Course;
import org.springframework.stereotype.Service;

@Service
public interface CourseService {
    public Course getCourseById(Integer courseId);
}
