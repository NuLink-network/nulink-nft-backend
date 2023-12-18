package com.project.nulinknft.service;

import com.project.nulinknft.repository.ReferrerRepository;
import org.springframework.stereotype.Service;

@Service
public class ReferrerService {

  private final ReferrerRepository referrerRepository;

    public ReferrerService(ReferrerRepository referrerRepository) {
        this.referrerRepository = referrerRepository;
    }


}
