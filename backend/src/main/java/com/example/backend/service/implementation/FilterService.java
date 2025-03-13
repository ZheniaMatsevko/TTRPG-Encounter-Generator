package com.example.backend.service.implementation;

import com.example.backend.repository.IFilterRepository;
import com.example.backend.service.IFilterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilterService implements IFilterService {
    private final IFilterRepository filterRepository;
}
