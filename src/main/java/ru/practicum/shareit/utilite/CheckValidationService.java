package ru.practicum.shareit.utilite;

import org.springframework.data.domain.PageRequest;

public interface CheckValidationService {
    PageRequest checkPageSize(Integer from, Integer size);
}

