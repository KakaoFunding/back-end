package org.kakaoshare.backend.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public final class MultiValueMapConverter {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private MultiValueMapConverter() {

    }

    public static MultiValueMap<String, String> convert(final Object object) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        try {
            final Map<String, String> convertedValue = MAPPER.convertValue(object, new TypeReference<Map<String, String>>() {
            });
            params.setAll(convertedValue);
            return params;
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}
