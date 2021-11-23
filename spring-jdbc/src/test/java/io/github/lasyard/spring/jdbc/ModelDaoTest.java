package io.github.lasyard.spring.jdbc;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ModelDaoTest {
    private static ModelDao dao;

    @BeforeAll
    public static void setupAll() {
        dao = DaoFactory.getModelDao();
    }

    @Test
    public void testInsertGet() {
        Model model1 = new Model();
        model1.setName("first");
        model1.setId(dao.insert(model1));

        Model model2 = new Model();
        model2.setName("second");
        model2.setId(dao.insert(model2));

        Model model;
        model = dao.get(model1.getId());
        assertEquals(model1, model);
        model = dao.get(model2.getId());
        assertEquals(model2, model);
        model = dao.get(10000);
        assertNull(model);

        assertThat(dao.getAll()).contains(model1, model2);
    }

    @Test
    public void testUpdate() {
        Model model = new Model();
        model.setId(1);
        model.setName("first-updated");
        int rows = dao.update(model);
        assertEquals(1, rows);
        model = dao.get(1);
        assertNotNull(model);
        assertEquals("first-updated", model.getName());
    }
}
