package org.astrobrains.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionResponse {

    /**
     * Error Response Preferred in Production Environment
     * {
     *   "timestamp": "2025-08-23T00:15:32Z",
     *   "status": 400,
     *   "error": "Bad Request",
     *   "code": 1001,
     *   "message": "Current password is incorrect",
     *   "path": "/api/v1/auth/change-password"
     * }
     */
    private Instant timestamp;
    private Integer status;
    private Integer code;
    private String message;
    private String path;
    private Map<String, String> errors;
}
