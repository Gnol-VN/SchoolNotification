package uet.k59t.controller.dto;

import uet.k59t.model.Student;

import java.util.List;

/**
 * Created by Longlaptop on 11/29/2017.
 */
public class ParentDTOwithChildren {
    private String parentName;
    private List<StudentDTO> studentDTOList;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<StudentDTO> getStudentDTOList() {
        return studentDTOList;
    }

    public void setStudentDTOList(List<StudentDTO> studentDTOList) {
        this.studentDTOList = studentDTOList;
    }
}
