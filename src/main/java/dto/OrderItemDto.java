package dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
// 주문이 발생할 때 1건의 상품 정보를 저장하고있는 클래스

public class OrderItemDto {
    // 변수 cartProductId는 카트목록보기 메뉴에서만 사용됨
    private Long cartProductId; // 카트 상품 번호
    private Long productId; // 상품 번호
    private int quantity; // 구매 수량


}
