package com.coffee.coffee.controller;

import com.coffee.coffee.entity.Element;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ElementController {
    @GetMapping("/element")
    public Element test(){
        Element bean = new Element();
        bean.setId(1);
        bean.setName("프렌치 바게트");
        bean.setPrice(1000);
        bean.setCategory("bread");
        bean.setStock(111);
        bean.setImage("french_baguette_01.png");
        bean.setDescription("프랑스의 대표적인 빵 중 하나로, 길쭉하고 얇은 형태의 식빵입니다. 바삭하면서도 촉촉한 식감과 진한 맛이 특징입니다.");
        return bean;
    }

    @GetMapping("/element/list")
    public List<Element> test02 (){
        List<Element> elementList = new ArrayList<>();
        elementList.add(new Element(2,"크로아상",1000,"bread",222,"croissant_02.png","프랑스의 대표적인 베이커리 중 하나로, 층층이 쌓인 반죽에 버터를 추가하여 구워낸 과자입니다."));
        return elementList;
    }
}


