package uet.k59t.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uet.k59t.controller.dto.ParentDTO;
import uet.k59t.controller.dto.ParentDTOwithChildren;
import uet.k59t.controller.dto.StudentDTO;
import uet.k59t.model.Parent;
import uet.k59t.model.Student;
import uet.k59t.repository.ParentRepository;
import uet.k59t.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Longlaptop on 11/29/2017.
 */
@Service
public class ParentService {
    @Autowired
    ParentRepository parentRepository;
    @Autowired
    StudentRepository studentRepository;


    public List<ParentDTO> showall() {
        List<Parent> parents = (List<Parent>) parentRepository.findAll();
        List<ParentDTO> parentDTOS = new ArrayList<ParentDTO>();
        for (int i = 0; i < parents.size(); i++) {
            ParentDTO parentDTO  = new ParentDTO();
//            parentDTO.setParentId(parentDTOS.get(i).getParentId());
            parentDTO.setParentName(parentDTOS.get(i).getParentName());
        }
        return parentDTOS;
    }


    public ParentDTO createParent(Parent parent) {
        parent.setToken(UUID.randomUUID().toString());
        parentRepository.save(parent);
        ParentDTO parentDTO = new ParentDTO();
        parentDTO.setParentName(parent.getParentName());
        return parentDTO;
    }

    public StudentDTO createStudent(StudentDTO studentDTO) {
        Student student = new Student();
        student.setStudentName(studentDTO.getStudentName());
//        student.setStaff(staffRepository.findBystaffName(studentDTO.getStaffDTO().getStaffName()));
        if(parentRepository.findByParentName(studentDTO.getParentDTO().getParentName()) != null){

            student.setParent(parentRepository.findByParentName(studentDTO.getParentDTO().getParentName()));
        }
        studentRepository.save(student);
        return studentDTO;
    }
    public ParentDTOwithChildren findByParentName(String parentName) {
        Parent parent = parentRepository.findByParentName(parentName);
        ParentDTOwithChildren parentDTOwithChildren = new ParentDTOwithChildren();
        parentDTOwithChildren.setParentName(parent.getParentName());
        List<Student> studentList = new ArrayList<Student>();
        List<StudentDTO> studentDTOList = new ArrayList<StudentDTO>();
        studentList = studentRepository.findByParent(parent);
        for (Student x: studentList             ) {
            StudentDTO studentDTO = new StudentDTO();
            studentDTO.setStudentName(x.getStudentName());
            studentDTOList.add(studentDTO);
        }
        parentDTOwithChildren.setStudentDTOList(studentDTOList);
        return parentDTOwithChildren;
    }
}
