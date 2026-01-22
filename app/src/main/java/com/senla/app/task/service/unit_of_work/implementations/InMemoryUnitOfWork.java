package com.senla.app.task.service.unit_of_work.implementations;

import com.senla.app.task.service.unit_of_work.AbstractUnitOfWork;

public class InMemoryUnitOfWork extends AbstractUnitOfWork {

    public InMemoryUnitOfWork() { }

    @Override
    protected void doBegin() { }

    @Override
    protected void doCommit() { }

    @Override
    protected void doRollback() { }
}
