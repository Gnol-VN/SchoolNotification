package uet.k59t.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uet.k59t.controller.dto.StudentDTO;
import uet.k59t.service.StudentService;

import java.util.List;

/**
 * Created by Longlaptop on 11/28/2017.
 */
@RestController
public class StudentController {
    @Autowired
    StudentService studentService;

    @RequestMapping(value = "/student/showall", method = RequestMethod.GET)
    public List<StudentDTO> showAllStudents(){
        return studentService.showAllStudents();
    }


    @RequestMapping(value = "/student/find/bystudentname/{studentName}", method = RequestMethod.GET)
    public StudentDTO findOne(@PathVariable String studentName){
        return studentService.findByStudentName(studentName);
    }
}
