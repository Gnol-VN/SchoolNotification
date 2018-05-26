package uet.k59t.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uet.k59t.ScheduledTasks;
import uet.k59t.controller.dto.ParentDTO;
import uet.k59t.controller.dto.ParentDTOwithChildren;

import uet.k59t.controller.dto.StudentDTO;
import uet.k59t.model.Parent;

import uet.k59t.model.Student;
import uet.k59t.repository.ParentRepository;
import uet.k59t.repository.StudentRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Longlaptop on 11/29/2017.
 */
@Service
public class ParentService {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

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
            parentDTO.setParentName(parents.get(i).getParentName());
            parentDTO.setPhone(parents.get(i).getPhone());
            parentDTOS.add(parentDTO);
        }
        return parentDTOS;
    }


    public ParentDTO createParent(Parent parent) {
        if(parentRepository.findByParentName(parent.getParentName())==null ){
            parent.setToken(UUID.randomUUID().toString());
            parentRepository.save(parent);
            ParentDTO parentDTO = new ParentDTO();
            parentDTO.setParentName(parent.getParentName());
            parentDTO.setPhone(parent.getPhone());
            return parentDTO;
        }
        else throw new NullPointerException("Parent name is already exist");

    }

    public StudentDTO createStudent(String token,StudentDTO studentDTO) {
        Parent parent = parentRepository.findByToken(token);
        if(parent==null) throw  new NullPointerException("Invalid token");
        if(studentRepository.findByStudentName(studentDTO.getStudentName())!=null) throw  new NullPointerException("This student name is existed");
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
        parentDTOwithChildren.setPhone(parent.getPhone());
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

    public Parent login(Parent parentDTO) {
        Parent parent = parentRepository.findByEmail(parentDTO.getEmail());
        if(parent == null){
            throw new NullPointerException("This parent is not exist");
        }
        else if(!parent.getParentPassword().equals(parentDTO.getParentPassword())){
            throw new NullPointerException("Invalid username/password");
        }
        else {
            parent.setParentPassword(null);
            parent.setConversationList(null);
            parent.setParentId(null);
            return parent;
        }
    }

    @Scheduled(fixedRate = 5000)
    public List<ParentDTO> migrateDb() {
        List<ParentDTO> parentDTOList = new ArrayList<>();
        String sURL = "http://localhost/school1/index.php?admin/listAllParent";

        // Connect to the URL using java's native library
        URL url = null;
        try {
            url = new URL(sURL);
            URLConnection request = url.openConnection();
            request.connect();

            // Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
            JsonArray rootobj = root.getAsJsonArray(); //May be an array, may be an object.
            //just grab the zipcode

            for(int i = 0; i < rootobj.size(); i++){
                JsonObject parentGson = rootobj.get(i).getAsJsonObject();
                if(parentRepository.findByEmail(parentGson.get("email").getAsString()) == null) {
                    ParentDTO parentDTO = new ParentDTO();
                    Parent parent = new Parent();
                    parent.setParentId(parentGson.get("parent_id").getAsLong());
                    parent.setParentName(parentGson.get("name").getAsString());
                    parent.setParentPassword(parentGson.get("password").getAsString());
                    parent.setEmail(parentGson.get("email").getAsString());
                    parent.setPhone(parentGson.get("phone").getAsString());
                    parent.setToken(UUID.randomUUID().toString());
                    parentRepository.save(parent);
                    parentDTO.setPhone(parent.getPhone());
                    parentDTO.setParentName(parent.getParentName());
                    parentDTO.setParentId(parent.getParentId());
                    parentDTOList.add(parentDTO);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Migrate Parent DB");

        return parentDTOList;
    }
}
