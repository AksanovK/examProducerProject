package exam.services;

import exam.models.dto.EmailPasswordDto;
import exam.models.dto.TokenDto;

public interface LoginService {
    TokenDto login(EmailPasswordDto emailPassword) throws Throwable;
}
