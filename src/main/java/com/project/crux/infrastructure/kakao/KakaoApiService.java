package com.project.crux.infrastructure.kakao;

import com.project.crux.gym.domain.Gym;
import com.project.crux.infrastructure.kakao.response.KakaoResponse;
import com.project.crux.gym.repository.GymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoApiService {
    private final GymRepository gymRepository;

    private final String key = "1d7bd93e2ce7f01247d51b1be511048b";


    public void updateGymByKakaoApi(double start_x, double start_y, double end_x, double end_y) {

        String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=암벽등반&rect=";

        String rect = start_x + "," + start_y + "," + end_x + "," + end_y;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "KakaoAK " + key);
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        URI targetUrl = UriComponentsBuilder
                .fromUriString(url + rect) //기본 url
                .build()
                .encode(StandardCharsets.UTF_8) //인코딩
                .toUri();


        ResponseEntity<?> resultMap = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, KakaoResponse.class);
        KakaoResponse kakaoResponse = (KakaoResponse) resultMap.getBody();

        if (kakaoResponse.getMeta().getTotal_count() < 15) {

            kakaoResponse.getDocuments().forEach(document -> {

                Optional<Gym> gym = gymRepository.findByName(document.getPlace_name());

                if (!gym.isPresent()) {
                    gymRepository.save(new Gym(document.getPlace_name(), document.getAddress_name(), document.getPhone(),document.getX(), document.getY()));
                } else {
                    Gym updateGym = gym.get();
                    updateGym.update(document.getPlace_name(), document.getAddress_name(), document.getPhone(),document.getX(), document.getY());
                }
            });
        } else {
            updateGymByKakaoApi(start_x, start_y, (end_x + start_x) / 2, (end_y + start_y) / 2);
            updateGymByKakaoApi((end_x + start_x) / 2, start_y, end_x, (end_y + start_y) / 2);
            updateGymByKakaoApi(start_x, (end_y + start_y) / 2, (end_x + start_x) / 2, end_y);
            updateGymByKakaoApi((end_x + start_x) / 2, (end_y + start_y) / 2, end_x, end_y);
        }

    }

    public void updateGymImgByKakaoApi() {

        String url =  "https://dapi.kakao.com/v2/search/image?size=1&query=";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "KakaoAK " + key);
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);


        for (Gym gym : gymRepository.findAll()) {
            URI targetUrl = UriComponentsBuilder
                    .fromUriString(url + gym.getName()) //기본 url
                    .build()
                    .encode(StandardCharsets.UTF_8) //인코딩
                    .toUri();
            ResponseEntity<?> resultMap = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, KakaoResponse.class);
            KakaoResponse kakaoResponse = (KakaoResponse) resultMap.getBody();
            if (!kakaoResponse.getDocuments().isEmpty()) {
                gym.updateImg(kakaoResponse.getDocuments().get(0).getImage_url());
            }
        }
    }





}
