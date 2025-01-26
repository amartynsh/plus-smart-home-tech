package ru.yandex.practicum.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.yandex.practicum.model.Pageable;

public class Pagination {
    public static PageRequest getPageRequest(Pageable pageable) {
      return PageRequest.of(pageable.getPage(),
              pageable.getSize(),
              Sort.by(Sort.Direction.ASC,
                      String.valueOf(pageable.getSort().getFirst())));
    }
}
