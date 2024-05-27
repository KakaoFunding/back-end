package org.kakaoshare.backend.domain.brand.service;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.brand.dto.SimpleBrandDto;
import org.kakaoshare.backend.domain.brand.repository.BrandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    
    public List<SimpleBrandDto> getSimpleBrandPage(final Long categoryId) {
        return brandRepository.findAllSimpleBrandByCategoryId(categoryId);
    }

    public SimpleBrandDto getBrandNameWithIcon(Long brandId) {
        return brandRepository.findById(brandId)
                .map(SimpleBrandDto::from)
                .orElseThrow(() -> new IllegalArgumentException("Brand not found with id: " + brandId)); //todo 추후 커스텀 예외처리
    }
}
