package com.epam.esm.domain.service.impl;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.relation.CertificateTagsDao;
import com.epam.esm.dao.domain.CriteriaFindAllDao;
import com.epam.esm.dao.domain.TagDao;
import com.epam.esm.dao.domain.UserDao;
import com.epam.esm.domain.dto.TagDto;
import com.epam.esm.domain.dto.bundles.TagDtoBundle;
import com.epam.esm.domain.exceptions.TagException;
import com.epam.esm.domain.exceptions.UserException;
import com.epam.esm.domain.service.TagService;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link com.epam.esm.domain.service.TagService} interface.
 *
 * @since 1.0
 */
@Service
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final UserDao userDao;
    private final ModelMapper modelMapper;
    private final CertificateTagsDao certificateTagsDao;
    private final CriteriaFindAllDao<Tag> findAllDao;

    @Autowired
    public TagServiceImpl(TagDao tagDao, UserDao userDao, ModelMapper modelMapper, CertificateTagsDao certificateTagsDao,
                          @Qualifier("tagCriteriaFindAllDao") CriteriaFindAllDao<Tag> findAllDao) {
        this.tagDao = tagDao;
        this.userDao = userDao;
        this.modelMapper = modelMapper;
        this.certificateTagsDao = certificateTagsDao;
        this.findAllDao = findAllDao;
    }

    /**
     * This method gets a list of TagDto according to request parameters, limit and offset.
     *
     * @param reqParams parameters of a request.
     * @param limit     for pagination.
     * @param offset    for pagination.
     * @return list of TagDto.
     * @since 1.0
     */
    @Override
    public TagDtoBundle findAllForQuery(Map<String, String[]> reqParams, int limit, int offset) {
        List<Tag> foundTags = findAllDao.findBy(reqParams, limit, offset);
        List<TagDto> tagDtos = foundTags.stream()
                .map(entity -> modelMapper.map(entity, TagDto.class))
                .collect(Collectors.toList());
        long count = tagDao.count();


        return new TagDtoBundle(tagDtos, count);
    }


    /**
     * This method gets Tag entity from dao layer with given id and converts it to TagDto.
     *
     * @param id id of necessary entity.
     * @return TagDto.
     * @since 1.0
     */
    @Override
    public TagDto findById(long id) {
        Tag foundTag = findByIdIfExist(id);
        return modelMapper.map(foundTag, TagDto.class);
    }

    /**
     * This method saves a TagDto into db.
     *
     * @param tagDto DTO for saving without id.
     * @return TagDto.
     * @since 1.0
     */
    @Override
    @Transactional
    public TagDto save(TagDto tagDto) {

        checkIfEntityWithGivenNameExist(tagDto.getName());
        Tag entity = modelMapper.map(tagDto, Tag.class);
        Tag savedEntity = tagDao.save(entity);
        tagDto.setId(savedEntity.getId());
        return tagDto;

    }

    /**
     * This method deletes Tag entity with given id from db.
     *
     * @param id id of deletable Tag entity.
     * @throws TagException if Order entity with given id doesn't exist in db.
     * @since 4.0
     */
    @Transactional
    @Override
    public void delete(long id) {
        certificateTagsDao.deleteAllCertificateLinksForTagId(id);
        if (tagDao.existsById(id)) {
            tagDao.deleteById(id);
        } else {
            throw new TagException(String.format("Tag with id: %d is not found in DB", id),
                    ApplicationConstants.TAG_NOT_FOUND_ERROR_CODE, id);
        }
    }

    /**
     * This method gets TagDto with given name.
     *
     * @param tagName name of Tag entity.
     * @return TagDto.
     * @throws TagException is there is no Tag entity with given name in db.
     * @since 1.0
     */
    @Override
    public TagDto findByName(String tagName) {
        Optional<Tag> foundTagOpt = tagDao.findByName(tagName);
        Tag foundTag = foundTagOpt.orElseThrow(() -> new TagException(String.format("Tag with name: %s is not found in DB",
                tagName), ApplicationConstants.TAG_NOT_FOUND_ERROR_CODE, tagName));
        return modelMapper.map(foundTag, TagDto.class);
    }


    /**
     * This method gets the most widely used tags of the user with given id.
     *
     * @param userId User entity's id.
     * @return list of TagDto.
     * @throws UserException is there is no user with given id in DB.
     * @since 2.0
     */
    @Override
    public List<TagDto> findMostWidelyUsed(long userId) {
        Optional<User> userOpt = userDao.findById(userId);
        if (!userOpt.isPresent()) {
            throw new UserException(String.format("Can't find an user with id: %d", userId),
                    ApplicationConstants.USER_NOT_FOUND_BY_ID_ERROR_CODE, userId);
        }

        return tagDao.findMaxWidelyUsed(userId)
                .stream()
                .map(tag -> modelMapper.map(tag, TagDto.class))
                .collect(Collectors.toList());
    }

    /**
     * This method attempts to get an Tag entity from db by it's id.
     *
     * @param tagId id of the Tag entity.
     * @return Tag entity.
     * @throws TagException when there is no entity with given id in db.
     * @since 1.0
     */
    private Tag findByIdIfExist(long tagId) {
        Optional<Tag> foundTagOpt = tagDao.findById(tagId);
        return foundTagOpt.orElseThrow(() ->
                new TagException(String.format("Can't find a tag with id: %d", tagId),
                        ApplicationConstants.TAG_NOT_FOUND_ERROR_CODE, tagId));
    }

    /**
     * This method checks if a Tag entity with given name exists in db.
     *
     * @param tagName name of the Tag entity.
     * @throws TagException if there is Tag entity with given name in db.
     * @since 1.0
     */
    private void checkIfEntityWithGivenNameExist(String tagName) {
        Optional<Tag> foundTagOpt = tagDao.findByName(tagName);
        if (foundTagOpt.isPresent()) {
            throw new TagException(String.format("Tag with name: %s already exist in DB",
                    tagName), ApplicationConstants.TAG_WITH_SUCH_NAME_EXISTS_ERROR_CODE, tagName);
        }
    }
}
