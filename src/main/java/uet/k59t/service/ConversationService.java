package uet.k59t.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uet.k59t.controller.dto.*;
import uet.k59t.model.Conversation;
import uet.k59t.model.Message;
import uet.k59t.model.Parent;
import uet.k59t.model.Staff;
import uet.k59t.repository.ConversationRepository;
import uet.k59t.repository.MessageRepository;
import uet.k59t.repository.ParentRepository;
import uet.k59t.repository.StaffRepository;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Longlaptop on 11/30/2017.
 */
@Service
public class ConversationService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private MessageRepository messageRepository;
    public ConversationDTO createConversation(String token, ConversationDTO conversationDTO) {
        Staff staff = staffRepository.findByToken(token);
        Conversation conversation = new Conversation();
        conversation.setConversationName(conversationDTO.getConversationName());
        List<Staff> staffList = new ArrayList<Staff>();
        staffList.add(staff);
        conversation.setStaffList(staffList);
        conversationRepository.save(conversation);

        Staff staffWithLittleInfo = new Staff();
        staffWithLittleInfo.setStaffName(staff.getStaffName());
        staffWithLittleInfo.setUnit(staff.getUnit());
        staffWithLittleInfo.setPosition(staff.getPosition());
        staffList.clear();
        staffList.add(staffWithLittleInfo);
        conversationDTO.setStaffList(staffList);
        return conversationDTO;
    }

    public ConversationDTO join(String token, Long conversationId) {
        if(conversationRepository.findOne(conversationId) !=null){

            Conversation conversation = conversationRepository.findOne(conversationId);
            Staff staff;
            Parent parent;
            if(staffRepository.findByToken(token) !=null){
                staff = staffRepository.findByToken(token);
                if(!conversation.getStaffList().contains(staff))
                conversation.getStaffList().add(staff);
                else throw new NullPointerException("This staff is already joined the conversation");
            }
            else{
                parent = parentRepository.findByToken(token);
                if(!conversation.getParentList().contains(parent))
                conversation.getParentList().add(parent);
                else throw new NullPointerException("This parent is already joined the conversation");
            }
            conversationRepository.save(conversation);
            ConversationDTO conversationDTO = new ConversationDTO();
            conversationDTO.setConversationId(conversation.getConversationId());
            conversationDTO.setConversationName(conversation.getConversationName());
//            conversationDTO.setStaffList(conversationRepository.findOne(conversationId).getStaffList());
//            conversationDTO.setParentList(conversationRepository.findOne(conversationId).getParentList());
            return conversationDTO;
        }
        else throw new NullPointerException("Conversation not found");
    }

    public ConversationDTOwithStaffAndParent findById(String token, Long conversationId) {
        Conversation conversation = conversationRepository.findOne(conversationId);
        ConversationDTOwithStaffAndParent conversationDTOwithStaffAndParent = new ConversationDTOwithStaffAndParent();
        conversationDTOwithStaffAndParent.setConversationName(conversation.getConversationName());
        conversationDTOwithStaffAndParent.setConversationId(conversationId);
        List<MessageDTO> messageDTOListTOS = new ArrayList<MessageDTO>();
        List<Message> messageList = conversation.getMessageList();
        for (int i = 0; i < messageList.size(); i++) {
            MessageDTO messageDTO = new MessageDTO();
            if(messageList.get(i).getParent()!=null){
                messageDTO.setSenderId(messageList.get(i).getParent().getParentId());
                messageDTO.setSender(messageList.get(i).getParent().getParentName());
            }
            if(messageList.get(i).getStaff() != null){
                messageDTO.setSenderId(messageList.get(i).getStaff().getId());
                messageDTO.setSender(messageList.get(i).getStaff().getStaffName());
            }
            messageDTO.setMessageContent(messageList.get(i).getMessageContent());
            messageDTO.setMessageId(messageList.get(i).getMessageId());
            messageDTOListTOS.add(messageDTO);
        }

        conversationDTOwithStaffAndParent.setMessageDTOS(messageDTOListTOS);
        List<StaffDTO> staffDTOList = new ArrayList<StaffDTO>();
        for (int i = 0; i < conversation.getStaffList().size(); i++) {
            Staff staff = conversation.getStaffList().get(i);
            StaffDTO staffDTO = new StaffDTO();
            staffDTO.setStaffName(staff.getStaffName());
            staffDTO.setPosition(staff.getPosition());
            staffDTO.setUnit(staff.getUnit());
            staffDTOList.add(staffDTO);
        }
        conversationDTOwithStaffAndParent.setStaffDTOList(staffDTOList);

        List<ParentDTO> parentDTOList = new ArrayList<ParentDTO>();
        for (int i = 0; i < conversation.getParentList().size(); i++) {
            Parent parent = conversation.getParentList().get(i);
            ParentDTO parentDTO = new ParentDTO();
            parentDTO.setParentName(parent.getParentName());
            parentDTOList.add(parentDTO);
        }
        conversationDTOwithStaffAndParent.setParentDTOS(parentDTOList);
        return conversationDTOwithStaffAndParent;
    }

    public MessageDTO sendMessage(String token, Long conversationId, Message message) {
        Conversation conversation = conversationRepository.findOne(conversationId);
        if(conversation !=null){
            if(parentRepository.findByToken(token)!=null){
                Parent parent = parentRepository.findByToken(token);
                if(!conversation.getParentList().contains(parent)){
                    throw new NullPointerException("This parent is not in this conversation");
                }
                message.setParent(parent);
                message.setConversation(conversation);
                messageRepository.save(message);
                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setSenderId(parent.getParentId());
                messageDTO.setSender(parent.getParentName());
                messageDTO.setMessageContent(message.getMessageContent());
                return messageDTO;
            }
            else if(staffRepository.findByToken(token) != null){
                Staff staff = staffRepository.findByToken(token);
                if(!conversation.getStaffList().contains(staff)){
                    throw new NullPointerException("This staff is not in this converstation");
                }
                message.setStaff(staff);
                message.setConversation(conversation);
                messageRepository.save(message);
                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setSenderId(staff.getId());
                messageDTO.setSender(staff.getStaffName());
                messageDTO.setMessageContent(message.getMessageContent());
                return messageDTO;
            }
            else throw new NullPointerException("Invalid token");
        }
        else throw new NullPointerException("Conversation does not exist");
    }

    public ConversationDTOwithStaffAndParent invite(String token, Long conversationId, String parentName) {
        Conversation conversation = conversationRepository.findOne(conversationId);
        if(conversation != null){
            Staff staff = staffRepository.findByToken(token);
            if(staff!= null){
                Parent parent = parentRepository.findByParentName(parentName);
                if(conversation.getParentList().contains(parent)) throw new NullPointerException("This parent is already in");
                conversation.getParentList().add(parent);
                conversationRepository.save(conversation);
                ConversationDTOwithStaffAndParent conversationDTOwithStaffAndParent = new ConversationDTOwithStaffAndParent();
                conversationDTOwithStaffAndParent.setConversationName(conversation.getConversationName());
                conversationDTOwithStaffAndParent.setConversationId(conversationId);
                List<MessageDTO> messageDTOListTOS = new ArrayList<MessageDTO>();
                List<Message> messageList = conversation.getMessageList();
                for (int i = 0; i < messageList.size(); i++) {
                    MessageDTO messageDTO = new MessageDTO();
                    if(messageList.get(i).getParent()!=null){
                        messageDTO.setSenderId(messageList.get(i).getParent().getParentId());
                        messageDTO.setSender(messageList.get(i).getParent().getParentName());
                    }
                    if(messageList.get(i).getStaff() != null){
                        messageDTO.setSenderId(messageList.get(i).getStaff().getId());
                        messageDTO.setSender(messageList.get(i).getStaff().getStaffName());
                    }
                    messageDTO.setMessageContent(messageList.get(i).getMessageContent());
                    messageDTO.setMessageId(messageList.get(i).getMessageId());
                    messageDTOListTOS.add(messageDTO);
                }

                conversationDTOwithStaffAndParent.setMessageDTOS(messageDTOListTOS);
                List<StaffDTO> staffDTOList = new ArrayList<StaffDTO>();
                for (int i = 0; i < conversation.getStaffList().size(); i++) {
                    Staff staff1 = conversation.getStaffList().get(i);
                    StaffDTO staffDTO = new StaffDTO();
                    staffDTO.setStaffName(staff1.getStaffName());
                    staffDTO.setPosition(staff1.getPosition());
                    staffDTO.setUnit(staff1.getUnit());
                    staffDTOList.add(staffDTO);
                }
                conversationDTOwithStaffAndParent.setStaffDTOList(staffDTOList);

                List<ParentDTO> parentDTOList = new ArrayList<ParentDTO>();
                for (int i = 0; i < conversation.getParentList().size(); i++) {
                    Parent parent1 = conversation.getParentList().get(i);
                    ParentDTO parentDTO = new ParentDTO();
                    parentDTO.setParentName(parent1.getParentName());
                    parentDTOList.add(parentDTO);
                }
                conversationDTOwithStaffAndParent.setParentDTOS(parentDTOList);
                return conversationDTOwithStaffAndParent;
            }
            else throw new NullPointerException("Invalid token");
        }
        else throw new NullPointerException("Conversation not found");
    }
}
