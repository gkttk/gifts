package com.epam.esm.dao.impl;

import com.epam.esm.criteria.result.CriteriaFactoryResult;
import com.epam.esm.criteria.tags.AllTagCriteria;
import com.epam.esm.criteria.tags.CertificateIdTagCriteria;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.config.DaoTestConfig;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DaoTestConfig.class})
@ActiveProfiles("test")
public class TagDaoTest {

    @Autowired
    private TagDao tagDao;

    @Autowired
    private CertificateIdTagCriteria certificateIdTagCriteria;

    @Autowired
    private AllTagCriteria allTagCriteria;

    private static Tag tag1;
    private static Tag tag2;
    private static Tag tag3;


    @BeforeAll
    static void init() {
        tag1 = new Tag(1L, "tag1");
        tag2 = new Tag(2L, "tag2");
        tag3 = new Tag(3L, "tag3");
    }

    @Test
    public void testGetById_EntityWithGivenIdIsPresentInDb_ReturnOptionalWithEntity() {
        //given
        Optional<Tag> expected = Optional.of(tag1);
        Long tagId = expected.get().getId();
        //when
        Optional<Tag> result = tagDao.findById(tagId);
        //then
        assertEquals(result, expected);
    }

    @Test
    public void testGetById_EntityWithGivenIdIsNotPresentInDb_ReturnEmptyOptional() {
        //given
        long tagId = 100L;
        Optional<Tag> expected = Optional.empty();
        //when
        Optional<Tag> result = tagDao.findById(tagId);
        //then
        assertEquals(result, expected);
    }

    @Test
    public void testGetBy_TagsWithGivenCertificateIdIsPresentInDb_ReturnListTags() {
        //given
        String[] params = new String[]{"3"};
        CriteriaFactoryResult<Tag> factoryResult = new CriteriaFactoryResult<>(certificateIdTagCriteria, params);

        List<Tag> expected = Collections.singletonList(tag1);
        //when
        List<Tag> result = tagDao.findBy(factoryResult);
        //then
        assertEquals(result, expected);
    }

    @Test
    public void testGetBy_TagsWithGivenCertificateIdIsPresentInDb_ReturnTagsList() {
        //given
        String[] params = new String[]{"100"};
        CriteriaFactoryResult<Tag> factoryResult = new CriteriaFactoryResult<>(certificateIdTagCriteria, params);
        List<Tag> expected = Collections.emptyList();
        //when
        List<Tag> result = tagDao.findBy(factoryResult);
        //then
        assertEquals(result, expected);
    }


    @Test
    public void testGetBy_ThereAreEntitiesInDb_ReturnAllTagsList() {
        //given
        CriteriaFactoryResult<Tag> factoryResult = new CriteriaFactoryResult<>(allTagCriteria, null);
        List<Tag> expected = Arrays.asList(tag1, tag2, tag3);
        //when
        List<Tag> result = tagDao.findBy(factoryResult);
        //then
        assertEquals(result, expected);
    }

    @Test
    @Transactional
    @Rollback
    public void testSave_ReturnSavedEntityWithNewId() {
        //given
        Tag savedEntity = new Tag(null, "newTag");
        //when
        Tag result = tagDao.save(savedEntity);
        //then
        assertNotNull(result.getId());
        assertEquals(result.getName(), savedEntity.getName());
    }

    @Test
    @Transactional
    @Rollback
    public void testDelete_EntityWithGivenIdIsPresentInDb_ReturnTrue() {
        //given
        Long tagId = tag1.getId();
        //when
        boolean result = tagDao.delete(tagId);
        //then
        assertTrue(result);
    }

    @Test
    @Transactional
    @Rollback
    public void testDelete_EntityWithGivenIdIsNotPresentInDb_ReturnFalse() {
        //given
        long tagId = 100L;
        //when
        boolean result = tagDao.delete(tagId);
        //then
        assertFalse(result);
    }

    @Test
    public void testGetByName_EntityWithGivenNameIsPresentInDb_ReturnOptionalEntity() {
        //given
        String tagName = tag1.getName();
        Optional<Tag> expected = Optional.of(TagDaoTest.tag1);
        //when
        Optional<Tag> result = tagDao.findByName(tagName);
        //then
        assertEquals(result, expected);
    }

    @Test
    public void testGetByName_EntityWithGivenNameIsNotPresentInDb_ReturnEmptyOptional() {
        //given
        String tagName = "incorrectName";
        Optional<Tag> expected = Optional.empty();
        //when
        Optional<Tag> result = tagDao.findByName(tagName);
        //then
        assertEquals(result, expected);
    }


}
