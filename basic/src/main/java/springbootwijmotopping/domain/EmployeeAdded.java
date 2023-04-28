package springbootwijmotopping.domain;

import java.util.*;
import lombok.*;
import springbootwijmotopping.domain.*;
import springbootwijmotopping.infra.AbstractEvent;

@Data
@ToString
public class EmployeeAdded extends AbstractEvent {

    private Long id;
    private String userName;
    private String userId;
    private DepartmentId departmentId;

    public EmployeeAdded(Employee aggregate) {
        super(aggregate);
    }

    public EmployeeAdded() {
        super();
    }
}
