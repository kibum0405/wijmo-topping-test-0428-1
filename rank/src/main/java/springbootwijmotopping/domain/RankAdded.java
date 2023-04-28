package springbootwijmotopping.domain;

import java.util.*;
import lombok.*;
import springbootwijmotopping.domain.*;
import springbootwijmotopping.infra.AbstractEvent;

@Data
@ToString
public class RankAdded extends AbstractEvent {

    private Long id;
    private String rank;

    public RankAdded(Rank aggregate) {
        super(aggregate);
    }

    public RankAdded() {
        super();
    }
}
