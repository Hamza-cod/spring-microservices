package com.enterprise.order_service.service;

import com.enterprise.order_service.dto.InventoryResponse;
import com.enterprise.order_service.dto.OrderLineItemsDto;
import com.enterprise.order_service.dto.OrderRequest;
import com.enterprise.order_service.model.Order;
import com.enterprise.order_service.model.OrderLineItems;
import com.enterprise.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient webClient;
    public void  placeOrder(OrderRequest orderRequest){
        var order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToEntity)
                .toList();
        order.setOrderLineItemsList(orderLineItems);
        List<String> skuCodes = orderLineItems.stream()
                .map(OrderLineItems::getSkuCode)
                .toList();
        // call inventory service to check if items in stock

            InventoryResponse[] inventoryResponseArray = webClient.get()
                    .uri("http://localhost:8082/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

        assert inventoryResponseArray != null;
        boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                .allMatch(InventoryResponse::isInStock);
        if(!allProductsInStock || inventoryResponseArray.length <1){
            throw new IllegalArgumentException("product not in stock try later");
        }

        orderRepository.save(order);
    }

    private OrderLineItems mapToEntity(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
