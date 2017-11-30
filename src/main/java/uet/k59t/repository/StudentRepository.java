package uet.k59t.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uet.k59t.model.Parent;
import uet.k59t.model.Staff;
import uet.k59t.model.Student;

import java.util.List;

/**
 * Created by Longlaptop on 11/28/2017.
 */
@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {
    Student findByStudentName(String studentName);

    List<Student> findByParent(Parent parent);
    List<Student> findByStaffList_StaffName(String staffName);
}
