package uet.k59t.controller.dto;

import uet.k59t.model.Parent;
import uet.k59t.model.Staff;

import java.util.List;

/**
 * Created by Longlaptop on 11/30/2017.
 */
public class ConversationDTO {
    private Long conversationId;
    private String conversationName;
    private List<Parent> parentList;
    private List<Staff> staffList;

    public List<Parent> getParentList() {
        return parentList;
    }

    public void setParentList(List<Parent> parentList) {
        this.parentList = parentList;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    public List<Staff> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<Staff> staffList) {
        this.staffList = staffList;
    }
}
