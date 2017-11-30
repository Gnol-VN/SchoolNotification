package uet.k59t.repository;

import org.springframework.data.repository.CrudRepository;
import uet.k59t.model.Message;

/**
 * Created by Longlaptop on 11/30/2017.
 */
public interface MessageRepository extends CrudRepository<Message,Long>{

}
