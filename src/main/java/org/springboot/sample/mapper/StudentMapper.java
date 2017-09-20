package org.springboot.sample.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springboot.sample.entity.Student;

@Mapper
public interface StudentMapper{

	List<Student> likeName(String name);
	
	Student getById(int id);
	
	int add(Student stu);
	
	String getNameById(int id);

}
