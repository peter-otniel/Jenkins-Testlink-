package service;

import domain.Homework;
import domain.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import repository.GradeXMLRepository;
import repository.HomeworkXMLRepository;
import repository.StudentXMLRepository;
import validation.ValidationException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

public class ServiceMockTest {

    private static Service service;
    @Mock
    private static StudentXMLRepository studentXMLRepository;
    @Mock
    private static HomeworkXMLRepository homeworkXMLRepository;
    @Mock
    private static GradeXMLRepository gradeXMLRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        service = new Service(studentXMLRepository, homeworkXMLRepository, gradeXMLRepository);
    }

    @Test
    void saveStudentWithInvalidData() {
        when(studentXMLRepository.save(any())).thenThrow(ValidationException.class);
        assertEquals(0, service.saveStudent("9", "Full Name", 1));
        Mockito.verify(studentXMLRepository).save(new Student("9", "Full Name", 1));
    }

    @Test
    void saveHomeworkWithAlreadyExistingID() {
        when(homeworkXMLRepository.save(any())).thenReturn(new Homework("","",0,0));
        assertEquals(0, service.saveHomework("123", "test2", 7, 2));
    }

    @Test
    void deleteExistingStudent() {
        doReturn(new Student("","",0)).when(studentXMLRepository).delete(anyString());
        assertNotEquals(0, service.deleteStudent("9"));
    }
}
