package uet.k59t.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uet.k59t.controller.dto.ParentDTO;
import uet.k59t.controller.dto.ParentDTOwithChildren;
import uet.k59t.controller.dto.StudentDTO;
import uet.k59t.model.Parent;
import uet.k59t.service.ParentService;

import java.util.List;

/**
 * Created by Longlaptop on 11/29/2017.
 */
@RestController
public class ParentController {
    @Autowired
    ParentService parentService;

    @RequestMapping(value = "parent/showall", method = RequestMethod.GET)
    public List<ParentDTO> parentDTOS(){
        return parentService.showall();
    }

    @RequestMapping(value = "parent/createparent", method = RequestMethod.POST)
    public ParentDTO createParent(@RequestBody Parent parent){
        return parentService.createParent(parent);
    }

    //find children of one parent
    @RequestMapping(value = "parent/find/byparentname/{parentName}", method = RequestMethod.GET)
    public ParentDTOwithChildren findByParentName(@PathVariable String parentName){
        return parentService.findByParentName(parentName);
    }
    @RequestMapping(value = "/parent/createstudent",method = RequestMethod.POST)
    public StudentDTO createStudent(@RequestBody StudentDTO studentDTO){
        return parentService.createStudent(studentDTO);
    }
}
