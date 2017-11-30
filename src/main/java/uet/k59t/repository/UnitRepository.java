package uet.k59t.repository;

import org.springframework.data.repository.CrudRepository;
import uet.k59t.model.Unit;

/**
 * Created by Longlaptop on 11/28/2017.
 */
public interface UnitRepository extends CrudRepository<Unit, Long> {
    Unit findByUnitName(String unitName);
}
