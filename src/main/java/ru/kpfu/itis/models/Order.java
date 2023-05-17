package ru.kpfu.itis.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "number")
    private String number;

    @Column(name = "count")
    private int count;

    @Column(name = "price")
    private float price;

    @Column(name = "dateTime")
    private LocalDateTime dateTime;

    @Column(name = "status", columnDefinition = "text")
    private Status status;

    @ManyToOne(optional = false)
    private Book book;

    @ManyToOne(optional = false)
    private User user;

    @PrePersist
    private void init() {
        dateTime = LocalDateTime.now();
    }

    public Order(String number, int count, float price, Status status, Book book, User user) {
        this.number = number;
        this.count = count;
        this.price = price;
        this.status = status;
        this.book = book;
        this.user = user;
    }


}