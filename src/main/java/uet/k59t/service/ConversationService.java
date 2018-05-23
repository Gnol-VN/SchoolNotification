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
        Parent parent = parentRepository.findByToken(token);
        Conversation conversation = new Conversation();
        conversation.setConversationName(conversationDTO.getConversationName());
        if(staff != null){
            List<Staff> staffList = new ArrayList<Staff>();
            staffList.add(staff);
            conversation.setStaffList(staffList);
            conversationRepository.save(conversation);

            Staff staffWithLittleInfo = new Staff();
            staffWithLittleInfo.setStaffName(staff.getStaffName());
            staffWithLittleInfo.setUnit(staff.getUnit());
            staffWithLittleInfo.getUnit().setStaffList(null);
            staffWithLittleInfo.setPosition(staff.getPosition());
            staffList.clear();
            staffList.add(staffWithLittleInfo);
            conversationDTO.setStaffList(staffList);
        }
        else if (parent != null){
            List<Parent> parentList = new ArrayList<>();
            parentList.add(parent);
            conversation.setParentList(parentList);
            conversationRepository.save(conversation);
            Parent parentWithLittleInfo = new Parent();
            parentWithLittleInfo.setParentName(parent.getParentName());
            parentWithLittleInfo.setPhone(parent.getPhone());;
            parentList.clear();
            parentList.add(parentWithLittleInfo);
            conversationDTO.setParentList(parentList);
        }
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
        Staff staff1 = staffRepository.findByToken(token);
        Parent parent1 = parentRepository.findByToken(token);
        if(!conversation.getStaffList().contains(staff1) && !conversation.getParentList().contains(parent1))
            throw new NullPointerException("This person is not in conversation");
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
            staffDTO.setPhone(staff.getPhone());
            staffDTO.setUnit(staff.getUnit());
            staffDTO.getUnit().setStaffList(null);
            staffDTOList.add(staffDTO);
        }
        conversationDTOwithStaffAndParent.setStaffDTOList(staffDTOList);

        List<ParentDTO> parentDTOList = new ArrayList<ParentDTO>();
        for (int i = 0; i < conversation.getParentList().size(); i++) {
            Parent parent = conversation.getParentList().get(i);
            ParentDTO parentDTO = new ParentDTO();
            parentDTO.setParentName(parent.getParentName());
            parentDTO.setPhone(parent.getPhone());
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

    public ConversationDTOwithStaffAndParent inviteParent(String token, Long conversationId, String parentName) {
        Conversation conversation = conversationRepository.findOne(conversationId);
        if(conversation != null){
            Staff checkStaff = staffRepository.findByToken(token);
            Parent checkParent = parentRepository.findByToken(token);

            if(checkStaff!= null || checkParent != null){
                if(conversation.getStaffList().contains(checkStaff) || conversation.getParentList().contains(checkParent)){
                    Parent parent = parentRepository.findByParentName(parentName);
                    if(parent==null) throw new NullPointerException("This parent name is not exist");
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
                        staffDTO.getUnit().setStaffList(null);
                        staffDTO.setPhone(staff1.getPhone());
                        staffDTOList.add(staffDTO);
                    }
                    conversationDTOwithStaffAndParent.setStaffDTOList(staffDTOList);

                    List<ParentDTO> parentDTOList = new ArrayList<ParentDTO>();
                    for (int i = 0; i < conversation.getParentList().size(); i++) {
                        Parent parent1 = conversation.getParentList().get(i);
                        ParentDTO parentDTO = new ParentDTO();
                        parentDTO.setParentName(parent1.getParentName());
                        parentDTO.setPhone(parent1.getPhone());
                        parentDTOList.add(parentDTO);
                    }
                    conversationDTOwithStaffAndParent.setParentDTOS(parentDTOList);
                    return conversationDTOwithStaffAndParent;
                }
                else throw new NullPointerException("This person is not in current conversation");

            }

            else throw new NullPointerException("Invalid token");
        }
        else throw new NullPointerException("Conversation not found");
    }
    public ConversationDTOwithStaffAndParent inviteStaff(String token, Long conversationId, String staffName) {
        Conversation conversation = conversationRepository.findOne(conversationId);
        if(conversation != null){
            Staff checkStaff = staffRepository.findByToken(token);
            Parent checkParent = parentRepository.findByToken(token);
            if(checkStaff!= null || checkParent != null){
                if(conversation.getStaffList().contains(checkStaff) || conversation.getParentList().contains(checkParent)){
                    Staff staff = staffRepository.findBystaffName(staffName);
                    if(staff==null) throw new NullPointerException("This staff name is not exist");
                    if(conversation.getStaffList().contains(staff)) throw new NullPointerException("This staff is already in");
                    conversation.getStaffList().add(staff);
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
                        staffDTO.getUnit().setStaffList(null);
                        staffDTO.setPhone(staff1.getPhone());
                        staffDTOList.add(staffDTO);
                    }
                    conversationDTOwithStaffAndParent.setStaffDTOList(staffDTOList);

                    List<ParentDTO> parentDTOList = new ArrayList<ParentDTO>();
                    for (int i = 0; i < conversation.getParentList().size(); i++) {
                        Parent parent1 = conversation.getParentList().get(i);
                        ParentDTO parentDTO = new ParentDTO();
                        parentDTO.setParentName(parent1.getParentName());
                        parentDTO.setPhone(parent1.getPhone());
                        parentDTOList.add(parentDTO);
                    }
                    conversationDTOwithStaffAndParent.setParentDTOS(parentDTOList);
                    return conversationDTOwithStaffAndParent;
                }
                else throw new NullPointerException("This person is not in current conversation");

            }

            else throw new NullPointerException("Invalid token");
        }
        else throw new NullPointerException("Conversation not found");
    }
    public List<ConversationDTOwithStaffAndParent> findConversationContainMe(String token) {
        Parent parent = parentRepository.findByToken(token);
        Staff staff = staffRepository.findByToken(token);
        //Kiểm tra xem token gửi lên là phụ huynh hay nhân viên
        if(parent!=null){
            List<Conversation> conversationList = parent.getConversationList();
            List<ConversationDTOwithStaffAndParent> returnList = new ArrayList<>();

            //Lấy ra tất cả conversation mà phụ huynh có mặt
            for (int i = 0; i < conversationList.size(); i++) {
                Conversation conversation = conversationList.get(i);
                ConversationDTOwithStaffAndParent conversationDTOwithStaffAndParent = new ConversationDTOwithStaffAndParent();
                conversationDTOwithStaffAndParent.setConversationName(conversation.getConversationName());
                conversationDTOwithStaffAndParent.setConversationId(conversation.getConversationId());
                //Sửa lỗi bad String, Unexpected '' đối với messageList
                List<MessageDTO> messageList = new ArrayList<>();
                for (Message x:conversation.getMessageList()) {
                    MessageDTO messageDTO = new MessageDTO();
                    messageDTO.setMessageId(x.getMessageId());
                    messageDTO.setMessageContent(x.getMessageContent());
                    if(x.getStaff()!=null){
                        messageDTO.setSender(x.getStaff().getStaffName());
                        messageDTO.setSenderId(x.getStaff().getId());
                    }
                    if(x.getParent()!=null){
                        messageDTO.setSender(x.getParent().getParentName());
                        messageDTO.setSenderId(x.getParent().getParentId());
                    }
                    messageList.add(messageDTO);
                }

                //Sửa lỗi bad String, Unexpected '' đối với parentList
                List<ParentDTO> parentDTOList = new ArrayList<>();
                for (Parent x:conversation.getParentList()) {
                    ParentDTO parentDTO = new ParentDTO();
                    parentDTO.setPhone(x.getPhone());
                    parentDTO.setParentName(x.getParentName());
                    parentDTO.setParentId(x.getParentId());
                    parentDTOList.add(parentDTO);
                }
                conversationDTOwithStaffAndParent.setParentDTOS(parentDTOList);

                //Sửa lỗi bad String, Unexpected '' đối với staffList
                List<StaffDTO> staffDTOList = new ArrayList<>();
                for(Staff x:conversation.getStaffList()){
                    StaffDTO staffDTO = new StaffDTO();
                    staffDTO.setPhone(x.getPhone());
                    staffDTO.setPosition(x.getPosition());
                    staffDTO.setUnit(x.getUnit());
                    staffDTO.getUnit().setStaffList(null);
                    staffDTO.setStaffName(x.getStaffName());
                    staffDTOList.add(staffDTO);
                }
                conversationDTOwithStaffAndParent.setStaffDTOList(staffDTOList);

                //Thêm phần tử vào returnList
                returnList.add(conversationDTOwithStaffAndParent);
            }
            return returnList;
        }
        else if(staff!=null){
            List<Conversation> conversationList = staff.getConversationList();
            List<ConversationDTOwithStaffAndParent> returnList = new ArrayList<>();

            //Lấy ra tất cả conversation mà phụ huynh có mặt
            for (int i = 0; i < conversationList.size(); i++) {
                Conversation conversation = conversationList.get(i);
                ConversationDTOwithStaffAndParent conversationDTOwithStaffAndParent = new ConversationDTOwithStaffAndParent();
                conversationDTOwithStaffAndParent.setConversationName(conversation.getConversationName());
                conversationDTOwithStaffAndParent.setConversationId(conversation.getConversationId());
                //Sửa lỗi bad String, Unexpected '' đối với messageList
                List<MessageDTO> messageList = new ArrayList<>();
                for (Message x:conversation.getMessageList()) {
                    MessageDTO messageDTO = new MessageDTO();
                    messageDTO.setMessageId(x.getMessageId());
                    messageDTO.setMessageContent(x.getMessageContent());
                    if(x.getStaff()!=null){
                        messageDTO.setSender(x.getStaff().getStaffName());
                        messageDTO.setSenderId(x.getStaff().getId());
                    }
                    if(x.getParent()!=null){
                        messageDTO.setSender(x.getParent().getParentName());
                        messageDTO.setSenderId(x.getParent().getParentId());
                    }
                    messageList.add(messageDTO);
                }
                conversationDTOwithStaffAndParent.setMessageDTOS(messageList);
                //Sửa lỗi bad String, Unexpected '' đối với parentList
                List<ParentDTO> parentDTOList = new ArrayList<>();
                for (Parent x:conversation.getParentList()) {
                    ParentDTO parentDTO = new ParentDTO();
                    parentDTO.setPhone(x.getPhone());
                    parentDTO.setParentName(x.getParentName());
                    parentDTO.setParentId(x.getParentId());
                    parentDTOList.add(parentDTO);
                }
                conversationDTOwithStaffAndParent.setParentDTOS(parentDTOList);

                //Sửa lỗi bad String, Unexpected '' đối với staffList
                List<StaffDTO> staffDTOList = new ArrayList<>();
                for(Staff x:conversation.getStaffList()){
                    StaffDTO staffDTO = new StaffDTO();
                    staffDTO.setPhone(x.getPhone());
                    staffDTO.setPosition(x.getPosition());
                    staffDTO.setUnit(x.getUnit());
                    staffDTO.getUnit().setStaffList(null);
                    staffDTO.setStaffName(x.getStaffName());
                    staffDTOList.add(staffDTO);
                }
                conversationDTOwithStaffAndParent.setStaffDTOList(staffDTOList);

                //Thêm phần tử vào returnList
                returnList.add(conversationDTOwithStaffAndParent);
            }
            return returnList;
        }
        else throw new NullPointerException("You are not belong to any conversation");
    }
}
