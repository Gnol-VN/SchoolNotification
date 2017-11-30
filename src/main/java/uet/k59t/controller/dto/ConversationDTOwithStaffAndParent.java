package uet.k59t.controller.dto;

import java.util.List;

/**
 * Created by Longlaptop on 11/30/2017.
 */

public class ConversationDTOwithStaffAndParent {
    private String conversationName;
    private Long conversationId;
    private List<StaffDTO> staffDTOList;
    private List<ParentDTO> parentDTOS;
    private List<MessageDTO> messageDTOS;

    public List<MessageDTO> getMessageDTOS() {
        return messageDTOS;
    }

    public void setMessageDTOS(List<MessageDTO> messageDTOS) {
        this.messageDTOS = messageDTOS;
    }

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public List<StaffDTO> getStaffDTOList() {
        return staffDTOList;
    }

    public void setStaffDTOList(List<StaffDTO> staffDTOList) {
        this.staffDTOList = staffDTOList;
    }

    public List<ParentDTO> getParentDTOS() {
        return parentDTOS;
    }

    public void setParentDTOS(List<ParentDTO> parentDTOS) {
        this.parentDTOS = parentDTOS;
    }
}
