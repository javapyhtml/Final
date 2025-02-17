package com.icia.rmate.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icia.rmate.util.kakao.KakaoAddress;
import com.icia.rmate.util.kakao.Document;
import com.icia.rmate.util.kakao.KakaoDirections;
import com.icia.rmate.util.kakao.KakaoDirections.Route.Section.Road;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class KakaoApiUtil1 {
    private static final String REST_API_KEY = "9f22b4f3e111e6d39801517778882963";

    /**
     * 주소 -> 좌표 변환
     *
     * @param address 주소
     * @return 좌표
     */

    public static Point getPointByAddress(String address) throws IOException, InterruptedException {

        System.out.println("address : " + address );
        HttpClient client = HttpClient.newHttpClient();
        String url = "https://dapi.kakao.com/v2/local/search/address.json";
        url += "?query=" + URLEncoder.encode(address, "UTF-8");
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "KakaoAK " + REST_API_KEY)
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        System.out.println(responseBody);

        KakaoAddress kakaoAddress = new ObjectMapper().readValue(responseBody, KakaoAddress.class);
        List<Document> documents = kakaoAddress.getDocuments();

        if(documents.isEmpty()){
            return null;
        }
        Document document = documents.get(0);
        return new Point(document.getX(), document.getY());
    }

    /**
     * 자동차 길찾기
     *
     * @param from 출발지
     * @param to   도착지
     * @return 이동 좌표 목록
     * @throws InterruptedException
     * @throws IOException
     */

    public static List<Point> getVehiclePaths(Point from, Point to) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String url = "https://apis-navi.kakaomobility.com/v1/directions";
        url += "?origin=" + from.getX() + "," + from.getY();
        url += "&destination=" + to.getX() + "," + to.getY();
        HttpRequest request = HttpRequest.newBuilder()//
                .header("Authorization", "KakaoAK " + REST_API_KEY)//
                .header("Content-Type", "application/json")//
                .uri(URI.create(url))//
                .GET()//
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        List<Point> pointList = new ArrayList<Point>();

        KakaoDirections kakaoDirections = new ObjectMapper().readValue(responseBody, KakaoDirections.class);
        List<Road> roads = kakaoDirections.getRoutes().get(0).getSections().get(0).getRoads();
        for (Road road : roads) {
            List<Double> vertexes = road.getVertexes();
            for (int i = 0; i < vertexes.size(); i++) {
                pointList.add(new Point(vertexes.get(i), vertexes.get(++i)));
            }
        }

        return pointList;

    }

    public static List<Point> getVehiclePaths(Double fromX, Double fromY, double toX, double toY) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String url = "https://apis-navi.kakaomobility.com/v1/directions";
        url += "?origin=" + fromX + "," + fromY;
        url += "&destination=" + toX + "," + toY;
        HttpRequest request = HttpRequest.newBuilder()//
                .header("Authorization", "KakaoAK " + REST_API_KEY)//
                .header("Content-Type", "application/json")//
                .uri(URI.create(url))//
                .GET()//
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        List<Point> pointList = new ArrayList<Point>();

        KakaoDirections kakaoDirections = new ObjectMapper().readValue(responseBody, KakaoDirections.class);
        List<Road> roads = kakaoDirections.getRoutes().get(0).getSections().get(0).getRoads();
        for (Road road : roads) {
            List<Double> vertexes = road.getVertexes();
            for (int i = 0; i < vertexes.size(); i++) {
                pointList.add(new Point(vertexes.get(i), vertexes.get(++i)));
            }
        }

        return pointList;

    }

    /**
     * 키워드 장소 검색
     *
     * @param keyword       검색할 키워드
     * @param center        기준되는 주소의 좌표
     * @return 좌표
     */
    public static List<Point> getPointByKeyword(String keyword, Point center) throws IOException, InterruptedException {
        List<Point> pointList = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json";
        url += "?query=" + URLEncoder.encode(keyword, "UTF-8");
        url += "&category_group_code=PM9";
        url += "&x=" + center.getX();
        url += "&y=" + center.getY();

        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "KakaoAK " + REST_API_KEY)
                .uri(URI.create(url))
                .GET()
                .build();

        System.out.println(request.headers());

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        System.out.println(responseBody);

        KakaoAddress kakaoAddress = new ObjectMapper().readValue(responseBody, KakaoAddress.class);
        List<Document> documents = kakaoAddress.getDocuments();
        if(documents.isEmpty()){
            return null;
        }

        for(Document document : documents){
            Point point = new Point(document.getX(), document.getY());
            point.setName(document.getPlaceName());
            point.setPhone(document.getPhone());

            pointList.add(point);
        }

        return pointList;
    }

    public static class Point {
        private Double x;
        private Double y;
        private String name;
        private String phone;

        public Point(Double x, Double y) {
            this.x = x;
            this.y = y;
        }

        public Double getX() {
            return x;
        }

        public Double getY() {
            return y;
        }

        public String getName(){
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

}
