package com.example.board.config.audit;

import com.example.board.config.security.CustomUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof String) {
            return Optional.of((String) principal);
        } else {
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            return Optional.of(userDetails.getUsername());
        }
    }
}
