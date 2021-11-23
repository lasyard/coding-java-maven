package io.github.lasyard.spring.jdbc;

import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ModelDao {
    int insert(@Nonnull Model model);

    int update(@Nonnull Model model);

    @Nullable
    Model get(int id);

    Collection<Model> getAll();

    void clear();
}
