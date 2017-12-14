package uet.k59t.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uet.k59t.controller.dto.StaffDTO;
import uet.k59t.controller.dto.StaffDTOwithPositionAndUnitname;
import uet.k59t.controller.dto.StaffDTOwithStudents;
import uet.k59t.model.Position;
import uet.k59t.model.Staff;
import uet.k59t.model.Student;
import uet.k59t.model.Unit;
import uet.k59t.repository.PositionRepostiory;
import uet.k59t.repository.StaffRepository;
import uet.k59t.repository.StudentRepository;
import uet.k59t.repository.UnitRepository;

import java.util.List;
import java.util.UUID;

/**
 * Created by Long on 11/21/2016.
 */
@Service
public class StaffService {
    @Autowired
    StaffRepository staffRepository;

    @Autowired
    PositionRepostiory positionRepostiory;

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    StudentRepository studentRepository;

    public StaffDTO createStaff(StaffDTO staffDTO) {
        Staff staff = staffRepository.findBystaffName(staffDTO.getStaffName());
        if(staff == null){
            staff = new Staff();
            staff.setStaffName(staffDTO.getStaffName());
            staff.setPassword(staffDTO.getPassword());
            staff.setPhone(staffDTO.getPhone());
            Position position = positionRepostiory.findByPositionName(staffDTO.getPosition().getPositionName());
            Unit unit = unitRepository.findByUnitName(staffDTO.getUnit().getUnitName());
            staff.setPosition(position);
            staff.setUnit(unit);
            staff.setToken(UUID.randomUUID().toString());
            staffRepository.save(staff);
            staffDTO.setPassword("Hidden");
            return staffDTO;
        }
        else {
            throw new NullPointerException("Username da ton tai");
        }

    }

    public StaffDTO getStaffById(Long id) {
        if(staffRepository.findOne(id) != null){
            Staff staff = staffRepository.findOne(id);
            StaffDTO staffDTO = new StaffDTO();
            staffDTO.setStaffName(staff.getStaffName());
            staffDTO.setPosition(staff.getPosition());
            staffDTO.setUnit(staff.getUnit());
            staffDTO.setPhone(staff.getPhone());
            staffDTO.getUnit().setStaffList(null);
            return staffDTO;
        }
        else throw new NullPointerException("Invalid id");
    }

    public StaffDTOwithStudents findByStaffName(String staffName) {
        Staff staff = staffRepository.findBystaffName(staffName);
        StaffDTOwithStudents staffDTOwithStudents = new StaffDTOwithStudents();
        staffDTOwithStudents.setStaffName(staff.getStaffName());
        staffDTOwithStudents.setPosition(staff.getPosition());
        staffDTOwithStudents.setUnit(staff.getUnit());
        staffDTOwithStudents.setPhone(staff.getPhone());
        //Get studentList of this staff
        List<Student> studentList  = studentRepository.findByStaffList_StaffName(staffName);
        for (int i = 0; i < studentList.size(); i++) {
            studentList.get(i).setStaffList(null);
            studentList.get(i).setParent(null);
        }
        staffDTOwithStudents.setStudentList(studentList);
        staffDTOwithStudents.getUnit().setStaffList(null);
        return staffDTOwithStudents;
    }

    public Staff login(StaffDTO staffDTO) {
        Staff staff = staffRepository.findBystaffName(staffDTO.getStaffName());
        if(staff == null){
            throw new NullPointerException("This staff is not exist");
        }
        else if(!staff.getPassword().equals(staffDTO.getPassword())){
            throw new NullPointerException("Invalid username/password");
        }
        else {
            staff.setPassword(null);
            staff.setConversationList(null);
            staff.setStudentList(null);
            staff.setId(null);
            staff.setStudentList(null);
            staff.getUnit().setStaffList(null);
            return staff;
        }

    }

    public StaffDTOwithStudents teachStudent(String token, String studentName) {
        Staff staff = staffRepository.findByToken(token);
        if(staff == null) throw new NullPointerException("Invalid token");
        Student student = studentRepository.findByStudentName(studentName);
        if(student == null) throw  new NullPointerException("This student is not exist");
        if(student.getStaffList().contains(staff)) throw new NullPointerException("This student has already been taught by this teacher");
        student.getStaffList().add(staff);
        studentRepository.save(student);
        StaffDTOwithStudents staffDTOwithStudents = new StaffDTOwithStudents();
        staffDTOwithStudents.setPhone(staff.getPhone());
        staffDTOwithStudents.setPosition(staff.getPosition());
        staffDTOwithStudents.setStudentList(staff.getStudentList());
        staffDTOwithStudents.setUnit(staff.getUnit());
        staffDTOwithStudents.getUnit().setStaffList(null);
        staffDTOwithStudents.setStaffName(staff.getStaffName());
        for (Student x :staffDTOwithStudents.getStudentList()) {
            x.setStaffList(null);
            x.setParent(null);
        }
        return  staffDTOwithStudents;
    }
}
