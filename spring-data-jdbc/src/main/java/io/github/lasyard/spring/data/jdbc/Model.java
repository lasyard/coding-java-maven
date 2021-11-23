package io.github.lasyard.spring.data.jdbc;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("model")
@Data
public class Model {
    @Column("model_name")
    private final String name;
    @Column("model_id")
    @Id
    private Integer id;
}
