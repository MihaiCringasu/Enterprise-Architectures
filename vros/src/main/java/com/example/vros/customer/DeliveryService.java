package com.example.vros.customer;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public Delivery save(Delivery delivery) { return deliveryRepository.save(delivery); }
    public List<Delivery> findByCustomer(Long customerId) { return deliveryRepository.findByCustomerId(customerId); }
    public Double getTotalQuantityForCustomer(Long customerId) {
        return deliveryRepository.getTotalQuantityByCustomer(customerId);
    }
}
