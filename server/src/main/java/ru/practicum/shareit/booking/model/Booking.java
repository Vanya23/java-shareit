package ru.practicum.shareit.booking.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;  // уникальный идентификатор бронирования;
    @NonNull
    @Column(name = "start_booking")
    LocalDateTime start; // дата и время начала бронирования;
    @NonNull
    @Column(name = "end_booking")
    LocalDateTime end; // дата и время конца бронирования;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "id_item")
    Item item; // вещь, которую пользователь бронирует;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "id_user")
    User booker; //пользователь, который осуществляет бронирование;
    @NonNull
    @Enumerated(EnumType.STRING)
    BookingStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking)) return false;
        return id != null && id.equals(((Booking) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}
