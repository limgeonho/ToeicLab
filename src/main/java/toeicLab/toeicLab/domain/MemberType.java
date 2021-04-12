package toeicLab.toeicLab.domain;

import org.springframework.security.core.GrantedAuthority;

public enum MemberType implements GrantedAuthority {
    USER,ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
