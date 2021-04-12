package toeicLab.toeicLab.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import toeicLab.toeicLab.domain.Member;

import java.util.List;

@Getter
@Slf4j
public class MemberUser extends User {
    private Member member;
    public MemberUser(Member member){
        super(member.getUserId(), member.getPassword(), List.of(member.getMemberType()));
        this.member = member;
    }
}
