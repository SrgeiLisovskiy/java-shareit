package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequesterIdOrderByCreatedAsc(Long requesterId);

    Page<ItemRequest> findByRequesterIdNotOrderByCreatedAsc(Long requesterId, PageRequest pageRequest);
}
