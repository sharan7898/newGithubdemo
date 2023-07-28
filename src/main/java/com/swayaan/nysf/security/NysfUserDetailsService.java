package com.swayaan.nysf.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.repository.UserRepository;

public class NysfUserDetailsService implements UserDetailsService {

	@Autowired 
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		String username=email.trim();
		User user = userRepo.getUserByEmail(username);
		if (user != null) {
			return new NysfUserDetails(user);
		}
		
		throw new UsernameNotFoundException("Could not find user with username: " + email);
	}

}
