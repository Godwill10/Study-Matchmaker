package com.studymatchmaker.repository;

import com.studymatchmaker.model.ConnectionRequest;
import com.studymatchmaker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest, Long> {

    List<ConnectionRequest> findByReceiverAndStatus(User receiver, ConnectionRequest.Status status);

    List<ConnectionRequest> findBySenderAndStatus(User sender, ConnectionRequest.Status status);

    Optional<ConnectionRequest> findBySenderAndReceiver(User sender, User receiver);

    boolean existsBySenderAndReceiverAndStatus(User sender, User receiver, ConnectionRequest.Status status);
}
