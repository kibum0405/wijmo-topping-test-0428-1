package springbootwijmotopping.domain;

import java.util.*;
import lombok.*;
import springbootwijmotopping.domain.*;
import springbootwijmotopping.infra.AbstractEvent;

@Data
@ToString
public class DepartmentAdded extends AbstractEvent {

    private Long id;
    private String department;

    public DepartmentAdded(Department aggregate) {
        super(aggregate);
    }

    public DepartmentAdded() {
        super();
    }
}
