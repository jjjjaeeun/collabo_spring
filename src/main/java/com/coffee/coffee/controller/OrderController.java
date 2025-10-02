package com.coffee.coffee.controller;

import com.coffee.coffee.constant.OrderStatus;
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

    // ✅ 주문 생성
    @PostMapping("")
    public ResponseEntity<?> order(@RequestBody OrderDto dto) {
        System.out.println(dto);

        Optional<Member> optionalMember = memberService.findMemberById(dto.getMemberId());
        if (!optionalMember.isPresent()) {
            throw new RuntimeException("회원이 존재하지 않습니다.");
        }
        Member member = optionalMember.get();

        Order order = new Order();
        order.setMember(member);
        order.setOrderdate(LocalDate.now());
        order.setStatus(dto.getStatus());

        List<OrderProduct> orderProductList = new ArrayList<>();

        for (OrderItemDto item : dto.getOrderItems()) {
            Optional<Product> optionalProduct = productService.findProductById(item.getProductId());
            if (!optionalProduct.isPresent()) {
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

            orderProductList.add(orderProduct);

            // 상품 재고 차감
            product.setStock(product.getStock() - item.getQuantity());

            // 장바구니에서 삭제
            Long cartProductId = item.getCartProductId();
            if (cartProductId != null) {
                cartProductService.deleteCartProductById(cartProductId);
            } else {
                System.out.println("상품 상세 보기에서 주문됨");
            }
        }

        order.setOrderProducts(orderProductList);
        orderService.saveOrder(order);

        return ResponseEntity.ok("주문이 완료되었습니다.");
    }

    // ✅ 주문 목록 조회 (관리자는 전체, 일반 회원은 본인 것만)
    @GetMapping("/list")
    public ResponseEntity<List<OrderResponseDto>> getOrderList(
            @RequestParam Long memberId,
            @RequestParam Role role) {
        System.out.println("로그인 한 사람의 id: " + memberId);
        System.out.println("로그인 한 사람 역할: " + role);

        List<Order> orders;
        if (role == Role.ADMIN) {
            orders = orderService.findAllOrders(OrderStatus.PENDING);
        } else {
            orders = orderService.findByMemberId(memberId, OrderStatus.PENDING);
        }

        System.out.println("주문 건수: " + orders.size());

        List<OrderResponseDto> responseDtos = new ArrayList<>();
        for (Order bean : orders) {
            OrderResponseDto dto = new OrderResponseDto();
            dto.setOrderId(bean.getId());
            dto.setOrderDate(bean.getOrderdate());
            dto.setStatus(bean.getStatus().name());

            List<OrderResponseDto.OrderItem> orderItems = new ArrayList<>();
            for (OrderProduct op : bean.getOrderProducts()) {
                OrderResponseDto.OrderItem item =
                        new OrderResponseDto.OrderItem(op.getProduct().getName(), op.getQuantity());
                orderItems.add(item);
            }
            dto.setOrderItems(orderItems);

            responseDtos.add(dto);
        }
        return ResponseEntity.ok(responseDtos);
    }

    // ✅ 주문 상태 변경
    @PutMapping("/update/status/{orderId}")
    public ResponseEntity<String> statusChange(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        System.out.println("수정할 항목의 아이디: " + orderId);
        System.out.println("변경하고자 하는 주문 상태: " + status);

        int affected = orderService.updateOrderStatus(orderId, status);
        System.out.println("데이터베이스 반영 행 개수: " + affected);

        return ResponseEntity.ok("송장 번호: " + orderId + "의 주문 상태가 " + status + "로 변경되었습니다.");
    }

    // ✅ 주문 취소
    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        if (!orderService.existsById(orderId)) {
            return ResponseEntity.notFound().build();
        }

        Optional<Order> orderOptional = orderService.findOrderById(orderId);
        if(orderOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Order order =orderOptional.get();

        // 주문 상품을 반복하면서 재고수량을 더해줌
        for (OrderProduct op : order.getOrderProducts()){
            Product product = op.getProduct();
            int quantity = op.getQuantity();

            // 기존 재고에 주문 취소된 수량을 다시 더해줌
            product.setStock(product.getStock()+quantity);

            productService.save(product); // 데이터 베이스에 수정
        }

        orderService.deleteById(orderId);

        return ResponseEntity.ok("주문이 취소되었습니다.");
    }
}
