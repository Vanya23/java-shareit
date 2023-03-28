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
import ru.practicum.shareit.GeneratePageableObj;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class RequestItemControllerTestWithContext {
    private final String basePath = "/requests";

    private final ItemRequestDtoOut itemRequestDtoOut = new ItemRequestDtoOut(
            1L,
            "описание",
            null,
            null);
    private final UserDto userDto = new UserDto(
            1L,
            "Пётр",
            "some@email.com");
    @Autowired
    ObjectMapper mapper;
    @MockBean
    ItemRequestService service;
    @MockBean
    GeneratePageableObj myServicePage;
    @Autowired
    private MockMvc mvc;

    @Test
    void addItemRequest() throws Exception {

        when(service.addItemRequest(anyInt(), any()))
                .thenReturn(itemRequestDtoOut);

        mvc.perform(post(basePath)
                        .content(mapper.writeValueAsString(itemRequestDtoOut))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllByUserId() throws Exception {
        List<ItemRequestDtoOut> itemRequestDtoOuts = new ArrayList<>();
        itemRequestDtoOuts.add(itemRequestDtoOut);
        when(service.getAllByUserId(anyInt()))
                .thenReturn(itemRequestDtoOuts);
        mvc.perform(
                        get(basePath)
                                .content(mapper.writeValueAsString(itemRequestDtoOut))
                                .header("X-Sharer-User-Id", "1")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    void getByRequestId() throws Exception {

        when(service.getByRequestId(anyInt(), anyInt()))
                .thenReturn(itemRequestDtoOut);

        mvc.perform(get(basePath + "/{requestId}", "1")
                        .content(mapper.writeValueAsString(itemRequestDtoOut))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    void getAllOtherUsers() throws Exception {
        List<ItemRequestDtoOut> itemRequestDtoOuts = new ArrayList<>();
        itemRequestDtoOuts.add(itemRequestDtoOut);
        when(service.getAllOtherUsers(anyInt()))
                .thenReturn(itemRequestDtoOuts);
        mvc.perform(
                        get(basePath + "/all")
                                .content(mapper.writeValueAsString(itemRequestDtoOut))
                                .header("X-Sharer-User-Id", "1")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllOtherUsersPage() throws Exception {
        List<ItemRequestDtoOut> itemRequestDtoOuts = new ArrayList<>();
        itemRequestDtoOuts.add(itemRequestDtoOut);
        Pageable pageableBlank = PageRequest.of(0, 1, Sort.by(Sort.DEFAULT_DIRECTION, "id"));
        PageImpl<ItemRequestDtoOut> ans = new PageImpl<>(itemRequestDtoOuts, pageableBlank, 100);
        when(service.getAllOtherUsersPage(anyInt(), any(), any()))
                .thenReturn(ans.getContent());
        mvc.perform(
                        get(basePath + "/all")
                                .content(mapper.writeValueAsString(itemRequestDtoOut))
                                .param("from", "0")
                                .param("size", "100")
                                .header("X-Sharer-User-Id", "1")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}