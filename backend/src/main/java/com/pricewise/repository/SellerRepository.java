package com.pricewise.repository;
import com.pricewise.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SellerRepository extends JpaRepository<Seller, Long> {}
