package com.pricewise.repository;
import com.pricewise.entity.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
    List<PriceHistory> findByProductPrice_IdOrderByRecordedAtAsc(Long priceId);
    List<PriceHistory> findByProductPrice_Product_IdOrderByRecordedAtAsc(Long productId);
}
