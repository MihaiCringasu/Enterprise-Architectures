package com.example.vros.fleet;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DepotService {
    private final DepotRepository depotRepository;

    public DepotService(DepotRepository depotRepository) {
        this.depotRepository = depotRepository;
    }

    public Depot save(Depot depot) { return depotRepository.save(depot); }
    public List<Depot> findAll() { return depotRepository.findAll(); }
}
