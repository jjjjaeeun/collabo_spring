package com.coffee.coffee.controller;

import com.coffee.coffee.constant.Role;
import com.coffee.coffee.entity.Member;
import com.coffee.coffee.entity.Order;
import com.coffee.coffee.entity.OrderProduct;
import com.coffee.coffee.entity.Product;
import com.coffee.coffee.service.CartProductService;
import com.coffee.coffee.service.MemberService;
import com.coffee.coffee.service.OrderService;
import com.coffee.coffee.service.ProductService;
import dto.OrderDto;
import dto.OrderItemDto;
import dto.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ProductService productService;
    private final CartProductService cartProductService;

    // 리액트의 카트 목록이나 주문하기 버튼을 눌러서 주문을 시도함
    @PostMapping("") // CartList.js 파일의 makeOrder() 함수와 연관이 있음.
    public ResponseEntity<?> order(@RequestBody OrderDto dto) {
        System.out.println(dto);

        // 회원(Member) 객체 생성
        Optional<Member> optionalMember = memberService.findMemberById(dto.getMemberId());
        if (!optionalMember.isPresent()) {
            throw new RuntimeException("회원이 존재하지 않습니다.");
        }
        Member member = optionalMember.get();

        // 마일리지 적립 시스템이면 마일리지 적립은 여기서 해야함

        // 주문(Order) 객체 생성
        Order order = new Order();

        order.setMember(member);// 이 사람이 주문자입니다.
        order.setOrderdate(LocalDate.now()); // 주문 시점
        order.setStatus(dto.getStatus());

        // 주문상품(OrderProduct)들은 확장 for 구문을 사용
        List<OrderProduct> orderProductList = new ArrayList<>();

        for (OrderItemDto item : dto.getOrderItems()) {
            // item은 주문하고자 하는 주문 상품 1개를 의미
            Optional<Product> optionalProduct = productService.findProductById(item.getProductId());

            if ((!optionalProduct.isPresent())) {
                throw new RuntimeException("해당 상품이 존재하지 않습니다.");
            }
            Product product = optionalProduct.get();

            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("재고 수량이 부족합니다.");
            }

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);
            orderProduct.setQuantity(item.getQuantity());

            // 리스트 컬렉션에 각 주문 상품을 담아줍니다.
            orderProductList.add(orderProduct);

            //상품의 재고수량 빼기
            product.setStock(product.getStock() - item.getQuantity());

            // 카트에 담겨 있던 품목을 삭제해 줘야 합니다.
            Long cartProductId = item.getCartProductId();
            //System.out.println("cartProductId : " + cartProductId);

            if (cartProductId != null) { // 장바구니 내역에서 `주문하기` 버튼을 클릭한 경우에 해당함
                cartProductService.deleteCartProductById(cartProductId);

            } else {
                System.out.println("상품 상세 보기에서 클릭하셨군요.");
            }
        }

        order.setOrderProducts(orderProductList);

        // 주문객체를 저장
        orderService.saveOrder(order);

        return ResponseEntity.ok("주문이 완료되었습니다.");
    }

    // 특정한 회원의 주문 정보를 최신 날짜 순으로 조회합니다.
    // http://localhost:9000/order/list?memberId=253
    @GetMapping("/list") // 리액트의 OrderList.js 파일 내의 useEffect 참조
    public ResponseEntity<List<OrderResponseDto>> getOrderList(@RequestParam Long memberId, @RequestParam Role role) {
        System.out.println("로그인 한 사람의 id: " + memberId);
        System.out.println("로그인 한 사람 역할: " + role);

        List<Order> orders = null;

        if (role == Role.ADMIN) { // 관리자면 모든 주문 내역을 조회하기
            orders = orderService.findAllOrders();

        } else { // 일반인인 경우에는 자기 주문 정보만 조회하기
            orders = orderService.findByMemberId(memberId);
        }

        //멤버 아이디로 주문정보 가져오기
        System.out.println("주문 건수: " + orders.size());

        List<OrderResponseDto> responseDtos = new ArrayList<>();

        for (Order bean : orders) {
            OrderResponseDto dto = new OrderResponseDto();

            // 주문의 기초 정보 세팅
            dto.setOrderId(bean.getId());
            dto.setOrderDate(bean.getOrderdate());
            dto.setStatus(bean.getStatus().name());

            // 주문 상품 여러개에 대한 세팅
            List<OrderResponseDto.OrderItem> orderItems = new ArrayList<>();

            for (OrderProduct op : bean.getOrderProducts()) {
                OrderResponseDto.OrderItem item
                        = new OrderResponseDto.OrderItem(op.getProduct().getName(), op.getQuantity());
                orderItems.add(item);

            }
            dto.setOrderItems(orderItems);

            responseDtos.add(dto);
        }
        return ResponseEntity.ok(responseDtos);
    }
    @GetMapping("/update/{orderId}")
    public String ddd(@PathVariable Long orderId){
        System.out.println("수정할 항목 : " + orderId);
        return null ;
    }
}
