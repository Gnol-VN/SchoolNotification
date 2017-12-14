package uet.k59t.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uet.k59t.controller.dto.ConversationDTO;
import uet.k59t.controller.dto.ConversationDTOwithStaffAndParent;
import uet.k59t.controller.dto.MessageDTO;
import uet.k59t.model.Message;
import uet.k59t.service.ConversationService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Longlaptop on 11/30/2017.
 */
@RestController
public class ConversationController {
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private ConversationService conversationService;
    @RequestMapping(value = "conversation/create", method = RequestMethod.POST)
    public ConversationDTO createConversation(@RequestBody ConversationDTO conversationDTO){
        String token = httpServletRequest.getHeader("token");
        return conversationService.createConversation(token,conversationDTO);
    }

//    @RequestMapping(value = "conversation/join/{conversationId}", method = RequestMethod.GET)
//    public ConversationDTO joinConversation(@PathVariable Long conversationId){
//        String token = httpServletRequest.getHeader("token");
//        return conversationService.join(token,conversationId);
//    }

    @RequestMapping(value = "conversation/inviteparent/{conversationId}/{parentName}", method = RequestMethod.POST)
    public ConversationDTOwithStaffAndParent inviteParent(@PathVariable Long conversationId, @PathVariable String parentName){
        String token = httpServletRequest.getHeader("token");
        return conversationService.inviteParent(token,conversationId,parentName);

    }
    @RequestMapping(value = "conversation/invitestaff/{conversationId}/{staffName}", method = RequestMethod.POST)
    public ConversationDTOwithStaffAndParent inviteStaff(@PathVariable Long conversationId, @PathVariable String staffName){
        String token = httpServletRequest.getHeader("token");
        return conversationService.inviteStaff(token,conversationId,staffName);

    }

    @RequestMapping(value = "conversation/find/byconversationid/{conversationId}", method = RequestMethod.GET)
    public ConversationDTOwithStaffAndParent findById(@PathVariable Long conversationId){
        String token = httpServletRequest.getHeader("token");
        return conversationService.findById(token, conversationId);
    }

    @RequestMapping(value = "conversation/sendmessage/{conversationId}", method = RequestMethod.POST)
    public MessageDTO sendMessage(@PathVariable Long conversationId, @RequestBody Message message){
        String token = httpServletRequest.getHeader("token");
        return conversationService.sendMessage(token,conversationId,message);
    }
    @RequestMapping(value = "conversation/containme", method = RequestMethod.GET)
    public List<ConversationDTOwithStaffAndParent> findConversationContainMe(){
        String token = httpServletRequest.getHeader("token");
        return conversationService.findConversationContainMe(token);
    }
}
