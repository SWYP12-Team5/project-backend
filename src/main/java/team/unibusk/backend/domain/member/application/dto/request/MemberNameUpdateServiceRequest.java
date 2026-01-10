package team.unibusk.backend.domain.member.application.dto.request;

import lombok.Builder;

@Builder
public record MemberNameUpdateServiceRequest(

        Long memberId,

        String name

) {
}
