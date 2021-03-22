package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.List;

public interface TagDao {

    Tag getById(long id);

    List<Tag> findAll();

    void save(Tag tag);

    void delete(long id);

}