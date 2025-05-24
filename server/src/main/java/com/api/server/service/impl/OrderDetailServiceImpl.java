package com.api.server.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.server.dto.order.OrderDetailDTO;
import com.api.server.dto.structure.StructureDTO;
import com.api.server.exception.ObjectNotFoundException;
import com.api.server.persistence.entity.order.OrderDetail;
import com.api.server.persistence.entity.structure.Structure;
import com.api.server.service.order.OrderDetailService;
import com.api.server.persistence.repository.order.OrderDetailRepository;
import com.api.server.persistence.repository.order.OrderRepository;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Page<OrderDetailDTO> findAll(Long orderId, String status, Pageable pageable) {
        Page<OrderDetail> orderDetails;
        if (orderId != null && status != null) {
            orderDetails = orderDetailRepository.findByOrderIdAndStatus(orderId, OrderDetail.OrderDetailStatus.valueOf(status), pageable);
        } else if (orderId != null) {
            orderDetails = orderDetailRepository.findByOrderId(orderId, pageable);
        } else {
            orderDetails = orderDetailRepository.findAll(pageable);
        }
        return orderDetails.map(this::mapToDTO);
    }

    @Override
    public OrderDetailDTO findOrderDetailById(Long id) {
        return orderDetailRepository.findById(id)
            .map(this::mapToDTO)
            .orElseThrow(() -> new ObjectNotFoundException("Detalle de la orden no encontrado"));
    }

    @Override
    public OrderDetailDTO updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) {
        // 1. Validar el DTO
        if (orderDetailDTO == null) {
            throw new IllegalArgumentException("El detalle de la orden no puede ser nulo");
        }

        // 2. Buscar el detalle de la orden
        OrderDetail orderDetail = orderDetailRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Detalle de la orden no encontrado"));

        // 3. Validar que no esté cancelado
        if (orderDetail.getStatus() == OrderDetail.OrderDetailStatus.CANCELADO) {
            throw new IllegalStateException("No se puede modificar un detalle de orden cancelado");
        }

        // 4. Actualizar el detalle de la orden con validaciones
        if (orderDetailDTO.getQuantity() != null) {
            if (orderDetailDTO.getQuantity() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
            }
            orderDetail.setQuantity(orderDetailDTO.getQuantity());
        }
        
        if (orderDetailDTO.getUnitprice() != null) {
            if (orderDetailDTO.getUnitprice() <= 0) {
                throw new IllegalArgumentException("El precio unitario debe ser mayor a 0");
            }
            orderDetail.setUnitprice(orderDetailDTO.getUnitprice());
        }

        if (orderDetailDTO.getStatus() != null) {
            orderDetail.setStatus(orderDetailDTO.getStatus());
        }

        if (orderDetailDTO.getCancellationreason() != null) {
            if (orderDetailDTO.getStatus() != OrderDetail.OrderDetailStatus.CANCELADO) {
                throw new IllegalArgumentException("La razón de cancelación solo puede establecerse cuando el estado es CANCELADO");
            }
            orderDetail.setCancellationreason(orderDetailDTO.getCancellationreason());
        }

        // 5. Asignar la orden al detalle de la orden
        if (orderDetailDTO.getOrderId() != null) {
            orderDetail.setOrder(orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new ObjectNotFoundException("Orden no encontrada")));
        }

        // 6. Guardar el detalle de la orden
        orderDetail = orderDetailRepository.save(orderDetail);

        // 7. Devolver el DTO del detalle de la orden
        return mapToDTO(orderDetail);
    }

    @Override
    public OrderDetailDTO cancelOrderDetail(Long id) {
        // 1. Buscar el detalle de la orden
        OrderDetail orderDetail = orderDetailRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Detalle de la orden no encontrado"));

        // 2. Validar que no esté ya cancelado
        if (orderDetail.getStatus() == OrderDetail.OrderDetailStatus.CANCELADO) {
            throw new IllegalStateException("El detalle de la orden ya está cancelado");
        }

        // 3. Actualizar el estado del detalle de la orden
        orderDetail.setStatus(OrderDetail.OrderDetailStatus.CANCELADO);

        // 4. Guardar el detalle de la orden
        orderDetail = orderDetailRepository.save(orderDetail);

        // 5. Devolver el DTO del detalle de la orden
        return mapToDTO(orderDetail);
    }

    private OrderDetailDTO mapToDTO(OrderDetail orderDetail) {
        // 1. Convertir la entidad a DTO
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();

        // 2. Asignar los valores del detalle de la orden
        orderDetailDTO.setQuantity(orderDetail.getQuantity());
        orderDetailDTO.setUnitprice(orderDetail.getUnitprice());
        orderDetailDTO.setStatus(orderDetail.getStatus());
        orderDetailDTO.setCancellationreason(orderDetail.getCancellationreason());
        orderDetailDTO.setOrderId(orderDetail.getOrder().getId());
        orderDetailDTO.setStructure(mapToDTO(orderDetail.getStructure()));

        // 3. Devolver el DTO del detalle de la orden
        return orderDetailDTO;
    }

    private StructureDTO mapToDTO(Structure structure) {
        // 1. Convertir la entidad a DTO
        StructureDTO structureDTO = new StructureDTO();

        // 2. Asignar los valores de la estructura
        structureDTO.setId(structure.getId());
        structureDTO.setName(structure.getName());
        structureDTO.setDescription(structure.getDescription());    
        structureDTO.setColors(structure.getColors());
        structureDTO.setMaterials(structure.getMaterials());
        structureDTO.setStartdate(structure.getStartdate());
        structureDTO.setEstimatedenddate(structure.getEstimatedenddate());
        structureDTO.setRealenddate(structure.getRealenddate());
        structureDTO.setObservations(structure.getObservations());

        // 3. Devolver el DTO de la estructura
        return structureDTO;
    }

}
