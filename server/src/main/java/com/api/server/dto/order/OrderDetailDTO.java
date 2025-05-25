package com.api.server.dto.order;

import com.api.server.dto.structure.StructureDTO;
import com.api.server.persistence.entity.order.OrderDetail.OrderDetailStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailDTO {

    private Long id;

    @NotNull
    @Min(value = 1)
    private Integer quantity;

    @NotNull
    @Min(value = 0)
    private Double unitprice;

    @NotNull
    private OrderDetailStatus status;

    private String cancellationreason;

    @NotNull
    private Long orderId;

    @NotNull
    private StructureDTO structure;
}
