package uet.k59t.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uet.k59t.controller.dto.AdminDTO;
import uet.k59t.controller.dto.StaffDTO;
import uet.k59t.controller.dto.StaffDTOwithPositionAndUnitname;
import uet.k59t.controller.dto.StaffDTOwithStudents;
import uet.k59t.model.Staff;
import uet.k59t.service.StaffService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Null;
import java.util.List;

/**
 * Created by Long on 11/21/2016.
 */
@RestController
public class StaffController {
    @Autowired
    private StaffService staffService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping(value = "staff/migratedb", method = RequestMethod.GET)
    public List<StaffDTO> migratedbStaff(){
        return staffService.migrateDb();
    }
    @RequestMapping(value = "admin/login", method = RequestMethod.POST)
    public AdminDTO adminLogin(@RequestBody AdminDTO adminDTO){
        if(adminDTO.getAdminUsername().equals("admin") && adminDTO.getAdminPassword().equals("admin")){
            adminDTO.setAdminPassword(null);
            adminDTO.setAuthorized(true);
            return adminDTO;
        }
        else throw new NullPointerException("Invalid admin username/password");
    }
    //create staff
    @RequestMapping(value = "staff/createstaff", method = RequestMethod.POST)
    public StaffDTO createUser(@RequestBody StaffDTO staffDTO){
        return staffService.createStaff(staffDTO);
    }

    //find staff by id
    @RequestMapping(value = "staff/findstaff/bystaffid/{staffId}", method = RequestMethod.GET)
    public StaffDTO findUser(@PathVariable("staffId") Long id){
        String token = httpServletRequest.getHeader("token");
        return staffService.getStaffById(id);
    }

    //find student of one staff
    @RequestMapping(value = "staff/find/bystaffname/{staffName}", method = RequestMethod.GET)
    public StaffDTOwithStudents findByStaffName(@PathVariable String staffName){
        String token = httpServletRequest.getHeader("token");
        return staffService.findByStaffName(staffName);

    }
    @RequestMapping(value = "staff/teachstudent/{studentName}", method = RequestMethod.POST)
    public StaffDTOwithStudents teachStudent(@PathVariable String studentName){
        String token = httpServletRequest.getHeader("token");
        return staffService.teachStudent(token, studentName);
    }
    @RequestMapping(value = "staff/login", method = RequestMethod.POST)
    public Staff login(@RequestBody StaffDTO staffDTO){

        return staffService.login(staffDTO);
    }


}
