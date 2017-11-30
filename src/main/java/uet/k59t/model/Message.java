package uet.k59t.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Longlaptop on 11/30/2017.
 */
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Long  messageId;
    private String messageContent;

    @ManyToOne
    @JoinColumn(name = "parentId")
    private Parent parent;

    @ManyToOne
    @JoinColumn(name = "staffId")
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "conversationId")
    private Conversation conversation;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
}
