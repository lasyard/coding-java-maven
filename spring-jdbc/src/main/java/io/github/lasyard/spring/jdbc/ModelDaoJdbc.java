package io.github.lasyard.spring.jdbc;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.Nullable;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;

class ModelDaoJdbc extends JdbcDaoSupport implements ModelDao {
    private static final String TABLE_NAME = "tbl_model";

    private final RowMapper<Model> rowMapper = (resultSet, row) -> {
        Model model = new Model();
        model.setId(resultSet.getInt("id"));
        model.setName(resultSet.getString("name"));
        return model;
    };

    @Override
    public int insert(@Nonnull Model model) {
        String sql = "insert into " + TABLE_NAME + "(name) values(?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Objects.requireNonNull(getJdbcTemplate()).update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, model.getName());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public int update(@Nonnull Model model) {
        String sql = "update " + TABLE_NAME + " set name = ? where id = ?";
        return Objects.requireNonNull(getJdbcTemplate()).update(sql, model.getName(), model.getId());
    }

    @Override
    @Nullable
    public Model get(int id) {
        String sql = "select id, name from " + TABLE_NAME + " where id = ?";
        List<Model> models = Objects.requireNonNull(getJdbcTemplate()).query(sql, rowMapper, id);
        if (models.size() == 0) {
            return null;
        }
        return models.get(0);
    }

    @Override
    public Collection<Model> getAll() {
        String sql = "select id, name from " + TABLE_NAME;
        return Objects.requireNonNull(getJdbcTemplate()).query(sql, rowMapper);
    }

    @Override
    public void clear() {
        String sql = "truncate table " + TABLE_NAME;
        Objects.requireNonNull(getJdbcTemplate()).execute(sql);
    }
}
