package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoIn;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getAllItemByUserId(long userId) {

        return get("", userId);
    }

    public ResponseEntity<Object> getItemById(long userId, long itemId) {

        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> searchItemByText(long userId, String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search" + "?text={text}", userId, parameters);

    }

    public ResponseEntity<Object> addItem(long userId, ItemDtoIn itemDto) {

        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> patchItem(long userId, long itemId, ItemDtoIn itemDto) {

        return patch("/" + itemId, userId, itemDto);
    }


    public ResponseEntity<Object> postComment(long userId, long itemId, CommentDtoIn commentDto) {

        return post("/" + itemId + "/" + "comment", userId, commentDto);
    }


}

