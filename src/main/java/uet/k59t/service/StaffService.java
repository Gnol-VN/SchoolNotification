package uet.k59t.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uet.k59t.controller.dto.StaffDTO;
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
        //Get studentList of this staff
        List<Student> studentList  = studentRepository.findByStaffList_StaffName(staffName);
        for (int i = 0; i < studentList.size(); i++) {
            studentList.get(i).setStaffList(null);
        }
        staffDTOwithStudents.setStudentList(studentList);
        return staffDTOwithStudents;
    }
}
