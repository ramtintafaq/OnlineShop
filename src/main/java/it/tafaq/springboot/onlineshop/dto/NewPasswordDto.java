package it.tafaq.springboot.onlineshop.dto;

import org.springframework.security.crypto.password.PasswordEncoder;

public class NewPasswordDto {
    String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
