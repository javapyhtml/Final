package com.icia.rmate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icia.rmate.util.KakaoApiUtil1;
import com.icia.rmate.util.KakaoApiUtil1.Point;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
public class testController {

    /**
     * 주소를 좌표로 변환
     *
     * @param address 주소정보
     * @param model html파일에 값을 전달해주는 객체
     * @return html 파일위치
     *
     */
    @GetMapping("/map/address/point")
    public String getMapAddressPoint(@RequestParam(required = false) String address, Model model) throws IOException, InterruptedException {
        if(address != null && !address.isEmpty()){
            Point point = KakaoApiUtil1.getPointByAddress(address);
            model.addAttribute("point", point);
        }
        return "map/address_point";
    }

    /**
     *
     * 출발지와 목적지를 지도상에 표시하기
     *
     * @param fromAddress 출발지 주소정보
     * @param toAddress 목적지 주소정보
     * @param model html파일에 값을 전달해주는 객체
     * @return html 파일위치
     *
     */
    @GetMapping("/map/marker")
    public String getMarker(@RequestParam(required = false)String fromAddress,
                            @RequestParam(required = false)String toAddress,
                            Model model) throws IOException, InterruptedException {
        if(fromAddress != null && !fromAddress.isEmpty()){
            System.out.println("fromAddress1 : " + fromAddress);
            Point fromPoint = KakaoApiUtil1.getPointByAddress(fromAddress);
            model.addAttribute("fromPoint", fromPoint);
        }

        if(toAddress != null && !toAddress.isEmpty()){
            Point toPoint = KakaoApiUtil1.getPointByAddress(toAddress);
            model.addAttribute("toPoint", toPoint);
        }

        return "map/marker";
    }

    /**
     *
     * 자동차 이동 경로 그리기
     *
     * @param fromAddress 출발지 주소정보
     * @param toAddress 목적지 주소정보
     * @param model html파일에 값을 전달해주는 객체
     * @return html 파일위치
     *
     */

    @GetMapping("/map/paths")
    public String getMapPaths(@RequestParam(required = false)String fromAddress,
                            @RequestParam(required = false)String toAddress,
                            Model model) throws IOException, InterruptedException {

        Point fromPoint = null;
        Point toPoint = null;

        if(fromAddress != null && !fromAddress.isEmpty()){
            System.out.println("fromAddress1 : " + fromAddress);
            fromPoint = KakaoApiUtil1.getPointByAddress(fromAddress);
            model.addAttribute("fromPoint", fromPoint);
        }

        if(toAddress != null && !toAddress.isEmpty()){
            toPoint = KakaoApiUtil1.getPointByAddress(toAddress);
            model.addAttribute("toPoint", toPoint);
        }

        if(fromPoint != null && toPoint != null){
            List<Point> pointList = KakaoApiUtil1.getVehiclePaths(fromPoint, toPoint);
            String pointListJson = new ObjectMapper().writer().writeValueAsString(pointList);
            model.addAttribute("pointList", pointListJson);
        }

        return "map/paths";
    }

    /**
     *  키워드를 통해 마커 표시
     *
     * @param x             중심지 x좌표
     * @param y             중심지 y좌표
     * @param keyword       검색 키워드
     * @param model         html 파일에 값을 전달해주는 객체
     * @return              html 파일 위치
     *
     */

    @GetMapping("/map/keyword")
    public String getMapKeyword(@RequestParam(required = false) Double x,
                                @RequestParam(required = false) Double y,
                                @RequestParam(required = false) String keyword,
                                Model model) throws IOException, InterruptedException{
        if(x != null && y != null & keyword != null){
            List<Point> pointList = KakaoApiUtil1.getPointByKeyword(keyword, new Point(x, y));
            String pointListJson = new ObjectMapper().writer().writeValueAsString(pointList);
            model.addAttribute("pointList", pointListJson);
        }

        return "map/keyword";
    }

    @GetMapping("/map/keyword/path")
    public String getMapKeywordPath(@RequestParam(required = false) Double x,
                                @RequestParam(required = false) Double y,
                                @RequestParam(required = false) String keyword,
                                Model model) throws IOException, InterruptedException{
        if(x != null && y != null & keyword != null){
            List<Point> pointList = KakaoApiUtil1.getPointByKeyword(keyword, new Point(x, y));
            String pointListJson = new ObjectMapper().writer().writeValueAsString(pointList);
            model.addAttribute("pointList", pointListJson);
        }

        return "map/keyword";
    }

    @GetMapping("/test")
    public String test1(@RequestParam(required = false)String fromAddress,
                              @RequestParam(required = false)String toAddress,
                              Model model) throws IOException, InterruptedException {

        Point fromPoint = null;
        Point toPoint = null;

        if(fromAddress != null && !fromAddress.isEmpty()){
            System.out.println("fromAddress1 : " + fromAddress);
            fromPoint = KakaoApiUtil1.getPointByAddress(fromAddress);
            model.addAttribute("fromPoint", fromPoint);
        }

        if(toAddress != null && !toAddress.isEmpty()){
            toPoint = KakaoApiUtil1.getPointByAddress(toAddress);
            model.addAttribute("toPoint", toPoint);
        }

        if(fromPoint != null && toPoint != null){
            List<Point> pointList = KakaoApiUtil1.getVehiclePaths(fromPoint, toPoint);
            String pointListJson = new ObjectMapper().writer().writeValueAsString(pointList);
            model.addAttribute("pointList", pointListJson);
        }

        return "test";
    }

    @PostMapping("/pointAjax")
    public @ResponseBody List<Point> getVehiclePaths(@RequestParam(required = false)Double fromX,
                                                     @RequestParam(required = false)Double fromY,
                                                     @RequestParam(required = false)Double toX,
                                                     @RequestParam(required = false)Double toY)throws IOException, InterruptedException{

        List<Point> pointList = KakaoApiUtil1.getVehiclePaths(fromX, fromY, toX, toY);

        return pointList;
    }

}
