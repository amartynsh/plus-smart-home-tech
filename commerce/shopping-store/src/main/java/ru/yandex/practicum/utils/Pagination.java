package ru.yandex.practicum.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.yandex.practicum.model.Pageable;

public class Pagination {
    public static PageRequest getPageRequest(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPage(),
                pageable.getSize(),
                Sort.by(pageable.getSort().getFirst()));
        return pageRequest;
    }
}
