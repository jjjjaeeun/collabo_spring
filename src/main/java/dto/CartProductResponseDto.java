package dto;

import com.coffee.coffee.entity.CartProduct;
import lombok.Getter;
import lombok.Setter;

// 리액트의 CartList.js 파일에서 fetchCartProducts() 함수 참조
// 카트 상품 목록 페이지에서 데이터 한개를 의미하는 자바 클래스
@Getter
@Setter
public class CartProductResponseDto {
    private Long cartProductId; // 카트 상품의 id로 수량 변경이나 삭제시 반드시 사용됨
    private Long productId; // 상품의 id
    private String name; // 상품 이름
    private String image; // 상품 이미지
    private int price; // 단가
    private int quantity; // 구매 또는 장바구니에 담을 수량
    private boolean checked = false; // 카트 상품 목록에서 체크 여부

    public CartProductResponseDto(CartProduct cartProduct) {
        this.cartProductId = cartProduct.getId();
        this.productId = cartProduct.getProduct().getId();
        this.name = cartProduct.getProduct().getName();
        this.image = cartProduct.getProduct().getImage();
        this.price = cartProduct.getProduct().getPrice();
        this.quantity = cartProduct.getQuantity();
    }
}
