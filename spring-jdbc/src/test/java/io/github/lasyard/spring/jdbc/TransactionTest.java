package io.github.lasyard.spring.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class TransactionTest {
    private static ModelDao dao = null;
    private static PlatformTransactionManager txManager = null;

    private boolean updatedInTx2;
    private boolean readInTx1;

    @BeforeAll
    public static void setupAll() {
        dao = DaoFactory.getModelDao();
        txManager = DaoFactory.getTxManager();
    }

    @BeforeEach
    public void setup() {
        updatedInTx2 = false;
        readInTx1 = false;
    }

    private @NonNull List<String> runTest(int isolationLevel) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("TxTest");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        def.setIsolationLevel(isolationLevel);

        Model model = new Model();
        model.setName("One");
        final int id = dao.insert(model);

        TransactionStatus tx1 = txManager.getTransaction(def);
        log.info("Transaction 1 started.");
        model = dao.get(id);
        List<String> results = new ArrayList<>();
        assert model != null;
        results.add(model.getName());
        log.info("First read result: " + model);

        Thread th = new Thread(() -> {
            TransactionStatus tx2 = txManager.getTransaction(def);
            log.info("Transaction 2 started.");
            try {
                synchronized (this) {
                    Model model1 = dao.get(id);
                    assert model1 != null;
                    model1.setName("Two");
                    dao.update(model1);
                    log.info("Records updated in transaction 2.");
                    updatedInTx2 = true;
                    notify();
                    while (!readInTx1) {
                        wait();
                    }
                    txManager.commit(tx2);
                    log.info("Transaction 2 committed.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        th.start();

        try {
            synchronized (this) {
                while (!updatedInTx2) {
                    wait();
                }
                model = dao.get(id);
                assert model != null;
                results.add(model.getName());
                log.info("Read uncommitted result: " + model);
                readInTx1 = true;
                notify();
            }
            th.join();
            model = dao.get(id);
            assert model != null;
            results.add(model.getName());
            log.info("Read committed result:" + model);
            txManager.commit(tx1);
            log.info("Transaction 1 committed.");
            model = dao.get(id);
            assert model != null;
            results.add(model.getName());
            log.info("Read final result:" + model);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return results;
    }

    @Test
    public void testDirtyReads() {
        List<String> results = runTest(TransactionDefinition.ISOLATION_READ_UNCOMMITTED);
        assertThat(results.size()).isEqualTo(4);
        assertThat(results.get(0)).isEqualTo("One");
        assertThat(results.get(1)).isEqualTo("Two");
        assertThat(results.get(2)).isEqualTo("Two");
        assertThat(results.get(3)).isEqualTo("Two");
    }

    @Test
    public void testNonRepeatableReads() {
        List<String> results = runTest(TransactionDefinition.ISOLATION_READ_COMMITTED);
        assertThat(results.size()).isEqualTo(4);
        assertThat(results.get(0)).isEqualTo("One");
        assertThat(results.get(1)).isEqualTo("One");
        assertThat(results.get(2)).isEqualTo("Two");
        assertThat(results.get(3)).isEqualTo("Two");
    }

    @Test
    public void testRepeatableReads() {
        List<String> results = runTest(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        assertThat(results.size()).isEqualTo(4);
        assertThat(results.get(0)).isEqualTo("One");
        assertThat(results.get(1)).isEqualTo("One");
        assertThat(results.get(2)).isEqualTo("One");
        assertThat(results.get(3)).isEqualTo("Two");
    }
}
