package uet.k59t.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Longlaptop on 11/28/2017.
 */
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long studentId;
    private String studentName;

    @ManyToMany
    @JoinTable(name = "Student_Staff",
        joinColumns = {
                @JoinColumn(name = "studentId")
        }, inverseJoinColumns = {
            @JoinColumn(name = "staffId")
            }
    )
    private List<Staff> staffList;

    @ManyToOne
    @JoinColumn(name = "parentId")
    private Parent parent;

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public List<Staff> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<Staff> staffList) {
        this.staffList = staffList;
    }
}
