package ru.practicum.shareit.item.model;

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
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id; // уникальный идентификатор комментария;
    @Column(name = "text")
    String text; // текст комментария;
    @ManyToOne
    @JoinColumn(name = "id_item")
    Item item; // item по которому написали комментарий;
    @ManyToOne
    @JoinColumn(name = "id_user")
    User author; // пользователь, создавший комментарий;

    @Column(name = "created_date")
    LocalDateTime created; //дата создания комментария.


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        return id != null && id.equals(((Comment) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}

