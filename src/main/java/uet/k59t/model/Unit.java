package uet.k59t.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Longlaptop on 11/28/2017.
 */
@Entity
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long unitId;
    private String unitName;
    @OneToMany(mappedBy = "unit")
    private List<Staff> staffList;

    public List<Staff> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<Staff> staffList) {
        this.staffList = staffList;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }
}
