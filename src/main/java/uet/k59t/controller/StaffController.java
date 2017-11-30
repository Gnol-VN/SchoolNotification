package uet.k59t.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uet.k59t.controller.dto.StaffDTO;
import uet.k59t.controller.dto.StaffDTOwithStudents;
import uet.k59t.service.StaffService;

/**
 * Created by Long on 11/21/2016.
 */
@RestController
public class StaffController {
    @Autowired
    private StaffService staffService;

    //create staff
    @RequestMapping(value = "staff/createstaff", method = RequestMethod.POST)
    public StaffDTO createUser(@RequestBody StaffDTO staffDTO){
        return staffService.createStaff(staffDTO);
    }

    //find staff by id
    @RequestMapping(value = "staff/findstaff/bystaffid/{staffId}", method = RequestMethod.GET)
    public StaffDTO findUser(@PathVariable("staffId") Long id){
        return staffService.getStaffById(id);
    }

    //find student of one staff
    @RequestMapping(value = "staff/find/bystaffname/{staffName}", method = RequestMethod.GET)
    public StaffDTOwithStudents findByStaffName(@PathVariable String staffName){
        return staffService.findByStaffName(staffName);

    }


}
