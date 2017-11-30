package uet.k59t.controller.dto;

import uet.k59t.model.Staff;

import java.util.List;

/**
 * Created by Longlaptop on 11/28/2017.
 */
public class StudentDTO {
    private Long studentId;
    private String studentName;
    private ParentDTO parentDTO;
    private List<StaffDTO> staffDTOList;

    public List<StaffDTO> getStaffDTOList() {
        return staffDTOList;
    }

    public void setStaffDTOList(List<StaffDTO> staffDTOList) {
        this.staffDTOList = staffDTOList;
    }

    public ParentDTO getParentDTO() {
        return parentDTO;
    }

    public void setParentDTO(ParentDTO parentDTO) {
        this.parentDTO = parentDTO;
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

}
