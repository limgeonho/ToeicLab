package toeicLab.toeicLab.configuration.oauth.provider;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attributes;

    public GoogleUserInfo(Map<String, Object> attributes){
        this.attributes = attributes;
    }

    // 숫자코드
    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    // 이름
    @Override
    public String getNickname() {
        return (String) attributes.get("name");
    }

    // 이메일
    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    // 회원가입 서비스명
    @Override
    public String getProvider() {
        return "google";
    }
}