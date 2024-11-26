package dev.project.sender.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "state_type", schema = "dev")
public class StateType {

    @Id
    @Column(name = "state_type_id")
    private Long stateTypeId;

    @Column(name = "state_type_name")
    private String stateName;

    public StateType() {
    }

    public Long getStateTypeId() {
        return stateTypeId;
    }

    public void setStateTypeId(Long stateTypeId) {
        this.stateTypeId = stateTypeId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
