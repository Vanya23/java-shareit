package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.MyServicePage;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTestWithContext {
    private final String basePath = "/items";

    private final UserDto userDto = new UserDto(
            1L,
            "Пётр",
            "some@email.com");
    private final ItemDtoIn itemDtoIn = new ItemDtoIn(
            1L,
            "отвертка",
            "отвертка++",
            Boolean.TRUE, null
    );
    private final ItemDtoOut itemDtoOut = new ItemDtoOut(
            1L,
            "отвертка",
            "отвертка++",
            Boolean.TRUE, null, null, null, null
    );
    private final CommentDtoIn commentDtoIn = new CommentDtoIn("comment");
    private final CommentDtoOut commentDtoOut = new CommentDtoOut(1L, "comment",
            "Пётр",
            null);


    @Autowired
    ObjectMapper mapper;
    @MockBean
    ItemService itemService;
    @MockBean
    MyServicePage myServicePage;
    @Autowired
    private MockMvc mvc;

    @Test
    void addItem() throws Exception {
        when(itemService.addItem(anyInt(), any()))
                .thenReturn(itemDtoOut);

        mvc.perform(post(basePath)
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void patchItem() throws Exception {
        when(itemService.patchItem(anyInt(), anyInt(), any()))
                .thenReturn(itemDtoOut);

        mvc.perform(patch(basePath + "/{itemId}", "1")
                        .content(mapper.writeValueAsString(userDto))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void postComment() throws Exception {
        when(itemService.postComment(anyInt(), anyInt(), any()))
                .thenReturn(commentDtoOut);

        mvc.perform(post(basePath + "/{itemId}/comment", "1")
                        .content(mapper.writeValueAsString(commentDtoOut))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllItemByUserId() throws Exception {
        List<ItemDtoOut> itemDtoOuts = new ArrayList<>();
        itemDtoOuts.add(itemDtoOut);
        when(itemService.getAllItemByUserId(anyInt()))
                .thenReturn(itemDtoOuts);
        mvc.perform(
                        get(basePath)
                                .content(mapper.writeValueAsString(itemDtoOut))
                                .header("X-Sharer-User-Id", "1")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllItemByUserIdPage() throws Exception {
        List<ItemDtoOut> itemDtoOuts = new ArrayList<>();
        itemDtoOuts.add(itemDtoOut);
        Pageable pageableBlank = PageRequest.of(0, 1, Sort.by(Sort.DEFAULT_DIRECTION, "id"));
        PageImpl<ItemDtoOut> ans = new PageImpl<>(itemDtoOuts, pageableBlank, 100);
        when(itemService.getAllItemByUserIdPage(anyInt(), any(), any()))
                .thenReturn(ans);
        mvc.perform(
                        get(basePath)
                                .param("from", "0")
                                .param("size", "100")
                                .content(mapper.writeValueAsString(itemDtoOut))
                                .header("X-Sharer-User-Id", "1")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getItemById() throws Exception {
        when(itemService.getItemById(anyInt(), anyInt()))
                .thenReturn(itemDtoOut);

        mvc.perform(get(basePath + "/{itemId}", "1")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void searchItemByText() throws Exception {
        List<ItemDtoOut> itemDtoOuts = new ArrayList<>();
        itemDtoOuts.add(itemDtoOut);
        when(itemService.searchItemByText(any()))
                .thenReturn(itemDtoOuts);
        mvc.perform(
                        get(basePath + "/search")
                                .content(mapper.writeValueAsString(itemDtoOut))
                                .param("text", "text")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void searchItemByTextPage() throws Exception {
        List<ItemDtoOut> itemDtoOuts = new ArrayList<>();
        itemDtoOuts.add(itemDtoOut);
        Pageable pageableBlank = PageRequest.of(0, 1, Sort.by(Sort.DEFAULT_DIRECTION, "id"));
        PageImpl<ItemDtoOut> ans = new PageImpl<>(itemDtoOuts, pageableBlank, 100);
        when(itemService.searchItemByTextPage(any(), any(), any()))
                .thenReturn(ans);
        mvc.perform(
                        get(basePath + "/search")
                                .content(mapper.writeValueAsString(itemDtoOut))
                                .param("text", "text")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}