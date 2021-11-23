package io.github.lasyard.mybatis;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ModelMapper {
    Integer insert(Model model);

    List<Model> findAll();

    Model findById(int id);
}
