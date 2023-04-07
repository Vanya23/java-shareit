package ru.practicum.shareit.request.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id; // уникальный идентификатор запроса;
    @Column(name = "description")
    String description; // текст запроса, содержащий описание требуемой вещи;
    @ManyToOne
    @JoinColumn(name = "id_user")
    User requestor; // пользователь, создавший запрос;
    @Column(name = "create_req")
    LocalDateTime created = LocalDateTime.now(); // дата и время создания запроса.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemRequest)) return false;
        return id != null && id.equals(((ItemRequest) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
