package com.icia.rmate.controller;

import com.icia.rmate.dto.OrderDTO;
import com.icia.rmate.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpSession session; // Added autowiring for HttpSession

    @PostMapping(value = "/orderSave", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> saveOrder(@ModelAttribute OrderDTO orderDTO) {

        System.out.println(orderDTO + "orderDTO확인!");
        orderService.saveOrder(orderDTO);

        String script = "<script>"
                + "  window.opener.postMessage('paymentComplete', '*');"
                + "</script>";

        return ResponseEntity.status(HttpStatus.OK).body(script);
    }

    @GetMapping("/myOrdersList")
    public ResponseEntity<List<OrderDTO>> myOrdersList() {
        String loginId = (String) session.getAttribute("loginId"); // 세션에서 loginId 가져오기
        if (loginId == null || loginId.isEmpty()) { // 로그인 상태가 아니면 401 Unauthorized 상태 코드 반환
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<OrderDTO> myOrders = orderService.findMyOrders(loginId);
        System.out.println(myOrders + ": dto로 변환된 myOrders를 확인하라!");

        if (myOrders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(myOrders, HttpStatus.OK);
        }
    }


    @GetMapping("/receipt")
    public ModelAndView showReceipt(@RequestParam("merchantUid") String merchantUid,
                                    @RequestParam("fuelCost") String fuelCost) {

        ModelAndView mav = new ModelAndView();

        // merchantUid를 사용하여 Order 객체를 조회
        Optional<OrderDTO> orderOptional = orderService.findByMerchantUid(merchantUid);


        // Order 객체가 존재하면 모델에 추가, 없으면 errorMessage 추가
        if (orderOptional.isPresent()) {
            OrderDTO order = orderOptional.get();
            mav.addObject("order", order); // 전체 Order 객체를 추가
            mav.addObject("merchantUid", merchantUid); //기존의 merchantUid도 추가
            mav.addObject("fuelCost", fuelCost); //기존의 fuelCost도 추가
        } else {
            // Order 객체가 없으면 에러 메시지를 모델에 추가
            mav.addObject("errorMessage", "주문 정보를 찾을 수 없습니다.");
            mav.addObject("merchantUid", merchantUid); // 에러인 경우에도 merchantUid는 표시
            mav.addObject("fuelCost", fuelCost);     // 에러인 경우에도 fuelCost는 표시

        }

        mav.setViewName("receipt");
        return mav;
    }


}