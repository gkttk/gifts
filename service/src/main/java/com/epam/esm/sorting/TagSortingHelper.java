package com.epam.esm.sorting;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.entity.Tag;
import com.epam.esm.exceptions.GiftCertificateException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link SortingHelper} interface for {@link Tag} entity.
 *
 * @since 1.0
 */
@Component
public class TagSortingHelper implements SortingHelper<Tag> {

    /**
     * This method returns a sorted list of Tag entities.
     *
     * @param fields            fields to sort by.
     * @param order             sorting order. If {@param order} == null or not equals {{@link ApplicationConstants}} DESC_ORDER then sorts by ascending order.
     * @param foundCertificates list of Tag entities for sorting.
     * @return sorted list of Tag entities.
     * @since 1.0
     */
    @Override
    public List<Tag> getSorted(String[] fields, String order, List<Tag> foundCertificates) {
        Comparator<Tag> comparator = buildComparator(fields);

        return ApplicationConstants.DESC_ORDER.equalsIgnoreCase(order) ? sortDesc(comparator, foundCertificates) : sortAsc(comparator, foundCertificates);

    }

    /**
     * This method return a list of Tag entities sorted by ascending order.
     *
     * @param comparator        built comparator for sorting.
     * @param foundCertificates list of Tag entities for sorting.
     * @return sorted list of Tag entities by ascending order.
     * @since 1.0
     */
    private List<Tag> sortAsc(Comparator<Tag> comparator, List<Tag> foundCertificates) {
        return foundCertificates.stream().sorted(comparator).collect(Collectors.toList());
    }

    /**
     * This method return a list of Tag entities sorted by descending order.
     *
     * @param comparator        built comparator for sorting.
     * @param foundCertificates list of Tag entities for sorting.
     * @return sorted list of Tag entities by descending order.
     * @since 1.0
     */
    private List<Tag> sortDesc(Comparator<Tag> comparator, List<Tag> foundCertificates) {
        return foundCertificates.stream().sorted(comparator.reversed()).collect(Collectors.toList());

    }

    /**
     * This method builds comparator according to passed fields for sorting.
     *
     * @param fields fields for sorting.
     * @return built comparator for sorting.
     * @throws GiftCertificateException when the {@param fields} array contains incorrect value of field.
     */
    private Comparator<Tag> buildComparator(String[] fields) {

        List<Comparator<Tag>> comparators = new ArrayList<>();

        Arrays.stream(fields)
                .forEach(field -> {
                    switch (field) {
                        case ApplicationConstants.TAG_ID_FIELD: {
                            comparators.add(Comparator.comparing(Tag::getId));
                            break;
                        }
                        case ApplicationConstants.TAG_NAME_FIELD: {
                            comparators.add(Comparator.comparing(Tag::getName));
                            break;
                        }
                        default:
                            throw new GiftCertificateException(ApplicationConstants.INVALID_SORT_FIELD_ERROR_CODE, String.format("Can't sort Tags by the field: %s", field));
                    }
                });

        return comparators.stream().reduce(Comparator::thenComparing).get();
    }


}