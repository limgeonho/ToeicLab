package toeicLab.toeicLab.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import toeicLab.toeicLab.service.StudyGroupApplicationService;

@Profile("service")
@Configuration
@RequiredArgsConstructor
@Slf4j
public class ServiceConfiguration {
    private final StudyGroupApplicationService studyGroupApplicationService;

    @Scheduled(cron="0 0 04 * * ?")
    public void MatchStudyGroup() {
        studyGroupApplicationService.matchStudyGroups();
    }
}
