package com.cos.photogramstart.service;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional(readOnly = true)
    public List<Image> 인기사진() {
        return imageRepository.mPopular();
    }

    @Transactional(readOnly = true)
    public Page<Image> 이미지스토리(int principalId, Pageable pageable){
        Page<Image> images = imageRepository.mStory(principalId,pageable);
       //2(cos) 로그인
        //images에 좋아요 상태 담기

        images.forEach((image) -> {
            
            //좋아요 카운트 담기
            image.setLikeCount(image.getLikes().size());
            
                image.getLikes().forEach((like) -> {
                
                if (like.getUser().getId() == principalId) { //해당 이미지에 좋아요한 사람들을 찾아서 현재 로그인 한 사람이 좋아요 한 것인지 비교
                    image.setLikeState(true);
                }
            });
        });
        return images;
    }

    @Value("${file.path}") // apl.yml 파일 경로 가져오기
    private String uploadFolder;

    @Transactional
    public void 사진업로드(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails){
        UUID uuid = UUID.randomUUID();// 범용 유니크 식별자 - 중복 안됨
        String imageFileName = uuid+"_"+imageUploadDto.getFile().getOriginalFilename(); // 1.jpg
        System.out.println("이미지 파일이름: "+imageFileName);

        Path imageFilePath = Paths.get(uploadFolder+imageFileName);

        // 통신, I/O -> 예외가 발생할 수 있따.
        try {
            Files.write(imageFilePath,imageUploadDto.getFile().getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }

        // image 테이블에 저장
        Image image = imageUploadDto.toEntity(imageFileName,principalDetails.getUser());
            imageRepository.save(image);

//        System.out.println(imageEntity);
    }
}
