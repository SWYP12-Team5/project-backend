package team.unibusk.backend.domain.member.presentation.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import team.unibusk.backend.domain.member.application.dto.request.MemberNameUpdateServiceRequest;

@Builder
public record MemberNameUpdateRequest(

        @NotNull(message = "이름을 입력해 주세요.")
        String name

) {

    public MemberNameUpdateServiceRequest toServiceRequest(Long memberId) {
        return MemberNameUpdateServiceRequest.builder()
                .memberId(memberId)
                .name(name)
                .build();
    }

}
