package com.example.vm.repository;

import com.example.vm.model.User;
import com.example.vm.model.enums.VisitStatus;
import com.example.vm.model.VisitAssignment;
import com.example.vm.model.VisitForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitFormRepository extends JpaRepository<VisitForm, Long> {
    List<VisitForm> findByVisitAssignment_User(User user);
    List<VisitForm> findVisitFormByVisitAssignment(VisitAssignment visitAssignment);

    List<VisitForm> findVisitFormByVisitAssignmentAndEnabled(VisitAssignment visitAssignment, Boolean enabled);


    List<VisitForm> findVisitFormByStatus(VisitStatus status);

    List<VisitForm> findVisitFormsByEnabled(Boolean enabled);


//    List<VisitForm>
}
