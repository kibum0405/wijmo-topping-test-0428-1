package springbootwijmotopping.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import org.springframework.context.ApplicationContext;
import springbootwijmotopping.DepartmentApplication;
import springbootwijmotopping.domain.DepartmentAdded;

@Entity
@Table(name = "Department_table")
@Data
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String department;

    @PostPersist
    public void onPostPersist() {
        DepartmentAdded departmentAdded = new DepartmentAdded(this);
        departmentAdded.publishAfterCommit();
    }

    public static DepartmentRepository repository() {
        DepartmentRepository departmentRepository = applicationContext()
            .getBean(DepartmentRepository.class);
        return departmentRepository;
    }

    public static ApplicationContext applicationContext() {
        return DepartmentApplication.applicationContext;
    }
}
