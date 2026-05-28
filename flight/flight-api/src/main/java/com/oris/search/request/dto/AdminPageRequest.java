package com.oris.search.request.dto;

public record AdminPageRequest(
        int page,
        int size
) {
    public static final int DEFAULT_SIZE = 20;

    public AdminPageRequest {
        if (page < 0) page = 0;
        if (size <= 0 || size > 100) size = DEFAULT_SIZE;
    }

    public static AdminPageRequest of(Integer page, Integer size) {
        return new AdminPageRequest(
                page  != null ? page  : 0,
                size  != null ? size  : DEFAULT_SIZE
        );
    }

    public int offset() { return page * size; }
}
