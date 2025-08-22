package com.example.stocksync.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_events")
@Getter
@Setter
@NoArgsConstructor
public class StockEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;
    private String vendor;
    private LocalDateTime eventTime;

    public StockEvent(String sku, String vendor, LocalDateTime eventTime) {
        this.sku = sku;
        this.vendor = vendor;
        this.eventTime = eventTime;
    }
}
