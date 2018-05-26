package uet.k59t.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uet.k59t.controller.dto.ParentDTO;
import uet.k59t.controller.dto.StaffDTO;
import uet.k59t.controller.dto.StudentDTO;
import uet.k59t.model.Staff;
import uet.k59t.model.Student;
import uet.k59t.repository.ParentRepository;
import uet.k59t.repository.StaffRepository;
import uet.k59t.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Longlaptop on 11/28/2017.
 */
@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    ParentRepository parentRepository;

    public List<StudentDTO> showAllStudents() {
        List<Student> studentList = (List<Student>) studentRepository.findAll();
        List<StudentDTO> studentDTOS = new ArrayList<StudentDTO>();
        for (int i = 0; i < studentList.size(); i++) {
            StudentDTO studentToBeAdded = new StudentDTO();
//            Staff staff = studentList.get(i).getStaff();
//            StaffDTO staffDTO = new StaffDTO();
//            staffDTO.setStaffName(staff.getStaffName());
//            staffDTO.setPosition(staff.getPosition());
//            staffDTO.setUnit(staff.getUnit());
//            studentToBeAdded.setStaffDTO(staffDTO);
            studentToBeAdded.setStudentName(studentList.get(i).getStudentName());
            studentDTOS.add(studentToBeAdded);

        }
        return studentDTOS;
    }



    public StudentDTO findByStudentName(String studentName) {
        Student student = studentRepository.findByStudentName(studentName);
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setStudentName(student.getStudentName());
        //Create ParentDTO and assign to StudentDTO
        ParentDTO parentDTO = new ParentDTO();
        parentDTO.setParentName(student.getParent().getParentName());
        parentDTO.setPhone(student.getParent().getPhone());
        studentDTO.setParentDTO(parentDTO);

        //Get list of staffs who are teaching this student
        List<Staff> staff = staffRepository.findByStudentList_StudentName(studentName);
        List<StaffDTO> staffDTOS = new ArrayList<StaffDTO>();
        for (Staff staff1: staff) {
            StaffDTO staffDTO = new StaffDTO();
            staffDTO.setStaffName(staff1.getStaffName());
            staffDTO.setPosition(staff1.getPosition());
            staffDTO.setUnit(staff1.getUnit());
            staffDTO.setPassword(null);
            staffDTO.setPhone(staff1.getPhone());
//            staffDTO.getUnit().setStaffList(null);
            staffDTOS.add(staffDTO);
        }
        studentDTO.setStaffDTOList(staffDTOS);
        return studentDTO;
    }
}
