package com.growcast.growcast.grownCrops.service;

import com.growcast.growcast.grownCrops.dto.GrownCropsDropdownDTO;
import com.growcast.growcast.grownCrops.repository.GrownCropsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GrownCropsService {
    private final GrownCropsRepository grownCropsRepository;

    //드롭다운용 작물 이름 가져옴
    public List<GrownCropsDropdownDTO> getGrownCropsDropdown(Long user_id) {
        return grownCropsRepository.findByUserId(user_id);
        /*return grownCropsRepository.findByUserId(user_id).stream()
                .map(cropsInfo -> new GrownCropsDropdownDTO(cropsInfo.getGc_id(), cropsInfo.getName()))
                .collect(Collectors.toList());*/
    }
}
