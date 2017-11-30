package uet.k59t.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Longlaptop on 11/30/2017.
 */
@Entity
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long conversationId;
    private String conversationName;
    @OneToMany(mappedBy = "conversation")
    private List<Message> messageList;

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    @ManyToMany
    @JoinTable(name = "Conversation_Staff",
            joinColumns = {
                    @JoinColumn(name = "conversationId")
            }, inverseJoinColumns = {
            @JoinColumn(name = "staffId")
    }
    )
    private List<Staff> staffList;

    @ManyToMany
    @JoinTable(name = "Conversation_Parent",
            joinColumns = {
                    @JoinColumn(name = "conversationId")
            }, inverseJoinColumns = {
            @JoinColumn(name = "parentId")
    }
    )
    private List<Parent> parentList;

    public List<Parent> getParentList() {
        return parentList;
    }

    public void setParentList(List<Parent> parentList) {
        this.parentList = parentList;
    }

    public List<Staff> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<Staff> staffList) {
        this.staffList = staffList;
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
}
