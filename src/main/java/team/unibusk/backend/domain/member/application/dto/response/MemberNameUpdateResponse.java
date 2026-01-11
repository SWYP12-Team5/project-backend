package team.unibusk.backend.domain.member.application.dto.response;

import lombok.Builder;

@Builder
public record MemberNameUpdateResponse(

        Long memberId

) {
}
