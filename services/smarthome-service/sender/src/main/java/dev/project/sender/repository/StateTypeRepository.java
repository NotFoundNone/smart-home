package dev.project.sender.repository;

import dev.project.sender.entity.StateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateTypeRepository extends JpaRepository<StateType, Long> {

}
