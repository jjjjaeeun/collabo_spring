package com.coffee.coffee.controller;

import com.coffee.coffee.entity.Cart;
import com.coffee.coffee.entity.CartProduct;
import com.coffee.coffee.entity.Member;
import com.coffee.coffee.entity.Product;
import com.coffee.coffee.service.CartProductService;
import com.coffee.coffee.service.CartService;
import com.coffee.coffee.service.MemberService;
import com.coffee.coffee.service.ProductService;
import dto.CartProductDto;
import dto.CartProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
테스트 시나리오
장바구니가 없을 때 상품을 담기
    1. 로그인한 사람의 회원 아이디와 상품 엔터티의 상품 번호를 확인
    2. Cart 엔터티의 회원 아이디가 로그인 한 사람인가요?
    3. Cart 엔터티의 카트 아이디와 CartProduct 엔터티의 카트 아이디가 동일해야함
    4. CartProduct 엔터티의 상품 아이디와 Product 엔터티의 상품 아이디가 동일해야함

장바구니가 있을 때 상품을 담기
    1. Cart 엔터티에 변동 사항은 없음
    2. CartProduct 엔터티에 신규 상품 정보만 추가됨

*/

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor // final 키워드를 가지고 있는 필드의 생성자를 이용하여 자동을 주입해줌
public class CartController {
    private final MemberService memberService;
    private final ProductService productService;
    private final CartService cartService;
    private final CartProductService cartProductService;

    @PostMapping("/insert") // 리액트에서 장바구니 버튼을 클릭하였습니다.
    public ResponseEntity<String> addToCart(@RequestBody CartProductDto dto) {
        // Member 또는 Product가 유효한 정보인지 확인
        Optional<Member> memberOptional = memberService.findMemberById(dto.getMemberId());
        Optional<Product> productOptional = productService.fineProductById(dto.getProductId());

        if (memberOptional.isEmpty() || productOptional.isEmpty()) { // 정보가 무효하면
            return ResponseEntity.badRequest().body("회원 또는 상품 정보가 올바르지 않습니다.");
        }

        // Member와 Product의 객체 정보 가져오기
        Member member = memberOptional.get(); // 진짜 배기 회원 정보
        Product product = productOptional.get();

        // 재고가 충분한지 확인
        if (product.getStock() < dto.getQuantity()) {
            return ResponseEntity.badRequest().body("재고수량이 부족합니다.");
        }

        // Cart 조회 또는 신규 작성
        Cart cart = cartService.findByMember(member);

        if (cart == null) {
            Cart newCart = new Cart(); // 새로운 cart
            newCart.setMember(member); // 고객이 cart를 집어듬
            cart = cartService.saveCart(newCart); // 데이터 베이스에 저장
        }

        // 선택한 상품을 'cart product'에 담기
        CartProduct cp = new CartProduct();
        cp.setCart(cart);
        cp.setProduct(product);
        cp.setQuantity(dto.getQuantity());
        cartProductService.saveCartProduct(cp);

        // 재고 수량은 차감하지 않습니다.

        return ResponseEntity.ok("요청하신 상품이 장바구니에 추가되었습니다.");
    }

    @GetMapping("/list/{memberId}") // 특정 사용자의 카트 상품 목록을 조회함
    public ResponseEntity<List<CartProductResponseDto>> getCartProducts(@PathVariable Long memberId) {
        Optional<Member> optionalMember = this.memberService.findMemberById(memberId);
        if (optionalMember.isEmpty()) { // 무효한 회원 정보 ( 회원이 없다는 뜻)
            return ResponseEntity.badRequest().build();

        }
        Member member = optionalMember.get();
        Cart cart = cartService.findByMember(member);

        if (cart == null) {
            cart = new Cart();
        }

        // cartProducts: 과거에 내가 Cart에 담아 두었던 목록을 의미하는 컬렉션
        List<CartProductResponseDto> cartProducts = new ArrayList<>();

        for (CartProduct cp : cart.getCartProducts()) {
            cartProducts.add(new CartProductResponseDto(cp));
        }

        System.out.println("카트 상품 개수: " + cartProducts.size());

        return ResponseEntity.ok(cartProducts); // 전체 카트상품 반환
    }

    String message = null;

    // http://localhost:9000/cart/edit/100?quantity=10
    @PatchMapping("/edit/{cartProductId}")
    public ResponseEntity<String> updateCartProduct(@PathVariable Long cartProductId, @RequestParam(required = false) Integer quantity) {
        System.out.println("카트 상품 아이디: " + cartProductId);
        System.out.println("변경할 갯수: " + quantity);

        if(quantity == null){
            message = "장바구니 품목은 최소 1개 이상이어야 합니다.";
            return ResponseEntity.badRequest().body(message);
        }

        Optional<CartProduct> cartProductOptional = this.cartProductService.findCartProductById(cartProductId);

        if (cartProductOptional.isEmpty()){
            message = "장바구니 품목을 찾을 수 없습니다.";
            return ResponseEntity.badRequest().body(message);
        }

        CartProduct cartProduct = cartProductOptional.get();
        cartProduct.setQuantity(quantity); // 기존 내용 덮어 쓰기
        // cartProduct.setQuantity(cartProduct.getQuantity()+quantity); // 기존 내용 누적 하기

        cartProductService.saveCartProduct(cartProduct); // 데이터 베이스에 저장

        message = "카트 상품 아이디 " + cartProductId + "번이 "+quantity+"개로 수정이 되었습니다.";
        return ResponseEntity.ok(message);
    }
}
