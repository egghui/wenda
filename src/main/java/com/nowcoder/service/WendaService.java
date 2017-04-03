package com.nowcoder.service;

import org.springframework.stereotype.Service;

/**
 * Created by 李丹慧 on 2017/4/2.
 */
@Service
public class WendaService {
    public String getMessage(int userId) {
        return "Hello message " + String.valueOf(userId);
    }
}
