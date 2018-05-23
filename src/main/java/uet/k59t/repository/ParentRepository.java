package uet.k59t.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uet.k59t.model.Parent;

/**
 * Created by Longlaptop on 11/29/2017.
 */
@Repository
public interface ParentRepository extends CrudRepository<Parent,Long> {
    Parent findByParentName(String parentName);
    Parent findByToken(String token);
    Parent findByEmail(String email);
}
