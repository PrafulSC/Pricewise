package com.pricewise.repository;
import com.pricewise.entity.PriceAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {
    List<PriceAlert> findByUser_Id(Long userId);
}
