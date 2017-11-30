package uet.k59t.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uet.k59t.model.Position;

/**
 * Created by Longlaptop on 11/28/2017.
 */
@Repository
public interface PositionRepostiory extends CrudRepository<Position,Long>{
    Position findByPositionName(String positionName);
}
