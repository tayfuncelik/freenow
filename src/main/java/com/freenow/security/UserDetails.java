package com.freenow.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetails
{
    private Long id;
    private String userName;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;
    private List<String> roles;


    public void setAuth(List<String> roles)
    {
        List<GrantedAuthority> authorities = roles.stream()
            .map(role -> new SimpleGrantedAuthority(role))
            .collect(Collectors.toList());
        this.authorities = authorities;
    }


    public Long getId()
    {
        return id;
    }


    public void setId(Long id)
    {
        this.id = id;
    }


    public String getUserName()
    {
        return userName;
    }


    public void setUserName(String userName)
    {
        this.userName = userName;
    }


    public String getEmail()
    {
        return email;
    }


    public void setEmail(String email)
    {
        this.email = email;
    }


    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return authorities;
    }


    public void setAuthorities(Collection<? extends GrantedAuthority> authorities)
    {
        this.authorities = authorities;
    }


    public List<String> getRoles()
    {
        return roles;
    }


    public void setRoles(List<String> roles)
    {
        this.roles = roles;
    }
}
