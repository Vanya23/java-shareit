package ru.practicum.shareit;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class MyServicePage {

    public Pageable checkAndCreatePageable(String from, String size, Sort sort) {
        int fromNum = Integer.valueOf(from);
        int sizeNum = Integer.valueOf(size);
        if (fromNum < 0 || sizeNum <= 0) throw new RuntimeException("");

        Pageable pageable = PageRequest.of(fromNum, sizeNum, sort);
        return pageable;
    }

    public Pageable getPageableBlank() {
        Pageable pageableBlank = PageRequest.of(0, 1, Sort.by(Sort.DEFAULT_DIRECTION, "id"));
        return pageableBlank;
    }
}
