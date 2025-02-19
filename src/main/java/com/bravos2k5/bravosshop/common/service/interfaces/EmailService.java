package com.bravos2k5.bravosshop.common.service.interfaces;

public interface EmailService {

    void sendEmail(String to, String subject, String content);

}
