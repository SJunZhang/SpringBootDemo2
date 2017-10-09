package org.springboot.sample.annotion;

import java.lang.reflect.Method;

import org.slf4j.LoggerFactory;
import org.springboot.sample.annotion.Todo.Priority;
import org.springboot.sample.annotion.Todo.Status;

public class TodoAnnotionTest {
	private org.slf4j.Logger logger = LoggerFactory.getLogger(TodoAnnotionTest.class);

	public static void main(String[]args) {
		Class businessLogicClass = BusinessLogic.class;
		for (Method method : businessLogicClass.getMethods()) {
			Todo todoAnnotion = method.getAnnotation(Todo.class);
			if (todoAnnotion != null) {
				System.out.println(method.getName());
				System.out.println(todoAnnotion.author());
				System.out.println(todoAnnotion.priority());
				System.out.println(todoAnnotion.status());

			}
		}
	}

}

class BusinessLogic {
	@Todo(priority = Priority.HIGH, status = Status.STARTED, author = "zhang")
	public void incompleteMethod() {

	}

	@Todo(priority = Priority.HIGH, status = Status.STARTED, author = "zhang")
	public void doBusiness() {
	}
}
