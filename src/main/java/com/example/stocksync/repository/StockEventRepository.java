package com.example.stocksync.repository;
import com.example.stocksync.entity.stockEvent.StockEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockEventRepository extends JpaRepository<StockEvent, Long> {}