package com.dhbw.unternehmenb.ssp.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.logging.Logger;

@Configuration
public class FirebaseAuthConfig {

    private Logger logger = Logger.getLogger(FirebaseAuthConfig.class.getName());

    @Value("classpath:firebase/serviceAccountKey.json")
    Resource serviceAccount;

    @Bean
    FirebaseAuth firebaseAuth() throws IOException{
        logger.info(serviceAccount.getInputStream().toString());
        var options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
                .build();

        var firebaseApp = FirebaseApp.initializeApp(options);
        return FirebaseAuth.getInstance(firebaseApp);
    }

}
