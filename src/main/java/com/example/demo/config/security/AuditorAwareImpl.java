package com.example.demo.config.security;

import com.example.demo.commons.security.SecurityUtil;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtil.authenticatedUsername());
    }

}
