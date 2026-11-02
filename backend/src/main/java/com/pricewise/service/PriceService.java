package com.pricewise.service;

import com.pricewise.dto.PriceRequest;
import com.pricewise.entity.*;
import com.pricewise.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PriceService {
    private final ProductPriceRepository priceRepository;
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final PriceHistoryRepository historyRepository;

    public PriceService(ProductPriceRepository priceRepository, ProductRepository productRepository,
                        SellerRepository sellerRepository, PriceHistoryRepository historyRepository) {
        this.priceRepository = priceRepository;
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
        this.historyRepository = historyRepository;
    }

    public List<ProductPrice> getPrices(Long productId) {
        return priceRepository.findByProduct_IdOrderByPriceAsc(productId);
    }

    @Transactional
    public ProductPrice savePrice(PriceRequest request) {
        Product product = productRepository.findById(request.productId())
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        Seller seller = sellerRepository.findById(request.sellerId())
            .orElseThrow(() -> new IllegalArgumentException("Seller not found"));

        ProductPrice price = priceRepository.findByProduct_IdAndSeller_Id(
            request.productId(), request.sellerId()).orElseGet(ProductPrice::new);

        price.setProduct(product);
        price.setSeller(seller);
        price.setPrice(request.price());
        price.setProductUrl(request.productUrl());
        price.setAvailability(request.availability() == null ? "AVAILABLE" : request.availability());

        ProductPrice saved = priceRepository.save(price);
        PriceHistory history = new PriceHistory();
        history.setProductPrice(saved);
        history.setPrice(saved.getPrice());
        historyRepository.save(history);
        return saved;
    }

    public List<PriceHistory> getHistory(Long productId) {
        return historyRepository.findByProductPrice_Product_IdOrderByRecordedAtAsc(productId);
    }
}
