package uet.k59t.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Longlaptop on 11/28/2017.
 */
@Entity
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String staffName;
    private String phone;
    @ManyToOne(optional = true)
    @JoinColumn(name = "positionId")
    private Position position;
    @ManyToOne(optional = true)
    @JoinColumn(name = "unitId")
    private Unit unit;
    @ManyToMany(mappedBy = "staffList")
    private List<Student> studentList;
    @ManyToMany(mappedBy = "staffList")
    private List<Conversation> conversationList;
    private String password;
    private String token;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Conversation> getConversationList() {
        return conversationList;
    }

    public void setConversationList(List<Conversation> conversationList) {
        this.conversationList = conversationList;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
