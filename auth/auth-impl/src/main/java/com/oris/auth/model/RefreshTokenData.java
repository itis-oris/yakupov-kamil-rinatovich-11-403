package com.oris.auth.model;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenData {
    private UUID accountId;
    private Instant createdAt = Instant.now();
}
