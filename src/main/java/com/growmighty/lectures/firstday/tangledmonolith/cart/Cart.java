package com.growmighty.lectures.firstday.tangledmonolith.cart;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "carts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Column(nullable = false)
    private Long userId;

    public static Cart create(Long userId) {
        Cart cart = new Cart();
        cart.userId = userId;
        return cart;
    }

    public void addItem(Long productId, Integer quantity) {
        validateQuantity(quantity);
        findItem(productId).ifPresentOrElse(
                item -> item.increaseQuantity(quantity),
                () -> items.add(CartItem.create(productId, quantity))
        );
    }

    public void changeQuantity(Long productId, Integer quantity) {
        validateQuantity(quantity);
        getItem(productId).changeQuantity(quantity);
    }

    public void removeItem(Long productId){
        CartItem cartItem = items.stream()
                .filter(cartItem1 -> cartItem1.getProductId().equals(productId))
                .findFirst().orElseThrow(()-> new IllegalArgumentException("존재하지 않는 상품입니다."));

        items.remove(cartItem);
    }

    private void validateQuantity(Integer quantity){
        if(quantity <= 0){
            throw new IllegalArgumentException("수량은 1개 이상이어야 한다.");
        }
    }

    private Optional<CartItem> findItem(Long productId){
        return items.stream()
                .filter(cartItem1 -> cartItem1.getProductId().equals(productId))
                .findFirst();
    }

    private CartItem getItem(Long productId){
        return findItem(productId).orElseThrow(()-> new IllegalArgumentException("카트에 존재하지 않은 상품입니다"));
    }

    public void clear() {
        this.items.clear();
    }
}
