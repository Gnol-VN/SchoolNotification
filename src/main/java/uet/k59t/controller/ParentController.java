package uet.k59t.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uet.k59t.controller.dto.ParentDTO;
import uet.k59t.controller.dto.ParentDTOwithChildren;
import uet.k59t.controller.dto.StaffDTO;
import uet.k59t.controller.dto.StudentDTO;
import uet.k59t.model.Parent;
import uet.k59t.service.ParentService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Longlaptop on 11/29/2017.
 */
@RestController
public class ParentController {
    @Autowired
    ParentService parentService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping(value = "parent/migratedb", method = RequestMethod.GET)
    public List<ParentDTO> migratedbStaff(){

        return parentService.migrateDb();
    }

    @RequestMapping(value = "parent/showall", method = RequestMethod.GET)
    public List<ParentDTO> parentDTOS(){
        String token = httpServletRequest.getHeader("token");
        return parentService.showall();
    }

    @RequestMapping(value = "parent/createparent", method = RequestMethod.POST)
    public ParentDTO createParent(@RequestBody Parent parent){
        String token = httpServletRequest.getHeader("token");
        return parentService.createParent(parent);
    }

    //find children of one parent
    @RequestMapping(value = "parent/find/byparentname/{parentName}", method = RequestMethod.GET)
    public ParentDTOwithChildren findByParentName(@PathVariable String parentName){
        String token = httpServletRequest.getHeader("token");
        return parentService.findByParentName(parentName);
    }
    @RequestMapping(value = "/parent/createstudent",method = RequestMethod.POST)
    public StudentDTO createStudent(@RequestBody StudentDTO studentDTO){
        String token = httpServletRequest.getHeader("token");
        return parentService.createStudent(token,studentDTO);
    }
    @RequestMapping(value = "parent/login", method = RequestMethod.POST)
    public Parent login(@RequestBody Parent parentDTO){
        String token = httpServletRequest.getHeader("token");
        return parentService.login(parentDTO);
    }
}
