package toeicLab.toeicLab.configuration.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import toeicLab.toeicLab.configuration.auth.PrincipalDetails;
import toeicLab.toeicLab.configuration.oauth.provider.GoogleUserInfo;
import toeicLab.toeicLab.configuration.oauth.provider.OAuth2UserInfo;
import toeicLab.toeicLab.domain.Address;
import toeicLab.toeicLab.domain.GenderType;
import toeicLab.toeicLab.domain.Member;
import toeicLab.toeicLab.domain.MemberType;
import toeicLab.toeicLab.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    // userRequest 는 code를 받아서 accessToken을 응답 받은 객체
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest); // google의 회원 프로필 조회

        // code를 통해 구성한 정보
        log.info("userRequest clientRegistration : " + userRequest.getClientRegistration());
        // token을 통해 응답받은 회원정보
        log.info("oAuth2User : " + oAuth2User);

        return processOAuth2User(userRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

        // Attribute를 파싱해서 공통 객체로 묶는다. 관리가 편함.
        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            log.info("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }
        Optional<Member> userOptional =
                memberRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());

        Member member;
        if (userOptional.isPresent()) {
            member = userOptional.get();
            // user가 존재하면 update 해주기
            member.setEmail(oAuth2UserInfo.getEmail());
            memberRepository.save(member);
        } else {
            // user의 패스워드가 null이기 때문에 OAuth 유저는 일반적인 로그인을 할 수 없음.
            member = Member.builder()
                    .username(oAuth2UserInfo.getNickname())
                    .userId(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
                    .nickname(oAuth2UserInfo.getNickname())
                    .email(oAuth2UserInfo.getEmail())
                    .age(000)
                    .address(Address.builder()
                            .city("미등록")
                            .street("미등록")
                            .zipcode("미등록").build())
                    .memberType(MemberType.USER)
                    .role("ROLE_USER")
                    .provider(oAuth2UserInfo.getProvider())
                    .providerId(oAuth2UserInfo.getProviderId())
                    .build();
            memberRepository.save(member);
        }

        return new PrincipalDetails(member, oAuth2User.getAttributes());
    }
}