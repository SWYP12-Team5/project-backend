package team.unibusk.backend.domain.member.domain;

import jakarta.persistence.*;
import lombok.*;
import team.unibusk.backend.global.auth.application.attributes.AuthAttributes;
import team.unibusk.backend.global.domain.BaseTimeEntity;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    private String name;

    @Enumerated(EnumType.STRING)
    private LoginProvider provider;

    private String externalId;

    private boolean firstLogin;

    public static Member from(AuthAttributes attributes) {
        return Member.builder()
                .email(attributes.getEmail())
                .provider(attributes.getProvider())
                .externalId(attributes.getExternalId())
                .firstLogin(true)
                .build();
    }

    public boolean isRegisteredWithDifferentProvider(LoginProvider provider) {
        return this.provider != provider;
    }

    public void markAsFirstLogin() {
        this.firstLogin = true;
    }

    public void markAsNotFirstLogin() {
        this.firstLogin = false;
    }


}
