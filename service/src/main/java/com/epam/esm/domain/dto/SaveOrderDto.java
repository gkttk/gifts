package com.epam.esm.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * This DTO is for making an order.
 *
 * @since 2.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveOrderDto {

    @NotNull(message = "save_order_dto_certificate_id_violation_message")
    private Long certificateId;

    @NotNull(message = "save_order_dto_count_violation_message")
    @Min(value = 1, message = "save_order_dto_count_violation_message")
    private Integer count;
}
