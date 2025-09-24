package com.coffee.coffee.controller;

import com.coffee.coffee.entity.Product;
import com.coffee.coffee.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService ;

    @GetMapping("/list") // 상품 목록을 List 컬렉션으로 반환해 줍니다.
    public List<Product> list(){
        List<Product> products = this.productService.getProductList();

        return products;
    }

    // 클라이언트가 특정 상품 id에 대하여 삭제요청을 함
    // @PathVariable은 URL의 경로 변수를 메소드의 매개 변수로 값을 전달해줌
    @DeleteMapping("/delete/{id}") // {id}는 경로 변수라고함, 가변 매개 변수
    public ResponseEntity<String> delete(@PathVariable Long id){ // {id}로 넘겨온 상품의 아이디가 변수 id에 할당됨
        try{
            boolean isDeleted = this.productService.deleteProduct(id);

            if (isDeleted){
                return ResponseEntity.ok(id +"번 상품이 삭제되었습니다.");
            }else {
                return ResponseEntity.badRequest().body(id+"번 상품이 존재하지 않습니다.");
            }

        }catch (Exception err){
            return ResponseEntity.internalServerError().body("오류 발생:"+ err.getMessage());
        }
    }
}
