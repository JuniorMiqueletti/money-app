package com.juniormiqueletti.moneyapp.service.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.juniormiqueletti.moneyapp.model.User;

public class AppUser extends org.springframework.security.core.userdetails.User{
	private static final long serialVersionUID = 1L;

	private User user;

	public AppUser(User user, Collection<? extends GrantedAuthority> authorities) {
		super(user.getEmail(), user.getPassword(), authorities);
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
