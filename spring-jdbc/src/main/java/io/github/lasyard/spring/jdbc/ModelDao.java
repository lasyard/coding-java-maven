package io.github.lasyard.spring.jdbc;

import java.util.Collection;

public interface ModelDao {
    int insert(Model model);

    int update(Model model);

    Model get(int id);

    Collection<Model> getAll();

    void clear();
}
