package com.icia.rmate.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icia.rmate.dao.CourseRepository;
import com.icia.rmate.dao.NodeDao;
import com.icia.rmate.dto.CourseEntity;
import com.icia.rmate.dto.Node;
import com.icia.rmate.dto.NodeCost;
import com.icia.rmate.parameter.NodeCostParam;
import com.icia.rmate.service.NodeCostService;
import com.icia.rmate.service.NodeService;
import com.icia.rmate.util.JsonResult;
import com.icia.rmate.util.KakaoApiUtil;
import com.icia.rmate.util.KakaoApiUtil.Point;
import com.icia.rmate.util.kakao.KakaoDirections;
import com.icia.rmate.util.kakao.KakaoDirections.Route;
import com.icia.rmate.util.kakao.KakaoDirections.Route.Section;
import com.icia.rmate.util.kakao.KakaoDirections.Route.Section.Road;
import com.icia.rmate.util.kakao.KakaoDirections.Route.Summary;
import com.icia.rmate.util.kakao.KakaoDirections.Route.Summary.Fare;
import com.icia.rmate.vrp.VrpResult;
import com.icia.rmate.vrp.VrpService;
import com.icia.rmate.vrp.VrpVehicleRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Controller
public class MapController {

  @Autowired
  private NodeService nodeService;
  @Autowired
  private NodeCostService nodeCostService;
  @Autowired
  private CourseRepository course;
    @Autowired
    private NodeDao nodeDao;

  @GetMapping("/map")
  public String getMain() {
    return "map";
  }


  @GetMapping("/cpoi")
  @ResponseBody
  public JsonResult processCpoi(@RequestParam int bNum) throws IOException, InterruptedException {
    System.out.println(bNum+"bnum 확인 ");
    List<CourseEntity> courseList = course.getCourseListByBNum(bNum);

    List<Point> pointList = new ArrayList<>();
    List<Node> nodeList = new ArrayList<>();
    if (courseList != null) {
      for (CourseEntity course : courseList) {
        Point point = new Point();
        point.setId(String.valueOf(course.getId()));
        point.setName(course.getName());
        point.setPhone(course.getPhone());
        point.setRoadAddress(course.getAddress());
        point.setJibunAddress(course.getAddress());
        point.setX(course.getX());
        point.setY(course.getY());
        pointList.add(point);
      }
    }


    for (Point point : pointList) {
      Node node = nodeService.getOne(Long.valueOf(point.getId()));
      if (node == null) {
        node = new Node();
        node.setId(Long.valueOf(point.getId()));// 노드id
        node.setName(point.getName());
        node.setPhone(point.getPhone());// 전화번호
        // 주소 설정 로직 수정
        if (point.getRoadAddress() != null && !point.getRoadAddress().isEmpty()) {
          node.setAddress(point.getRoadAddress()); // 도로명 주소 사용
        } else if (point.getJibunAddress() != null && !point.getJibunAddress().isEmpty()) {
          node.setAddress(point.getJibunAddress()); // 지번 주소 사용
        } else {
          node.setAddress(null); // 또는 null (상황에 맞게)
        }
        node.setX(point.getX());// 경도
        node.setY(point.getY());// 위도
        node.setRegDt(new Date());// 등록일시
        node.setModDt(new Date());// 수정일시
        nodeService.add(node);
      }
      nodeList.add(node);
    }

    int totalDistance = 0;
    int totalDuration = 0;
    List<Point> totalPathPointList = new ArrayList<>();
    for (int i = 1; i < nodeList.size(); i++) {
      Node prev = nodeList.get(i - 1);
      Node next = nodeList.get(i);

      NodeCost nodeCost = getNodeCost(prev, next);
      if (nodeCost == null) {
        System.out.println("노트코스트가없습니다.");
        continue;
      }
      totalDistance += nodeCost.getDistanceMeter();
      totalDuration += nodeCost.getDurationSecond();
      try {
        totalPathPointList.addAll(new ObjectMapper().readValue(nodeCost.getPathJson(), new TypeReference<List<Point>>() {
        }));
      }catch (Exception e){
        System.out.println("파싱에러" + e);
      }
    }
    JsonResult jsonResult = new JsonResult();
    jsonResult.addData("totalDistance", totalDistance);// 전체이동거리
    jsonResult.addData("totalDuration", totalDuration);// 전체이동시간
    jsonResult.addData("totalPathPointList", totalPathPointList);// 전체이동경로
    jsonResult.addData("nodeList", nodeList);// 방문지목록
    return jsonResult.success();
  }
  @GetMapping("/getDpoi")
  @ResponseBody
  public JsonResult getDpoi( @RequestParam String keyword, @RequestParam double x, @RequestParam double y) throws IOException, InterruptedException {

    Point center = new Point(x, y);// 중심좌표

    List<Point> pointList = KakaoApiUtil.getPointByKeyword(keyword,center);


    if (pointList != null && !pointList.isEmpty()) { // pointList가 null이 아니고 비어있지 않은지 확인
      System.out.println(pointList+"포인트 리스트값 확인");
      JsonResult jsonResult = new JsonResult().success();
      jsonResult.addData("pointList", pointList); // pointList를 "pointList" 키로 담아서 success 응답
      return jsonResult;
    } else {
      return null;
    }
  }

  @GetMapping("/poi")
  @ResponseBody
  public JsonResult getPoi(@RequestParam String keyword, @RequestParam double x, @RequestParam double y) throws IOException, InterruptedException {
    Point center = new Point(x, y);// 중심좌표
    List<Point> pointList = KakaoApiUtil.getPointByKeyword(keyword, center);
    System.out.println("pointList (Kakao API 호출 결과): " + pointList); // Kakao API 호출 결과 pointList 출력


    List<Node> nodeList = new ArrayList<>();
    System.out.println("pointList 순회 시작:"); // pointList 순회 시작 로그
    for (Point point : pointList) {
      System.out.println("현재 point (Kakao API Point): " + point); // 현재 Kakao API Point 출력
      Node node = nodeService.getOne(Long.valueOf(point.getId()));
      System.out.println("nodeService.getOne() 결과 (DB Node): " + node); // DB 조회 결과 Node 출력
      if (node == null) {
        node = new Node();
        node.setId(Long.valueOf(point.getId()));// 노드id
        node.setName(point.getName());
        node.setPhone(point.getPhone());// 전화번호
        // 주소 설정 로직 수정
        if (point.getRoadAddress() != null && !point.getRoadAddress().isEmpty()) {
          node.setAddress(point.getRoadAddress()); // 도로명 주소 사용
        } else if (point.getJibunAddress() != null && !point.getJibunAddress().isEmpty()) {
          node.setAddress(point.getJibunAddress()); // 지번 주소 사용
        } else {
          node.setAddress(null); // 또는 null (상황에 맞게)
        }
        node.setX(point.getX());// 경도
        node.setY(point.getY());// 위도
        node.setRegDt(new Date());// 등록일시
        node.setModDt(new Date());// 수정일시
        nodeService.add(node);
        System.out.println("새로운 Node 추가됨 (DB 저장): " + node); // 새로 추가된 Node (DB 저장 후) 출력
      } else {
        System.out.println("기존 Node 사용 (DB에서 가져옴): " + node); // 기존 Node 사용 로그
      }
      nodeList.add(node);
      System.out.println("nodeList에 추가된 Node (현재 nodeList 상태): " + node + ", nodeList: " + nodeList); // nodeList에 추가 후 상태 로그
    }
    System.out.println("pointList 순회 완료"); // pointList 순회 완료 로그


    int totalDistance = 0;
    int totalDuration = 0;
    List<Point> totalPathPointList = new ArrayList<>();
    System.out.println("nodeList 기반 경로 계산 시작:"); // nodeList 기반 경로 계산 시작 로그
    for (int i = 1; i < nodeList.size(); i++) {
      Node prev = nodeList.get(i - 1);
      Node next = nodeList.get(i);
      System.out.println("경로 계산 중 - 출발지 Node: " + prev + ", 목적지 Node: " + next); // 경로 계산 중 출발/목적지 Node 로그

      NodeCost nodeCost = getNodeCost(prev, next);
      System.out.println("getNodeCost() 결과 (NodeCost): " + nodeCost); // getNodeCost() 결과 NodeCost 출력
      if (nodeCost == null) {
        System.out.println("노드 코스트가 없습니다."); // 노드 코스트 없음 로그
        continue;
      }

      totalDistance += nodeCost.getDistanceMeter();
      totalDuration += nodeCost.getDurationSecond();
      List<Point> pathPoints = new ObjectMapper().readValue(nodeCost.getPathJson(), new TypeReference<List<Point>>() {});
      totalPathPointList.addAll(pathPoints);
      System.out.println("경로 포인트 추가됨 (현재 totalPathPointList 크기): " + pathPoints.size() + ", totalPathPointList 크기: " + totalPathPointList.size()); // 경로 포인트 추가 로그
    }
    System.out.println("nodeList 기반 경로 계산 완료"); // nodeList 기반 경로 계산 완료 로그

    JsonResult jsonResult = new JsonResult();
    jsonResult.addData("totalDistance", totalDistance);// 전체이동거리
    jsonResult.addData("totalDuration", totalDuration);// 전체이동시간
    jsonResult.addData("totalPathPointList", totalPathPointList);// 전체이동경로
    jsonResult.addData("nodeList", nodeList);// 방문지목록
    System.out.println("JsonResult 반환 직전 nodeList 상태: " + nodeList); // JsonResult 반환 직전 nodeList 최종 상태 로그
    return jsonResult;
  }




  private NodeCost getNodeCost(Node prev, Node next) throws IOException, InterruptedException {
    NodeCostParam nodeCostParam = new NodeCostParam();
    nodeCostParam.setStartNodeId(prev.getId());
    nodeCostParam.setEndNodeId(next.getId());
    NodeCost nodeCost = nodeCostService.getOneByParam(nodeCostParam);

    if (nodeCost == null) {
      KakaoDirections kakaoDirections = KakaoApiUtil.getKakaoDirections(new Point(prev.getX(), prev.getY()),
              new Point(next.getX(), next.getY()));
      List<Route> routes = kakaoDirections.getRoutes();
      Route route = routes.get(0);
      List<Point> pathPointList = new ArrayList<Point>();
      List<Section> sections = route.getSections();

      if (sections == null) {
        // {"trans_id":"018e3d7f7526771d9332cb717909be8f","routes":[{"result_code":104,"result_msg":"출발지와
        // 도착지가 5 m 이내로 설정된 경우 경로를 탐색할 수 없음"}]}
        pathPointList.add(new Point(prev.getX(), prev.getY()));
        pathPointList.add(new Point(next.getX(), next.getY()));
        nodeCost = new NodeCost();
        nodeCost.setStartNodeId(prev.getId());// 시작노드id
        nodeCost.setEndNodeId(next.getId());// 종료노드id
        nodeCost.setDistanceMeter(0l);// 이동거리(미터)
        nodeCost.setDurationSecond(0l);// 이동시간(초)
        nodeCost.setTollFare(0);// 통행 요금(톨게이트)
        nodeCost.setTaxiFare(0);// 택시 요금(지자체별, 심야, 시경계, 복합, 콜비 감안)
        nodeCost.setPathJson(new ObjectMapper().writeValueAsString(pathPointList));// 이동경로json [[x,y],[x,y]]
        nodeCost.setRegDt(new Date());// 등록일시
        nodeCost.setModDt(new Date());// 수정일시
        nodeCostService.saveNodeCost(nodeCost);
        return null;
      }
      List<Road> roads = sections.get(0).getRoads();
      for (Road road : roads) {
        List<Double> vertexes = road.getVertexes();
        for (int q = 0; q < vertexes.size(); q++) {
          pathPointList.add(new Point(vertexes.get(q), vertexes.get(++q)));
        }
      }
      Summary summary = route.getSummary();
      Integer distance = summary.getDistance();
      Integer duration = summary.getDuration();
      Fare fare = summary.getFare();
      Integer taxi = fare.getTaxi();
      Integer toll = fare.getToll();

      nodeCost = new NodeCost();
      nodeCost.setStartNodeId(prev.getId());// 시작노드id
      nodeCost.setEndNodeId(next.getId());// 종료노드id
      nodeCost.setDistanceMeter(distance.longValue());// 이동거리(미터)
      nodeCost.setDurationSecond(duration.longValue());// 이동시간(초)
      nodeCost.setTollFare(toll);// 통행 요금(톨게이트)
      nodeCost.setTaxiFare(taxi);// 택시 요금(지자체별, 심야, 시경계, 복합, 콜비 감안)
      nodeCost.setPathJson(new ObjectMapper().writeValueAsString(pathPointList));// 이동경로json [[x,y],[x,y]]
      nodeCost.setRegDt(new Date());// 등록일시
      nodeCost.setModDt(new Date());// 수정일시
      nodeCostService.saveNodeCost(nodeCost);
    }
    return nodeCost;
  }


  @PostMapping("/vrp")
  @ResponseBody
  public JsonResult postVrp(@RequestBody List<Node> nodeList) throws IOException, InterruptedException {
    VrpService vrpService = new VrpService();
    Node firstNode = nodeList.get(0);
    System.out.println(firstNode.getId()+firstNode.getName() +"첫번째노드인지 확인");
    String firstNodeId = String.valueOf(firstNode.getId());
    // 차량 등록
    vrpService.addVehicle("차량01", firstNodeId);

    Map<String, Node> nodeMap = new HashMap<>();
    Map<String, Map<String, NodeCost>> nodeCostMap = new HashMap<>();

    for (Node node : nodeList) {
      String nodeId = String.valueOf(node.getId());
      // 화물 등록
      vrpService.addShipement(node.getName(), firstNodeId, nodeId);
      nodeMap.put(nodeId, node);
    }

    for (int i = 0; i < nodeList.size(); i++) {
      Node startNode = nodeList.get(i);
      for (int j = 0; j < nodeList.size(); j++) {
        Node endNode = nodeList.get(j);
        NodeCost nodeCost = getNodeCost(startNode, endNode);
        if (i == j) {
          continue;
        }
        if (nodeCost == null) {
          nodeCost = new NodeCost();
          nodeCost.setDistanceMeter(0l);
          nodeCost.setDurationSecond(0l);
        }
        Long distanceMeter = nodeCost.getDistanceMeter();
        Long durationSecond = nodeCost.getDurationSecond();
        String startNodeId = String.valueOf(startNode.getId());
        String endNodeId = String.valueOf(endNode.getId());

        // 비용 등록
        vrpService.addCost(startNodeId, endNodeId, durationSecond, distanceMeter);
        if (!nodeCostMap.containsKey(startNodeId)) {
          nodeCostMap.put(startNodeId, new HashMap<>());
        }
        nodeCostMap.get(startNodeId).put(endNodeId, nodeCost);
      }
    }

    List<Node> vrpNodeList = new ArrayList<>();

    VrpResult vrpResult = vrpService.getVrpResult();

    String prevLocationId = null;
    for (VrpVehicleRoute vrpVehicleRoute : vrpResult.getVrpVehicleRouteList()) {
      System.out.println(vrpVehicleRoute);
//    모든 약을 시작점에서 픽업 한 경우만 정상 동작 하는 코드.
//      if ("deliverShipment".equals(vrpVehicleRoute.getActivityName())) {
//        String locationId = vrpVehicleRoute.getLocationId();
//        vrpNodeList.add(nodeMap.get(locationId));
//      }

      // 수정 된 코드
      // 시작 약국에서 픽업 몇개하고 배송 후 다시 픽업해도 되는 코드
      String locationId = vrpVehicleRoute.getLocationId();
      if (prevLocationId == null) {
        prevLocationId = locationId;
      } else if (locationId.equals(prevLocationId)) {
        continue;
      }

      prevLocationId = locationId;
      vrpNodeList.add(nodeMap.get(locationId));

    }

    int totalDistance = 0;
    int totalDuration = 0;
    List<Point> totalPathPointList = new ArrayList<>();
    for (int i = 1; i < vrpNodeList.size(); i++) {
      Node prev = vrpNodeList.get(i - 1);
      Node next = vrpNodeList.get(i);

      NodeCost nodeCost = nodeCostMap.get(String.valueOf(prev.getId())).get(String.valueOf(next.getId()));
      if (nodeCost == null) {
        continue;
      }

      totalDistance += nodeCost.getDistanceMeter();
      totalDuration += nodeCost.getDurationSecond();
      String pathJson = nodeCost.getPathJson();
      if (pathJson != null) {
        totalPathPointList.addAll(new ObjectMapper().readValue(pathJson, new TypeReference<List<Point>>() {
        }));
      }
    }

    JsonResult jsonResult = new JsonResult();
    jsonResult.addData("totalDistance", totalDistance);// 전체이동거리
    jsonResult.addData("totalDuration", totalDuration);// 전체이동시간
    jsonResult.addData("totalPathPointList", totalPathPointList);// 전체이동경로
    jsonResult.addData("nodeList", vrpNodeList);// 방문지목록
    return jsonResult;
  }


  @DeleteMapping("/deleteNode/{nodeId}")
  public ResponseEntity<String> deleteNode(@PathVariable Long nodeId) {
    try {
      System.out.println(nodeId + "노드아이디 확인");
      nodeDao.deleteById(nodeId);
      course.deletebynodeId(nodeId);
      return new ResponseEntity<>("삭제 성공", HttpStatus.OK);
    } catch (Exception e) {
      System.out.println("에러발생"+e.getMessage());
      return new ResponseEntity<>("삭제 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}