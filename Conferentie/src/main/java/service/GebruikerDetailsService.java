package service;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.NoArgsConstructor;
import repository.GebruikerRepository;
import util.Rol;
import domein.Gebruiker;

@Service
@NoArgsConstructor
public class GebruikerDetailsService implements UserDetailsService {

		@Autowired
		private GebruikerRepository gebruikerRepo;

	    @Override
	    public UserDetails loadUserByUsername(String email)
	                       throws UsernameNotFoundException {

	        Gebruiker g = gebruikerRepo.findByEmail(email)
	                      .orElseThrow(() -> new UsernameNotFoundException(email));

	        return new GebruikerDetails(g);
	    }

		  
	    private Collection<? extends GrantedAuthority> convertAuthorities(Rol rol) { 
			 return Collections.singletonList(
			    new SimpleGrantedAuthority("ROLE_" + rol.toString()));
		}
		  
		public boolean bestaatEmail(String email) {
		     return gebruikerRepo.existsByEmail(email);
		}

}
