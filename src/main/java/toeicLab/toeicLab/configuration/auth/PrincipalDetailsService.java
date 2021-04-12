//package toeicLab.toeicLab.configuration.auth;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import toeicLab.toeicLab.domain.Member;
//import toeicLab.toeicLab.repository.MemberRepository;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class PrincipalDetailsService implements UserDetailsService {
//
//    private final MemberRepository memberRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        log.info("구글...");
//        Member member = memberRepository.findByUsername(username);
//        if(member == null) {
//            return null;
//        }else {
//            return new PrincipalDetails(member);
//        }
//
//    }
//
//}