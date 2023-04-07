package ru.practicum.shareit;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class GeneratePageableObj {

    public Pageable checkAndCreatePageable(String from, String size, Sort sort) {
        int fromNum = Integer.parseInt(from);
        int sizeNum = Integer.parseInt(size);
        if (fromNum < 0 || sizeNum <= 0) throw new RuntimeException("");

        if (fromNum >= 2)
            fromNum -= 2;
        // --------

        Pageable pageable = PageRequest.of(fromNum, sizeNum, sort);
        return pageable;
    }

    public Pageable getPageableBlank() {
        Pageable pageableBlank = PageRequest.of(0, 1, Sort.by(Sort.DEFAULT_DIRECTION, "id"));
        return pageableBlank;
    }
}
