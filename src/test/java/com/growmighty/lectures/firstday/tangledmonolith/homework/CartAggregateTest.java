package com.growmighty.lectures.firstday.tangledmonolith.homework;

import com.growmighty.lectures.firstday.tangledmonolith.cart.Cart;
import com.growmighty.lectures.firstday.tangledmonolith.cart.CartItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * [과제 DDD-2] 장바구니 애그리거트 불변식 테스트 — 뼈대입니다.
 *
 * 1. 먼저 Cart / CartItem 을 아래 규칙대로 고치세요.
 * 2. 각 테스트의 fail(...) 줄을 지우고 given / when / then 을 채우세요.
 * 3. 스프링 없이 도는 순수 단위 테스트로 작성합니다 (@SpringBootTest 금지).
 *
 * 목표 API 예시 (이름은 바꿔도 됩니다)
 *   Cart cart = Cart.create(userId);
 *   cart.addItem(productId, quantity);
 *   cart.changeQuantity(productId, newQuantity);
 *   cart.removeItem(productId);
 */
class CartAggregateTest {

    @Test
    @DisplayName("새 상품을 담으면 항목이 하나 추가된다")
    void 새_상품을_담으면_항목이_추가된다() {
        // given: 빈 장바구니
        Cart cart = Cart.create(1L);
        // when : addItem(1L, 2)
        cart.addItem(1L,2);
        // then : 항목 1개, 수량 2
        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getProductId()).isEqualTo(1);
        assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(2);
    }

    @Test
    @DisplayName("같은 상품을 다시 담으면 새 항목이 아니라 수량이 합쳐진다")
    void 같은_상품을_담으면_수량이_합쳐진다() {
        Cart cart = Cart.create(1L);
        cart.addItem(1L,2);
        cart.addItem(1L,3);

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getProductId()).isEqualTo(1);
        assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("수량이 0 이하이면 담을 수 없다")
    void 수량이_0_이하면_예외() {

        Cart cart = Cart.create(1L);

        assertThatThrownBy(()->cart.addItem(1L,0)).isInstanceOf(IllegalArgumentException.class);
        assertThat(cart.getItems()).isEmpty();
    }

    @Test
    @DisplayName("담긴 상품의 수량을 바꿀 수 있다")
    void 수량을_변경한다() {

        Cart cart = Cart.create(1L);
        cart.addItem(1L,2);
        cart.changeQuantity(1L, 5);

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getProductId()).isEqualTo(1);
        assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("장바구니에 없는 상품의 수량을 바꾸면 예외가 난다")
    void 없는_상품의_수량_변경은_예외() {

        Cart cart = Cart.create(1L);
        cart.addItem(1L,2);

        assertThatThrownBy(()->cart.changeQuantity(2L, 5)).isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("담긴 상품을 뺄 수 있다")
    void 상품을_뺀다() {

        Cart cart = Cart.create(1L);
        cart.addItem(1L,2);

        cart.removeItem(1L);

        assertThat(cart.getItems()).hasSize(0);
    }
}
