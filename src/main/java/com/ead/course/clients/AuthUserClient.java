package com.ead.course.clients;

import com.ead.course.dtos.CourseUserRecordDto;
import com.ead.course.dtos.ResponsePageDto;
import com.ead.course.dtos.UserRecordDto;
import com.ead.course.exceptions.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

@Log4j2
@Component
public class AuthUserClient {

    @Value("${ead.api.url.authuser}")
    String baseUrlAuthUser;

    final RestClient restClient;

    public AuthUserClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public Page<UserRecordDto> getAllUsersByCourse(UUID courseId, Pageable pageable){
        String url = baseUrlAuthUser + "/users?courseId=" + courseId + "&page=" + pageable.getPageNumber() + "&size=" +
                pageable.getPageSize() + "&sort=" + pageable.getSort().toString()
                                                    .replaceAll(":", ",")
                                                    .replaceAll(" ", "");
        log.debug("Request URL: {}", url);

        try {
            return restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(new ParameterizedTypeReference<ResponsePageDto<UserRecordDto>>() {});

        }catch (RestClientException e){
            log.error("Erro request restclient with cause: {}", e.getMessage());
            throw new RuntimeException("Erro request restclient" + e);
        }
    }

    public ResponseEntity<UserRecordDto> getOneUserById(UUID userId){
        String url = baseUrlAuthUser + "/users/" + userId;
        log.debug("Request URL: {}", url);
        return restClient.get()
                .uri(url)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) -> {
                    log.error("Error: User not found: {}", userId);
                    throw new NotFoundException("User not found: " + userId);
                })
                .toEntity(UserRecordDto.class);
    }

    public void postSubscriptionUserInCourse(UUID courseId, UUID userId){
        String url = baseUrlAuthUser + "/users/" + userId + "/courses/subscription";
        log.debug("Request URL: {}", url);
        var courseUserRecordDto = new CourseUserRecordDto(courseId, userId);
        try{
            restClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(courseUserRecordDto)
                    .retrieve()
                    .toBodilessEntity();

        }catch (RestClientException e){
            log.error("Erro request POST restclient with cause: {}", e.getMessage());
            throw new RuntimeException("Erro request POST restclient" + e);
        }
    }

    public void deleteCourseUserInAuthUser(UUID courseId){
        String url = baseUrlAuthUser + "/users/courses/" + courseId;
        log.debug("Request URL: {} ", url);

        try{
            restClient.delete()
                    .uri(url)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e){
            log.error("Error Request DELETE RestClient with cause: {} ", e.getMessage());
            throw new RuntimeException("Error Request DELETE RestClient", e);
        }
    }




}
