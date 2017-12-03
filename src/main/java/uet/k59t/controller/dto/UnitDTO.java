package uet.k59t.controller.dto;

import uet.k59t.model.Staff;

import java.util.List;

/**
 * Created by Longlaptop on 12/3/2017.
 */
public class UnitDTO {
    private Long unitId;
    private String unitName;
    private List<StaffDTOwithPositionAndUnitname> staffList;

    public List<StaffDTOwithPositionAndUnitname> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<StaffDTOwithPositionAndUnitname> staffList) {
        this.staffList = staffList;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }


}
