package uet.k59t.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uet.k59t.controller.dto.ConversationDTO;
import uet.k59t.controller.dto.ConversationDTOwithStaffAndParent;
import uet.k59t.controller.dto.MessageDTO;
import uet.k59t.model.Message;
import uet.k59t.service.ConversationService;

import javax.servlet.http.HttpServletRequest;

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

    @RequestMapping(value = "conversation/invite/{conversationId}/{parentName}", method = RequestMethod.POST)
    public ConversationDTOwithStaffAndParent invite(@PathVariable Long conversationId, @PathVariable String parentName){
        String token = httpServletRequest.getHeader("token");
        return conversationService.invite(token,conversationId,parentName);

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
}
