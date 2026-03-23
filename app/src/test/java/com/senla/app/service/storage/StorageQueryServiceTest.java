package com.senla.app.service.storage;

import com.senla.app.repository.StorageRepository;
import com.senla.app.service.unit_of_work.UnitOfWork;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class StorageQueryServiceTest {

    @Mock
    private StorageRepository repository;

    @Mock
    private UnitOfWork unitOfWork;

    private StorageQueryService service;

    @BeforeEach
    public void init() {
        service = new StorageQueryService(repository, 12, unitOfWork);
    }

    @Test
    public void shouldReturnBooks() {

    }
}
