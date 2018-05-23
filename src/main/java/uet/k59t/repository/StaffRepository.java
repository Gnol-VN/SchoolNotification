package uet.k59t.repository;

import org.springframework.data.repository.CrudRepository;
import uet.k59t.model.Staff;

import java.util.List;

/**
 * Created by Long on 11/21/2016.
 */
public interface StaffRepository extends CrudRepository<Staff, Long> {
    Staff findBystaffName(String staffName);
    Staff findByToken(String token);
    Staff findByEmail(String email);
    List<Staff> findByStudentList_StudentName(String studentName);
}
