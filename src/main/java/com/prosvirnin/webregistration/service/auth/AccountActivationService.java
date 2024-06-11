package com.prosvirnin.webregistration.service.auth;

import com.prosvirnin.webregistration.model.account.ActivationResponse;
import com.prosvirnin.webregistration.model.user.ActivationCode;
import com.prosvirnin.webregistration.model.user.User;
import com.prosvirnin.webregistration.repository.ActivationCodeRepository;
import com.prosvirnin.webregistration.repository.UserRepository;
import com.prosvirnin.webregistration.service.MailSender;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional(readOnly = true)
public class AccountActivationService {

    private final MailSender mailSender;
    private final ActivationCodeRepository activationCodeRepository;
    private final UserRepository userRepository;

    @Getter
    @Value("${sendActivationCode}")
    private Boolean sendActivationCode;

    public AccountActivationService(MailSender mailSender, ActivationCodeRepository activationCodeRepository, UserRepository userRepository) {
        this.mailSender = mailSender;
        this.activationCodeRepository = activationCodeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void sendActivationCode(User user) {
        if (!sendActivationCode)
            return;

        ActivationCode activationCode;
        if(user.getActivationCode() == null) {
            activationCode = ActivationCode.builder()
                    .build();
            user.setActivationCode(activationCode);
            activationCode.setUser(user);
        }
        activationCode = user.getActivationCode();
        activationCode.setCode(getNewActivationCode());

        mailSender.send(
                user.getEmail(),
                "Activation code",
                String.format("Ваш код активаци: %s", user.getActivationCode().getCode())
        );
        userRepository.save(user);
        activationCodeRepository.save(activationCode);
    }

    private String getNewActivationCode(){
        return Integer.toString(ThreadLocalRandom
                .current()
                .nextInt(1000,9999)
        );
    }

    @Transactional
    public ActivationResponse activate(String code, Authentication authentication){
        var user = (User) authentication.getPrincipal();
        if (user.getActivationCode().getCode().equals(code)) {
            user.getActivationCode().setCode(null);
            user.setEmail(user.getEmailToChange());
            user.setEmailToChange(null);
            userRepository.save(user);
            return ActivationResponse.ok();
        }
        return ActivationResponse.wrongCode();
    }

}
