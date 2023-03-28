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
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTestWithContext {
    private final String basePath = "/bookings";

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

    private final BookingDtoOut bookingDtoOut = new BookingDtoOut(
            1L,
            LocalDateTime.now().plusDays(1).toString(),
            LocalDateTime.now().plusDays(2).toString(),
            itemDtoIn,
            userDto,
            BookingStatus.APPROVED.name()
    );
    private final BookingDtoInput bookingDtoInput = new BookingDtoInput(
            1L,
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2),
            1L
    );


    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookingService service;
    @MockBean
    GeneratePageableObj myServicePage;
    @Autowired
    private MockMvc mvc;

    @Test
    void addBooking() throws Exception {
        when(service.addBooking(anyInt(), any(BookingDtoInput.class)))
                .thenReturn(bookingDtoOut);

        mvc.perform(post(basePath)
                        .content(mapper.writeValueAsString(bookingDtoInput))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void patchBooking() throws Exception {
        when(service.patchBooking(anyInt(), anyInt(), any(Boolean.class)))
                .thenReturn(bookingDtoOut);

        mvc.perform(patch(basePath + "/{bookingId}", "1")
                        .content(mapper.writeValueAsString(bookingDtoInput))
                        .param("approved", "True")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBookingsByUserId() throws Exception {
        List<BookingDtoOut> bookingDtoOuts = new ArrayList<>();
        bookingDtoOuts.add(bookingDtoOut);
        when(service.getAllBookingsByUserId(anyInt(), any()))
                .thenReturn(bookingDtoOuts);

        mvc.perform(get(basePath)
                        .content(mapper.writeValueAsString(bookingDtoInput))
                        .param("state", "All")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBookingsByUserIdPage() throws Exception {
        List<BookingDtoOut> bookingDtoOuts = new ArrayList<>();
        bookingDtoOuts.add(bookingDtoOut);
        Pageable pageableBlank = PageRequest.of(0, 1, Sort.by(Sort.DEFAULT_DIRECTION, "id"));
        PageImpl<BookingDtoOut> ans = new PageImpl<>(bookingDtoOuts, pageableBlank, 100);
        when(service.getAllBookingsByUserIdPage(anyInt(), any(), any(), any()))
                .thenReturn(ans.getContent());

        mvc.perform(get(basePath)
                        .content(mapper.writeValueAsString(bookingDtoInput))
                        .param("from", "0")
                        .param("size", "100")
                        .param("state", "All")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBookingsByOwner() throws Exception {
        List<BookingDtoOut> bookingDtoOuts = new ArrayList<>();
        bookingDtoOuts.add(bookingDtoOut);
        when(service.getAllBookingsByOwner(anyInt(), any()))
                .thenReturn(bookingDtoOuts);

        mvc.perform(get(basePath + "/owner")
                        .content(mapper.writeValueAsString(bookingDtoInput))
                        .param("state", "All")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBookingsByOwnerPage() throws Exception {
        List<BookingDtoOut> bookingDtoOuts = new ArrayList<>();
        bookingDtoOuts.add(bookingDtoOut);
        Pageable pageableBlank = PageRequest.of(0, 1, Sort.by(Sort.DEFAULT_DIRECTION, "id"));
        PageImpl<BookingDtoOut> ans = new PageImpl<>(bookingDtoOuts, pageableBlank, 100);
        when(service.getAllBookingsByOwnerPage(anyInt(), any(), any(), any()))
                .thenReturn(ans.getContent());

        mvc.perform(get(basePath + "/owner")
                        .content(mapper.writeValueAsString(bookingDtoInput))
                        .param("from", "0")
                        .param("size", "100")
                        .param("state", "All")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingById() throws Exception {
        when(service.getBookingById(anyInt(), anyInt()))
                .thenReturn(bookingDtoOut);

        mvc.perform(get(basePath + "/{bookingId}", "1")
                        .content(mapper.writeValueAsString(bookingDtoInput))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}