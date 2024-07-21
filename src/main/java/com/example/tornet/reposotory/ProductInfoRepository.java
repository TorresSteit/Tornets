package com.example.tornet.reposotory;

import com.example.tornet.model.Cart;
import com.example.tornet.model.Customer;
import com.example.tornet.model.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductInfoRepository extends JpaRepository<ProductInfo, Long> {

    List<ProductInfo> findAllByProduct_Id(Long productId);
    @Query("SELECT pi FROM ProductInfo pi WHERE pi.product.id = :productId AND :size MEMBER OF pi.sizes")
    Optional<ProductInfo> findByProductIdAndSize(@Param("productId") Long productId, @Param("size") String size);

    @Query("SELECT pi FROM ProductInfo pi WHERE pi.product.id = :productId")
    List<ProductInfo> findAllByProductId(@Param("productId") Long productId);
    Page<ProductInfo> findByCartCustomer(Customer customer, Pageable pageable);

    Page<ProductInfo> findByCart(Cart cart, Pageable pageable);

    void deleteByCartId(Long cartId);
}
