package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;

import java.util.List;
import java.util.Map;

/**
 * This interface represents an api to interact with the GiftCertificate dao layer.
 *
 * Implementations : {@link com.epam.esm.service.impl.GiftCertificateServiceImpl} classes.
 *
 * @since 1.0
 */
public interface GiftCertificateService {

    GiftCertificateDto findById(long id);

    GiftCertificateDto save(GiftCertificateDto certificate);



    List<GiftCertificateDto> findAllForQuery(Map<String, String[]> reqParams, int limit, int offset);

    GiftCertificateDto update(GiftCertificateDto certificate, long id);

    GiftCertificateDto patch(GiftCertificateDto giftCertificatePatchDto, long id);

    void delete(long id);


}
