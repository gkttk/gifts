package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.exceptions.TagWithSuchNameAlreadyExists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {

    @Mock
    private TagDao tagDaoMock;
    @Mock
    private ModelMapper modelMapperMock;

    @InjectMocks
    private TagServiceImpl tagService;

    private TagDto testDto;
    private Tag testEntity;

    @BeforeEach
    void init() {
        testDto = new TagDto(100L, "test");
        testEntity = new Tag(100L, "test");
    }

    @Test
    public void testFindByIdShouldReturnDtoWhenEntityWithGivenIdIsPresentInDb() {
        //given
        when(tagDaoMock.getById(anyLong())).thenReturn(Optional.of(new Tag()));
        when(modelMapperMock.map(any(), any())).thenReturn(testDto);
        //when
        TagDto result = tagService.findById(100);
        //then
        verify(tagDaoMock).getById(anyLong());
        verify(modelMapperMock).map(any(), any());
        assertEquals(result, testDto);
    }

    @Test
    public void testFindByIdShouldThrowExceptionWhenEntityWithGivenIdIsNotPresentInDb() {
        //given
        long tagId = testDto.getId();
        when(tagDaoMock.getById(tagId)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(TagNotFoundException.class, () -> tagService.findById(tagId));
        verify(tagDaoMock).getById(tagId);
    }

    @Test
    public void testFindAllShouldReturnListOfDtoWhenEntitiesArePresentInDb() {
        //given
        List<Tag> expectedEntitiesList = Stream.generate(Tag::new).limit(3).collect(Collectors.toList());
        List<TagDto> expectedResult = Arrays.asList(testDto, testDto, testDto);
        when(tagDaoMock.getAll()).thenReturn(expectedEntitiesList);
        when(modelMapperMock.map(any(), any())).thenReturn(testDto);
        //when
        List<TagDto> result = tagService.findAll();
        //then
        verify(tagDaoMock).getAll();
        verify(modelMapperMock, times(expectedEntitiesList.size())).map(any(), any());
        assertEquals(result, expectedResult);
    }

    @Test
    public void testFindAllShouldThrowExceptionWhenThereIsNoEntitiesInDb() {
        //given
        List<Tag> expectedEntitiesList = Collections.emptyList();
        when(tagDaoMock.getAll()).thenReturn(expectedEntitiesList);
        //when
        //then
        assertThrows(TagNotFoundException.class, () -> tagService.findAll());
        verify(tagDaoMock).getAll();
    }

    @Test
    public void testSaveShouldSaveEntityAndReturnDtoWhenGivenEntityIsNotPresentInDb() {
        //given
        String tagName = testDto.getName();
        when(tagDaoMock.getByName(tagName)).thenReturn(Optional.empty());
        when(modelMapperMock.map(testDto, Tag.class)).thenReturn(testEntity);
        when(tagDaoMock.save(testEntity)).thenReturn(testEntity);
        //when
        TagDto result = tagService.save(testDto);
        //then
        verify(tagDaoMock).getByName(tagName);
        verify(modelMapperMock).map(testDto, Tag.class);
        verify(tagDaoMock).save(testEntity);
        assertEquals(result, testDto);
    }

    @Test
    public void testSaveShouldThrowExceptionWhenGivenEntityIsPresentInDb() {
        //given
        String tagName = testDto.getName();
        when(tagDaoMock.getByName(tagName)).thenReturn(Optional.of(testEntity));
        //when
        //then
        assertThrows(TagWithSuchNameAlreadyExists.class, () -> tagService.save(testDto));
        verify(tagDaoMock).getByName(tagName);
    }

    @Test
    public void testDeleteShouldDeleteEntityIfEntityWithSuchIdIsPresentInDb() {
        //given
        long tagId = testDto.getId();
        when(tagDaoMock.delete(tagId)).thenReturn(true);
        //when
        tagService.delete(tagId);
        //then
        verify(tagDaoMock).delete(tagId);
    }

    @Test
    public void testDeleteShouldThrowExceptionIfEntityWithSuchIdIsNotPresentInDb() {
        //given
        long tagId = testDto.getId();
        when(tagDaoMock.delete(tagId)).thenReturn(false);
        //when
        //then
        assertThrows(TagNotFoundException.class, () -> tagService.delete(tagId));
        verify(tagDaoMock).delete(tagId);
    }

    @Test
    public void testFindByNameShouldReturnDtoWhenEntityWithSuchNameIsPresentInDb() {
        //given
        String name = testDto.getName();
        when(tagDaoMock.getByName(name)).thenReturn(Optional.of(testEntity));
        when(modelMapperMock.map(testEntity, TagDto.class)).thenReturn(testDto);
        //when
        TagDto result = tagService.findByName(name);
        //then
        verify(tagDaoMock).getByName(name);
        verify(modelMapperMock).map(testEntity, TagDto.class);
        assertEquals(result, testDto);
    }

    @Test
    public void testFindByNameShouldThrowExceptionWhenEntityWithSuchNameIsNotPresentInDb() {
        //given
        String name = testDto.getName();
        when(tagDaoMock.getByName(name)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(TagNotFoundException.class, () -> tagService.findByName(name));
        verify(tagDaoMock).getByName(name);
    }


}