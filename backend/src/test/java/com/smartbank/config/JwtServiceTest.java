package com.smartbank.config;
import org.junit.jupiter.api.Test; import org.springframework.mock.env.MockEnvironment; import static org.junit.jupiter.api.Assertions.*;
class JwtServiceTest {
 @Test void missingSecretUsesTemporaryDevelopmentKey(){JwtService jwt=new JwtService("",3600000,new MockEnvironment());String token=jwt.create("customer@example.test","CUSTOMER");assertEquals("customer@example.test",jwt.subject(token));}
 @Test void productionRequiresConfiguredSecret(){MockEnvironment environment=new MockEnvironment();environment.setActiveProfiles("prod");assertThrows(IllegalStateException.class,()->new JwtService("",3600000,environment));}
 @Test void rejectsShortConfiguredSecret(){assertThrows(IllegalStateException.class,()->new JwtService("too-short",3600000,new MockEnvironment()));}
}
