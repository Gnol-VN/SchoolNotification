package uet.k59t.controller.dto;

import uet.k59t.model.Position;
import uet.k59t.model.Student;
import uet.k59t.model.Unit;

import java.util.List;

/**
 * Created by Longlaptop on 11/29/2017.
 */
public class StaffDTOwithStudents {
    private String staffName;
    private Unit unit;
    private Position position;
    private List<Student> studentList;

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }
}
