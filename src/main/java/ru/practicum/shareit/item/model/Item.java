package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id; // уникальный идентификатор вещи;
    @Column(name = "full_name")
    String name; // краткое название;
    @NotBlank
    @Column(name = "description")
    String description; // развёрнутое описание;
    @Column(name = "available")
    Boolean available; // статус о том, доступна или нет вещь для аренды;
    @ManyToOne
    @JoinColumn(name = "id_user")
    User owner; // владелец вещи;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_requests")
    ItemRequest request; // если вещь была создана по запросу другого пользователя, то в этом

    @Transient
    Booking lastBooking; // даты последнего бронирования
    @Transient
    Booking nextBooking; // даты ближайшего бронирования
    @Transient
    List<Comment> comments; // комментарии


    public Item(String name, String description, Boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }

    //    поле будет храниться ссылка на соответствующий запрос.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        return id != null && id.equals(((Item) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
