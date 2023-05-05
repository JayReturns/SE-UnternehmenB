package com.dhbw.unternehmenb.ssp.view;

import com.dhbw.unternehmenb.ssp.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    public String getUser(){
        //TODO: get the data from db
        User user = new User("Hello", "World");
        return user.toString();
    }
}
