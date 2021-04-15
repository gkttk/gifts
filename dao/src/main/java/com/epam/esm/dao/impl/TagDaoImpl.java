package com.epam.esm.dao.impl;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.factory.CriteriaFactory;
import com.epam.esm.criteria.result.CriteriaFactoryResult;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Default implementation of {@link com.epam.esm.dao.TagDao} interface.
 *
 * @since 1.0
 */
@Repository
public class TagDaoImpl implements TagDao {

    private final JdbcTemplate template;
    private final RowMapper<Tag> rowMapper;
    private final CriteriaFactory<Tag> criteriaFactory;

    @Autowired
    public TagDaoImpl(JdbcTemplate template, RowMapper<Tag> rowMapper, CriteriaFactory<Tag> criteriaFactory) {
        this.template = template;
        this.rowMapper = rowMapper;
        this.criteriaFactory = criteriaFactory;
    }

    /**
     * This method saves Tag entity.
     * The method uses KayHolder for getting generated id for Tag entity from db.
     *
     * @param tag Tag entity without id.
     * @return Tag entity with generated id.
     * @since 1.0
     */
    @Override
    public Tag save(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String name = tag.getName();
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(ApplicationConstants.SAVE_TAG_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            return ps;
        }, keyHolder);

        tag.setId(keyHolder.getKey().longValue());
        return tag;
    }

    /**
     * This method get Tag entity by id.
     *
     * @param id Tag entity's id.
     * @return Optional of Tag entity.If there is no Tag with given id, return Optional.empty().
     * @since 1.0
     */
    @Override
    public Optional<Tag> findById(long id) {
        Tag result = template.queryForStream(ApplicationConstants.GET_BY_ID_TAG_QUERY, rowMapper, id).findFirst().orElse(null);
        return Optional.ofNullable(result);
    }

    /**
     * This method get Tag entity by name.
     *
     * @param tagName Tag entity's name.
     * @return Optional of Tag entity.If there is no Tag with given name, return Optional.empty().
     * @since 1.0
     */
    @Override
    public Optional<Tag> findByName(String tagName) {
        Tag result = template.queryForStream(ApplicationConstants.GET_BY_NAME_TAG_QUERY, rowMapper, tagName).findFirst().orElse(null);
        return Optional.ofNullable(result);
    }

    /**
     * This method combines all getList queries.
     *
     * @param reqParams an instance of {@link CriteriaFactoryResult} which contains {@link Criteria}
     *                  and arrays of params for searching.
     * @return list of Tag entities
     * @since 1.0
     */
    @Override
    public List<Tag> findBy(Map<String, String[]> reqParams) {
        List<CriteriaFactoryResult<Tag>> criteriaWithParams = criteriaFactory.getCriteriaWithParams(reqParams);

        return criteriaWithParams.stream()
                .flatMap(criteriaResult->{
                    Criteria<Tag> criteria = criteriaResult.getCriteria();
                    String[] params = criteriaResult.getParams();
                    return criteria.find(params).stream();
                })
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * This method delete Tag entity.
     *
     * @param id Tag entity id.
     * @return a boolean which shows if in db was changed any row or not
     * @since 1.0
     */
    @Override
    public boolean delete(long id) {
        int updatedRows = template.update(ApplicationConstants.DELETE_TAG_QUERY, id);
        return updatedRows >= 1;
    }


}
