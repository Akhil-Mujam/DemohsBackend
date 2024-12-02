package com.example.Demohs.Service;

import com.example.Demohs.Dto.UserAuthDataDto;
import com.example.Demohs.Entity.UserAuthData;

import java.util.List;
import java.util.UUID;

public interface UserAuthDataService {

    UserAuthData findByUserName(String username);

    String create(UserAuthDataDto userAuthDataDto);


    public String delete(UUID userId);

    public List<UserAuthData> findAll();

    public String update( UserAuthData userAuthData);

}
