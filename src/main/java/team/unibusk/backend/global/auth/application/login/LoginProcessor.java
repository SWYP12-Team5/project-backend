package team.unibusk.backend.global.auth.application.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.unibusk.backend.global.auth.application.dto.response.LoginResultResponse;
import team.unibusk.backend.global.auth.domain.refreshtoken.RefreshToken;
import team.unibusk.backend.global.auth.domain.refreshtoken.RefreshTokenRepository;
import team.unibusk.backend.global.jwt.config.TokenProperties;
import team.unibusk.backend.global.jwt.generator.JwtTokenGenerator;

@RequiredArgsConstructor
@Component
public class LoginProcessor {

    private final JwtTokenGenerator tokenGenerator;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProperties tokenProperties;

    public LoginResultResponse generateLoginResult(Long memberId) {
        String accessToken = tokenGenerator.generateAccessToken(memberId);
        String refreshToken = tokenGenerator.generateRefreshToken(memberId);

        RefreshToken refreshTokenEntity = refreshTokenRepository.findFirstByMemberIdOrderByIdDesc(memberId)
                .orElse(RefreshToken.of(memberId, refreshToken, tokenProperties.expirationTime().refreshToken()));

        refreshTokenEntity.rotate(refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);

        return LoginResultResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
