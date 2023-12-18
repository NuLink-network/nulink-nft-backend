package com.project.nulinknft.service;

import com.project.nulinknft.entity.ContractOffset;
import com.project.nulinknft.repository.ContractOffsetRepository;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ContractOffsetService {

    private final ContractOffsetRepository contractOffsetRepository;

    public ContractOffsetService(ContractOffsetRepository contractOffsetRepository) {
        this.contractOffsetRepository = contractOffsetRepository;
    }

    public ContractOffset findByContractAddress(String contractAddress){
        return contractOffsetRepository.findByContractAddress(contractAddress);
    }

    @Transactional
    public void update(ContractOffset contractOffset){
        contractOffsetRepository.save(contractOffset);
    }
}
