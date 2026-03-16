package com.senla.app.service.unit_of_work.implementations;

import com.senla.annotation.db_qualifiers.Jdbc;
import com.senla.app.db.DbConnection;
import com.senla.app.service.unit_of_work.AbstractUnitOfWork;
import com.senla.app.service.unit_of_work.TransactionException;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
@Jdbc
public class JdbcUnitOfWork extends AbstractUnitOfWork {

    private final Connection connection = DbConnection.getInstance().initOrGetConnection();

    public JdbcUnitOfWork() { }

    @Override
    protected void doBegin() {
        try {
            connection.setAutoCommit(false);
        } catch (Exception e) {
            throw new TransactionException(e.getMessage());
        }
    }

    @Override
    protected void doCommit() {
        try {
            connection.commit();
            connection.setAutoCommit(true);
        } catch (Exception e) {
            throw new TransactionException(e.getMessage());
        }
    }

    @Override
    protected void doRollback() {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
        } catch (Exception e) {
            throw new TransactionException(e.getMessage());
        }
    }
}
