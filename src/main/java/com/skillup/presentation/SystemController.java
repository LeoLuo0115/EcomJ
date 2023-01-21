package com.skillup.presentation;

import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class SystemController {
    @GetMapping(value = "/system/time")
    public String getName() {
        //blabla
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(new Date());
        return date;
    }
}
