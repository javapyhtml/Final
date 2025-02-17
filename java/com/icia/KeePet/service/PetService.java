package com.icia.rmate.service;

import com.icia.rmate.dao.PetRepository;
import com.icia.rmate.dto.PetDTO;
import com.icia.rmate.dto.PetEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository prepo;

    private ModelAndView mav;

    // 반려견 등록 메소드
    public boolean registerPet(PetDTO petDTO) {
        try {
            // DTO를 Entity로 변환
            PetEntity petEntity = PetEntity.toEntity(petDTO);

            // 데이터베이스에 반려견 정보 저장
            prepo.save(petEntity);

            return true;  // 저장 성공 시 true 반환
        } catch (Exception e) {
            // 예외 발생 시 false 반환
            e.printStackTrace();
            return false;
        }
    }


    // 사용자 ID로 반려견 정보 가져오기
    public List<PetDTO> getPetInfo(String userId) {
        List<PetEntity> entityList = prepo.findByUserId(userId);  // 사용자 ID로 반려견 정보 조회
        if (entityList.isEmpty()) {
            return Collections.emptyList();  // 반려견 정보가 없다면 빈 리스트 반환
        }

        // PetEntity들을 PetDTO로 변환하여 반환
        return entityList.stream()
                .map(PetDTO::toDTO)  // DTO 변환 메서드 호출
                .collect(Collectors.toList());
    }



    public List<PetDTO> getPetsByUserId(String userId) {
        // 사용자 ID로 반려견 정보를 조회
        List<PetEntity> entityList = prepo.findByUserId(userId);  // 예: petRepository는 DB에서 데이터를 가져오는 Repository

//        if (entityList.isEmpty()) {
//            return Collections.emptyList();  // 반려견 정보가 없다면 빈 리스트 반환
//        }

        // PetEntity들을 PetDTO로 변환하여 반환
        return entityList.stream()
                .map(PetDTO::toDTO)  // PetEntity를 PetDTO로 변환
                .collect(Collectors.toList());  // 변환된 DTO 리스트 반환
    }

    public PetEntity getPetNameByPetNum(int petNum) {
        return prepo.findById(petNum).orElse(null);
    }

    public PetDTO getPetByPetNum(int petNum) {
        PetEntity petEntity = prepo.findById(petNum)
                .orElseThrow(() -> new IllegalArgumentException("해당 강아지가 존재하지 않습니다."));
        return PetDTO.toDTO(petEntity);
    }
}
