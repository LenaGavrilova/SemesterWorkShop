package ru.kpfu.itis.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", nullable = false, columnDefinition = "text")
    @NotEmpty(message = "Название не может быть пустым")
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    @NotEmpty(message = "Описание не может быть пустым")
    private String description;

    @Column(name = "price", nullable = false)
    @Min(value = 1, message = "Цена не может быть отрицательной или нулевой")
    @NotNull(message = "Цена товара не может быть пустой")
    private float price;

    @Column(name = "publishingHouse", nullable = false)
    @NotEmpty(message = "Издательство не может быть пустым")
    private String publishingHouse;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "book")
    List<Image> imageList = new ArrayList<>();

    @ManyToOne(optional = false)
    private Category category;

    @JsonIgnore
    @ManyToMany()
    @JoinTable(name = "book_cart", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> userList;


    @JsonIgnore
    @OneToMany(mappedBy = "book")
    private List<Order> orderList;

    private LocalDateTime dataTimeOfCreated;

    @PrePersist
    private void init() {
        dataTimeOfCreated = LocalDateTime.now();
    }


    public void addImageBook(Image image) {
        image.setBook(this);
        imageList.add(image);
    }

    @JsonIgnore
    public String getFirstImage() {
        if (imageList.size() > 1) {
            Image img = imageList.get(0);
            return img.getFileName();
        } else return "NOT_FOUND.png";
    }
}
