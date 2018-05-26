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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Long on 11/21/2016.
 */
@Service
public class StaffService {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

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
//            staffDTO.getUnit().setStaffList(null);
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
//        staffDTOwithStudents.getUnit().setStaffList(null);
        return staffDTOwithStudents;
    }

    public Staff login(StaffDTO staffDTO) {
        Staff staff = staffRepository.findByEmail(staffDTO.getEmail());
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
//            staff.getUnit().setStaffList(null);
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
//        staffDTOwithStudents.getUnit().setStaffList(null);
        staffDTOwithStudents.setStaffName(staff.getStaffName());
        for (Student x :staffDTOwithStudents.getStudentList()) {
            x.setStaffList(null);
            x.setParent(null);
        }
        return  staffDTOwithStudents;
    }

    @Scheduled(fixedRate = 5000)
    public List<StaffDTO> migrateDb() {
        List<StaffDTO> staffDTOList = new ArrayList<>();
        String sURL = "http://localhost/school1/index.php?admin/listAllTeacher"; //just a string

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
                JsonObject teacherGson = rootobj.get(i).getAsJsonObject();
                if(staffRepository.findByEmail(teacherGson.get("email").getAsString()) == null) {
                    StaffDTO staffDTO = new StaffDTO();
                    Staff staff = new Staff();
                    staff.setId(teacherGson.get("teacher_id").getAsLong());
                    staff.setStaffName(teacherGson.get("name").getAsString());
                    staff.setPassword(teacherGson.get("password").getAsString());
                    staff.setEmail(teacherGson.get("email").getAsString());
                    staff.setPhone(teacherGson.get("phone").getAsString());
                    staff.setToken(UUID.randomUUID().toString());
                    staffRepository.save(staff);
                    staffDTO.setPhone(staff.getPhone());
                    staffDTO.setStaffName(staff.getStaffName());
                    staffDTO.setPassword(staff.getPassword());
                    staffDTOList.add(staffDTO);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Migrate Staff DB");

        return staffDTOList;

    }
}
