package org.springboot.sample.service;

import org.springboot.sample.entity.Student;

public interface IStudentService {
	Student getById(int id);
}