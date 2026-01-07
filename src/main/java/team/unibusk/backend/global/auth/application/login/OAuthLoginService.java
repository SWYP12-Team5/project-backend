package team.unibusk.backend.global.auth.application.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.unibusk.backend.domain.member.domain.Member;
import team.unibusk.backend.domain.member.domain.MemberRepository;
import team.unibusk.backend.global.auth.application.attributes.AuthAttributes;
import team.unibusk.backend.global.auth.application.dto.response.LoginResultResponse;
import team.unibusk.backend.global.auth.application.dto.response.OauthLoginResultResponse;
import team.unibusk.backend.global.auth.presentation.exception.AlreadyRegisteredMemberException;

@RequiredArgsConstructor
@Service
public class OAuthLoginService {

    private final MemberRepository memberRepository;
    private final LoginProcessor loginProcessor;

    @Transactional
    public OauthLoginResultResponse handleLoginSuccess(AuthAttributes attributes) {
        Member member = memberRepository.findByEmail(attributes.getEmail())
                .map(existing -> validateAndReturn(existing, attributes))
                .orElseGet(() -> registerNewMember(attributes));

        LoginResultResponse tokens = loginProcessor.generateLoginResult(member.getId());

        return OauthLoginResultResponse.builder()
                .accessToken(tokens.accessToken())
                .refreshToken(tokens.refreshToken())
                .isFirstLogin(member.isFirstLogin())
                .memberId(member.getId())
                .build();
    }

    private Member validateAndReturn(Member member, AuthAttributes attributes) {
        if (member.isRegisteredWithDifferentProvider(attributes.getProvider())) {
            throw new AlreadyRegisteredMemberException();
        }
        return member;
    }

    private Member registerNewMember(AuthAttributes attributes) {
        Member newMember = Member.from(attributes);
        newMember.markAsFirstLogin();
        return memberRepository.save(newMember);
    }

}
