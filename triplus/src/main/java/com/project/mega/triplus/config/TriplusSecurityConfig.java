package com.project.mega.triplus.config;

import com.project.mega.triplus.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.util.Collections;
import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class TriplusSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        다 무시
//        http.authorizeRequests().
//                anyRequest().authenticated().
//                and().
//                csrf().disable();

        http.authorizeRequests()
                .antMatchers(
                        "/",
                        "/search",
                        "/detail",
                        "total_plan",
                        "total_place",
                        "/oauth2/**",
                        "/login"
                ).permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/mypage/**").hasRole("USER")
                .anyRequest().authenticated()
                .accessDecisionManager(getMyAccessDecisionManager())

                .and()
                .oauth2Login().defaultSuccessUrl("/loginSuccess").failureUrl("/loginFailure")
                .and()
                .headers().frameOptions().disable()

                .and()
                .exceptionHandling()
                .accessDeniedPage("/access_denied")
                // 인증이 진행되지 않은 상태에서 페이지에 접근할 경우, 자동으로 "/login" 모달을 띄워준다.
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))

                .and()
                .formLogin().successForwardUrl("/")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/").deleteCookies("JSESSIONID").invalidateHttpSession(true)
                .and()
                .csrf().disable();
    }

    private AccessDecisionManager getMyAccessDecisionManager() {
        // 권한 계층
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");

        // 검사 기준
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);

        // voter
        WebExpressionVoter voter = new WebExpressionVoter();
        voter.setExpressionHandler(handler);

        List<AccessDecisionVoter<?>> voters = Collections.singletonList(voter);

        return new AffirmativeBased(voters);
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user").password(passwordEncoder().encode("user")).roles("USER")
//                .and()
//                .withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN");
//
//        auth.userDetailsService(userService);
//    }

    @Override
    public void configure(WebSecurity web)throws Exception{
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


}














