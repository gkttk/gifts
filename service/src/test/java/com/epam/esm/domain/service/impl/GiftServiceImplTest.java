package com.epam.esm.domain.service.impl;

import com.epam.esm.dao.domain.CriteriaFindAllDao;
import com.epam.esm.dao.domain.GiftCertificateDao;
import com.epam.esm.dao.domain.TagDao;
import com.epam.esm.dao.relation.CertificateTagsDao;
import com.epam.esm.domain.dto.GiftCertificateDto;
import com.epam.esm.domain.dto.TagDto;
import com.epam.esm.domain.dto.bundles.GiftCertificateDtoBundle;
import com.epam.esm.domain.exceptions.GiftCertificateException;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GiftServiceImplTest {

    @Mock
    private CriteriaFindAllDao<GiftCertificate> criteriaFindAllDao;

    @Mock
    private GiftCertificateDao certDao;

    @Mock
    private TagDao tagDao;

    @Mock
    private CertificateTagsDao certificateTagsDao;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private GiftCertificateServiceImpl service;

    private static GiftCertificateDto testDto;
    private static GiftCertificateDto testDtoWithoutId;
    private static GiftCertificate testEntity;
    private static GiftCertificate testEntityWithoutId;
    private static Tag tag;
    private static TagDto tagDto;

    @BeforeAll
    static void init() {
        tag = new Tag();
        tag.setId(100L);
        tag.setName("testTag");
        tagDto = new TagDto();
        tagDto.setId(100L);
        tagDto.setName("testTag");

        testEntity = new GiftCertificate();
        testEntity.setId(100L);
        testEntity.setName("testCertificate");
        testEntity.setDescription("description");
        testEntity.setPrice(new BigDecimal("1.5"));
        testEntity.setDuration(10);
        testEntity.setTags(Collections.singletonList(tag));

        testEntityWithoutId = new GiftCertificate();
        testEntityWithoutId.setName("testName");
        testEntityWithoutId.setDescription("testDescription");
        testEntityWithoutId.setPrice(new BigDecimal("10"));
        testEntityWithoutId.setDuration(25);
        testEntityWithoutId.setTags(Collections.singletonList(tag));

        testDto = new GiftCertificateDto();
        testDto.setId(100L);
        testDto.setName("testCertificate");
        testDto.setDescription("description");
        testDto.setPrice(new BigDecimal("1.5"));
        testDto.setDuration(10);
        testDto.setTags(Arrays.asList(tagDto, tagDto, tagDto));
        testDto.setTags(Collections.singletonList(tagDto));

        testDtoWithoutId = new GiftCertificateDto();
        testDtoWithoutId.setName("testName");
        testDtoWithoutId.setDescription("testDescription");
        testDtoWithoutId.setPrice(new BigDecimal("10"));
        testDtoWithoutId.setDuration(25);
        testDtoWithoutId.setTags(Collections.singletonList(tagDto));
    }

    @Test
    public void testFindById_DtoWithGivenId_EntityWithGivenIdExistsInDb() {
        //given
        long testId = testEntity.getId();
        when(certDao.findById(testId)).thenReturn(Optional.of(testEntity));
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        //when
        GiftCertificateDto result = service.findById(testId);
        //then
        assertEquals(result, testDto);
        verify(certDao).findById(testId);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
    }

    @Test
    public void testFindById_ThrowException_EntityWithGivenIdDoesNotExistInDb() {
        //given
        long testId = -1;
        when(certDao.findById(testId)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(GiftCertificateException.class, () -> service.findById(testId));
        verify(certDao).findById(testId);
    }

    @Test
    public void testSave_DtoWithId_EntityWithGivenNameDoesNotExistInDb() {
        //given
        when(certDao.findByName(testDtoWithoutId.getName())).thenReturn(Optional.empty());
        when(modelMapper.map(testDtoWithoutId, GiftCertificate.class)).thenReturn(testEntityWithoutId);
        when(modelMapper.map(tagDto, Tag.class)).thenReturn(tag);
        when(tagDao.findByName(tag.getName())).thenReturn(Optional.empty());
        when(certDao.save(testEntityWithoutId)).thenReturn(testEntity);
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        //when
        GiftCertificateDto result = service.save(testDtoWithoutId);
        //then
        assertNotNull(result.getId());
        verify(certDao).findByName(testDtoWithoutId.getName());
        verify(modelMapper).map(testDtoWithoutId, GiftCertificate.class);
        verify(modelMapper).map(tagDto, Tag.class);
        verify(certDao).save(testEntityWithoutId);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
    }

    @Test
    public void testUpdate_UpdatedDto_EntityWithGivenNameDoesNotExistInDb() {
        //given
        long certificateId = testDto.getId();
        when(certDao.findById(certificateId)).thenReturn(Optional.of(testEntity));

        when(certDao.findByName(testDto.getName())).thenReturn(Optional.empty());
        doNothing().when(certificateTagsDao).deleteAllTagLinksForCertificateId(certificateId);

        when(modelMapper.map(tagDto, Tag.class)).thenReturn(tag);
        when(tagDao.findByName(tag.getName())).thenReturn(Optional.empty());

        when(modelMapper.map(testDto, GiftCertificate.class)).thenReturn(testEntity);

        when(certDao.save(testEntity)).thenReturn(testEntity);
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        //when
        GiftCertificateDto result = service.update(testDto, certificateId);
        //then
        assertEquals(result, testDto);
        verify(certDao).findById(certificateId);
        verify(certDao).findByName(testDto.getName());
        verify(certificateTagsDao).deleteAllTagLinksForCertificateId(certificateId);
        verify(modelMapper).map(tagDto, Tag.class);
        verify(tagDao).findByName(tag.getName());
        verify(modelMapper).map(testDto, GiftCertificate.class);
        verify(certDao).save(testEntity);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
    }

    @Test
    public void testPatch_PatchedDto_EntityWithGivenNameDoesNotExistInDb() {
        //given
        long certificateId = testDto.getId();
        when(certDao.findById(certificateId)).thenReturn(Optional.of(testEntity));
        when(certDao.findByName(testDto.getName())).thenReturn(Optional.empty());
        when(modelMapper.map(tagDto, Tag.class)).thenReturn(tag);
        when(tagDao.findByName(tag.getName())).thenReturn(Optional.empty());
        when(certDao.save(testEntity)).thenReturn(testEntity);
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        //when
        GiftCertificateDto result = service.patch(testDto, certificateId);
        //then
        assertEquals(result, testDto);
        verify(certDao).findById(certificateId);
        verify(certDao).findByName(testDto.getName());
        verify(modelMapper).map(tagDto, Tag.class);
        verify(tagDao).findByName(tag.getName());
        verify(certDao).save(testEntity);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
    }

    @Test
    public void testDelete_ShouldDeleteEntity_EntityWithGivenIdDoesExistInDb() {
        //given
        long certificateId = testDto.getId();
        doNothing().when(certificateTagsDao).deleteAllTagLinksForCertificateId(certificateId);
        when(certDao.existsById(certificateId)).thenReturn(true);
        doNothing().when(certDao).deleteById(certificateId);
        //when
        service.delete(certificateId);
        //then
        verify(certificateTagsDao).deleteAllTagLinksForCertificateId(certificateId);
        verify(certDao).existsById(certificateId);
        verify(certDao).deleteById(certificateId);
    }

    @Test
    public void testDelete_ShouldThrowException_EntityWithGivenIdDoesNotExistInDb() {
        //given
        long certificateId = -1;
        doNothing().when(certificateTagsDao).deleteAllTagLinksForCertificateId(certificateId);
        when(certDao.existsById(certificateId)).thenReturn(false);
        //when
        //then
        assertThrows(GiftCertificateException.class, () -> service.delete(certificateId));
        verify(certificateTagsDao).deleteAllTagLinksForCertificateId(certificateId);
        verify(certDao).existsById(certificateId);
    }

    @Test
    public void testFindAllForQuery_BundleOfDto_ThereAreEntitiesInDb() {
        //given
        int limit = 5;
        int offset = 0;
        Map<String, String[]> reqParams = Collections.emptyMap();
        when(criteriaFindAllDao.findBy(reqParams, limit, offset)).thenReturn(Arrays.asList(testEntity, testEntity));
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        long expectedSize = 2;
        when(certDao.count()).thenReturn(expectedSize);
        GiftCertificateDtoBundle expectedBundle = new GiftCertificateDtoBundle(Arrays.asList(testDto, testDto), expectedSize);
        //when
        GiftCertificateDtoBundle result = service.findAllForQuery(reqParams, limit, offset);
        //then
        assertEquals(result, expectedBundle);
        verify(criteriaFindAllDao).findBy(reqParams, limit, offset);
        verify(modelMapper, times(2)).map(testEntity, GiftCertificateDto.class);
        verify(certDao).count();
    }

    @Test
    public void testFindAllForQuery_BundleWithoutDto_ThereAreNoEntitiesInDb() {
        //given
        int limit = 5;
        int offset = 0;
        Map<String, String[]> reqParams = Collections.emptyMap();
        when(criteriaFindAllDao.findBy(reqParams, limit, offset)).thenReturn(Collections.emptyList());
        long expectedSize = 0L;
        when(certDao.count()).thenReturn(expectedSize);
        GiftCertificateDtoBundle expectedBundle = new GiftCertificateDtoBundle(Collections.emptyList(), expectedSize);
        //when
        GiftCertificateDtoBundle result = service.findAllForQuery(reqParams, limit, offset);
        //then
        assertEquals(result, expectedBundle);
        verify(criteriaFindAllDao).findBy(reqParams, limit, offset);
        verify(certDao).count();
    }
}

