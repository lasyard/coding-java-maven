package io.github.lasyard.spring.jdbc;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Nonnull;

final class DaoFactory {
    private static final ClassPathXmlApplicationContext ctx;

    static {
        ctx = new ClassPathXmlApplicationContext();
        ctx.setConfigLocation("/application.xml");
        ctx.refresh();
    }

    private DaoFactory() {
    }

    @Nonnull
    static ModelDao getModelDao() {
        return (ModelDao) ctx.getBean("modelDao");
    }

    @Nonnull
    static PlatformTransactionManager getTxManager() {
        return (PlatformTransactionManager) ctx.getBean("txManager");
    }
}
