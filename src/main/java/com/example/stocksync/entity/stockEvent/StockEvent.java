package com.example.stocksync.entity.stockEvent;

import com.example.stocksync.entity.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class StockEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @Enumerated(EnumType.STRING)
    StockEventStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime eventTime;

    public StockEvent(Product product,  StockEventStatus status) {
        this.product = product;
        this.status = status;
    }
}
