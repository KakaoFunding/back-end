package org.kakaoshare.backend.domain.brand.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.util.sort.error.SortErrorCode;
import org.kakaoshare.backend.common.util.sort.error.exception.NoMorePageException;
import org.kakaoshare.backend.domain.brand.dto.SimpleBrandDto;
import org.kakaoshare.backend.domain.brand.entity.Brand;
import org.kakaoshare.backend.domain.brand.repository.BrandRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    
    public Page<SimpleBrandDto> getSimpleBrandPage(final Long categoryId, final Pageable pageable) {
        Page<SimpleBrandDto> brandDtos = brandRepository.findAllSimpleBrandByCategoryId(categoryId, pageable);
        if(brandDtos.isEmpty()){
            throw new NoMorePageException(SortErrorCode.NO_MORE_PAGE);
        }
        return brandDtos;
    }

    public SimpleBrandDto getBrandNameWithIcon(Long brandId) {
        return brandRepository.findById(brandId)
                .map(SimpleBrandDto::from)
                .orElseThrow(() -> new IllegalArgumentException("Brand not found with id: " + brandId)); //todo 추후 커스텀 예외처리
    }
}
