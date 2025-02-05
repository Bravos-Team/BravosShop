package com.bravos2k5.bravosshop.service;

public interface EmailService {

    void sendEmail(String to, String subject, String content);

}
