package com.pricewise.repository;
import com.pricewise.entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
    List<ProductPrice> findByProduct_IdOrderByPriceAsc(Long productId);
    Optional<ProductPrice> findByProduct_IdAndSeller_Id(Long productId, Long sellerId);
}
