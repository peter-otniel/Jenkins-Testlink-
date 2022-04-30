package service;

import domain.Grade;
import domain.Homework;
import domain.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import repository.GradeXMLRepository;
import repository.HomeworkXMLRepository;
import repository.StudentXMLRepository;
import validation.*;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

    private static Service service;

    @BeforeAll
    private static void createEntities() {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Homework> homeworkValidator = new HomeworkValidator();
        Validator<Grade> gradeValidator = new GradeValidator();
        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, "students.xml");
        HomeworkXMLRepository fileRepository2 = new HomeworkXMLRepository(homeworkValidator, "homework.xml");
        GradeXMLRepository fileRepository3 = new GradeXMLRepository(gradeValidator, "grades.xml");
        service = new Service(fileRepository1, fileRepository2, fileRepository3);
    }

    @Test
    void saveStudentWithValidData() {
        service.saveStudent("9", "Full Name", 227);
        Collection<Student> newStudentsList = (Collection<Student>) service.findAllStudents();
        assertTrue(newStudentsList.contains(new Student("9", "Full Name", 227)));
        service.deleteStudent("9");
    }

    @Test
    void saveStudentWithInvalidData() {
        assertEquals(0, service.saveStudent("9", "Full Name", 1));
    }

    @Test
    void deleteExistingStudent() {
        service.saveStudent("9", "Full Name", 227);
        assertNotEquals(0, service.deleteStudent("9"));
    }

    @Test
    void saveHomeworkWithAlreadyExistingID() {
        service.saveHomework("123", "test1", 5, 1);
        assertEquals(0, service.saveHomework("123", "test2", 7, 2));
        service.deleteHomework("123");
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "999", "asd"})
    void deleteNonexistentHomework(String id) {
        assertNotEquals(1, service.deleteHomework(id));
    }
}