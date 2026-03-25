package com.senla.app.service.unit_of_work.implementations;

import com.senla.annotation.repo_qualifiers.InMemory;
import com.senla.app.service.unit_of_work.AbstractUnitOfWork;
import org.springframework.stereotype.Component;

@Component
@InMemory
public class InMemoryUnitOfWork extends AbstractUnitOfWork {

    public InMemoryUnitOfWork() { }

    @Override
    protected void doBegin() { }

    @Override
    protected void doCommit() { }

    @Override
    protected void doRollback() { }
}
