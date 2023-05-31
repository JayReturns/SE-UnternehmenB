package com.dhbw.unternehmenb.ssp.view;

import com.dhbw.unternehmenb.ssp.model.response.ProvisioningResponse;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class ProvisioningRepository {

    private final Logger LOGGER = LoggerFactory.getLogger(ProvisioningRepository.class);

    @Data
    private static class APIinput{
        private String id;
        private String type;

        public APIinput(UUID uuid, String environmentType){
            super();
            this.id = uuid.toString();
            this.type = environmentType;
        }
    }

    public ProvisioningResponse getTechnicalProvisioning(UUID id, String environmentType){
        WebClient client = WebClient.create("https://provisioning-backend.azurewebsites.net/api/v1/virtualenvironments/verification");
        APIinput requestbody = new APIinput(id, environmentType);
        return client.post()
                .uri("")
                .headers(httpHeaders -> {
                    httpHeaders.set("token", "UnternehmenB");
                    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                })
                .body(BodyInserters.fromValue(requestbody))
                .retrieve()
                .onStatus(
                        HttpStatus.BAD_REQUEST::equals,
                        clientResponse ->
                            clientResponse.bodyToMono(String.class).map(Exception::new)
                )
                .bodyToMono(ProvisioningResponse.class)
                .block();
    }
}
