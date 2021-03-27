package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateDao {


    List<GiftCertificate> getAllByPartOfDescription(String partOfDescription);

    List<GiftCertificate> getAllByPartOfName(String partOfName);

    List<GiftCertificate> getAllSorted(List<String> sortingFieldNames, String sortingOrder);

    List<GiftCertificate> findAllByTagName(String tagName);

    Optional<GiftCertificate> getById(long id);

    List<GiftCertificate> findAll();

    GiftCertificate save(GiftCertificate certificate);

    void update(GiftCertificate certificate, long id);

    boolean delete(long id);

}
