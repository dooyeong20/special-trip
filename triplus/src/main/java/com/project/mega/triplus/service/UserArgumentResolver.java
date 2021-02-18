package com.project.mega.triplus.service;

import com.project.mega.triplus.annotation.SocialUser;
import com.project.mega.triplus.entity.Oauth2User;
import com.project.mega.triplus.entity.SocialType;
import com.project.mega.triplus.repository.Oauth2UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.project.mega.triplus.entity.SocialType.*;

@RequiredArgsConstructor
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    private final Oauth2UserRepository oauth2UserRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(SocialUser.class) != null &&
                parameter.getParameterType().equals(Oauth2User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getSession();
        Oauth2User oauth2User = (Oauth2User) session.getAttribute("user");

        return getUser(oauth2User, session);
    }

    private Oauth2User getUser(Oauth2User oauth2User, HttpSession session) {
        if (oauth2User == null) {
            try {
                OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
                Map<String, Object> map = token.getPrincipal().getAttributes();

                Oauth2User convertUser = convertUser(token.getAuthorizedClientRegistrationId(), map);

                oauth2User = oauth2UserRepository.findByEmail(convertUser.getEmail());
                if (oauth2User == null)
                    oauth2User = oauth2UserRepository.save(convertUser);

                setRoleIfNotSame(oauth2User, token, map);
                session.setAttribute("user", oauth2User);
            } catch (ClassCastException ex) {
                return oauth2User;
            }
        }

        return oauth2User;
    }

    private Oauth2User convertUser(String authority, Map<String, Object> map) {
        if (GOOGLE.getValue().equals(authority))
            return getModernUser(GOOGLE, map);

        else if (FACEBOOK.getValue().equals(authority))
            return getModernUser(FACEBOOK, map);


        return null;
    }

    private Oauth2User getModernUser(SocialType socialType, Map<String, Object> map) {
        return Oauth2User.builder()
                .name(String.valueOf(map.get("name")))
                .email(String.valueOf(map.get("email")))
                .principal(String.valueOf(map.get("id")))
                .socialType(socialType)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
    }

    private void setRoleIfNotSame(Oauth2User oauth2User, OAuth2AuthenticationToken token, Map<String, Object> map) {
        if (!token.getAuthorities().contains(
                new SimpleGrantedAuthority(oauth2User.getSocialType().getRoleType()))) {
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(map, "N/A",
                            AuthorityUtils.createAuthorityList(oauth2User.getSocialType().getRoleType())));
        }
    }
}
