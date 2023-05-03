package com.dhbw.unternehmenb.ssp.repositories;

import com.dhbw.unternehmenb.ssp.models.JwtToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Base64;

@Repository
public class Authenticater {

    Logger logger = LoggerFactory.getLogger(Authenticater.class);

    @Autowired
    UserRepository userRepository;

        public boolean authenticate(String jwtToken){
            String[] chunks = jwtToken.split("\\.");
            if(chunks.length != 3){
                return false;
            }
            try {
                JwtToken token = decode(chunks);
                if(token == null){
                    return false;
                }
                String userId = token.getUserId();
                if(userId == null){
                    return false;
                }
                if(userRepository.findById(userId).isEmpty()){
                    return false;
                }
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage());
                return false;
            }
            return true;
        }

        private JwtToken decode(String[] chunks) throws JsonProcessingException {
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String payload = new String(decoder.decode(chunks[1]));
            ObjectMapper obejectMapper = new ObjectMapper();

            return obejectMapper.readValue(payload, JwtToken.class);
        }
}
