package uet.k59t.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uet.k59t.model.Conversation;
import uet.k59t.model.Staff;

import java.util.List;

/**
 * Created by Longlaptop on 11/30/2017.
 */
@Repository
public interface ConversationRepository extends CrudRepository<Conversation,Long>{
    Conversation findByConversationName(String conversationName);
}
