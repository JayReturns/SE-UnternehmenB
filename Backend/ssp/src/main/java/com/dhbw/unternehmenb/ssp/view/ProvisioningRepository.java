package com.dhbw.unternehmenb.ssp.view;

import com.dhbw.unternehmenb.ssp.model.response.ProvisioningResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Repository
public class ProvisioningRepository {

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

    @Value("${provisioning.url}")
    private String provisioningUrl;

    @Value("${provisioning.token}")
    private String provisioningToken;

    public ProvisioningResponse getTechnicalProvisioning(UUID id, String environmentType){
        WebClient client = WebClient.create(provisioningUrl);
        APIinput requestbody = new APIinput(id, environmentType);
        return client.post()
                .uri("")
                .headers(httpHeaders -> {
                    httpHeaders.set("token", provisioningToken);
                    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                })
                .body(BodyInserters.fromValue(requestbody))
                .retrieve()
                .bodyToMono(ProvisioningResponse.class)
                .block();
    }
}
