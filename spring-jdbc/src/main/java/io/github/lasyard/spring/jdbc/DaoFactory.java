package io.github.lasyard.spring.jdbc;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;

final class DaoFactory {
    private static final ClassPathXmlApplicationContext ctx;

    static {
        ctx = new ClassPathXmlApplicationContext();
        ctx.setConfigLocation("/application.xml");
        ctx.refresh();
    }

    private DaoFactory() {
    }

    static @NonNull ModelDao getModelDao() {
        return (ModelDao) ctx.getBean("modelDao");
    }

    static @NonNull PlatformTransactionManager getTxManager() {
        return (PlatformTransactionManager) ctx.getBean("txManager");
    }
}
