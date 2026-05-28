package com.oris.search.response;

import com.oris.search.request.dto.AdminPageRequest;
import java.util.List;

public record AdminPage<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrev
) {
    public static <T> AdminPage<T> of(List<T> content, AdminPageRequest req, long total) {
        int totalPages = (int) Math.ceil((double) total / req.size());
        return new AdminPage<>(
                content,
                req.page(),
                req.size(),
                total,
                totalPages,
                req.page() < totalPages - 1,
                req.page() > 0
        );
    }
}
