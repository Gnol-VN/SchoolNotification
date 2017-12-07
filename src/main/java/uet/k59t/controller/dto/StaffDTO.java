package uet.k59t.controller.dto;

import uet.k59t.model.Position;
import uet.k59t.model.Unit;

/**
 * Created by Long on 11/21/2016.
 */
public class StaffDTO {
    private String staffName;
    private String password;
    private Unit unit;
    private Position position;
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
}
