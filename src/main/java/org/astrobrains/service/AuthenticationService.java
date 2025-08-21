package org.astrobrains.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.astrobrains.dto.request.RegistrationRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    public void register(@Valid RegistrationRequest request) {
        // TODO: Implement registration logic. 1:55
    }
}
