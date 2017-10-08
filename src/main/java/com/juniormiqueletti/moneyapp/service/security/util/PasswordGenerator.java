package com.juniormiqueletti.moneyapp.service.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

    public static void main(String[] args){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("admin: " + encoder.encode("admin"));
        System.out.println("john: " + encoder.encode("john"));
        System.out.println(encoder.matches("john","$2a$10$9C09OgrNue79J8PItYoFwujKWaQf0rlfO313z.kPcZWUWxvaqZPo6"));
    }
}
