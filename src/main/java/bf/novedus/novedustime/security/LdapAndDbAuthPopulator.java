package bf.novedus.novedustime.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;

import bf.novedus.novedustime.repository.UserRepository;

public class LdapAndDbAuthPopulator extends DefaultLdapAuthoritiesPopulator {

	@Autowired
	UserRepository userRepository;

	public LdapAndDbAuthPopulator(ContextSource contextSource, String groupSearchBase) {
		super(contextSource, groupSearchBase);
	}

	protected Set<GrantedAuthority> getAdditionalRoles(DirContextOperations user, String username) {
		Set<GrantedAuthority> mappedAuthorities = new HashSet<GrantedAuthority>();

		/* Add additional roles from other sources for this user */
		mappedAuthorities.add(new GrantedAuthority() {
			private static final long serialVersionUID = 3618700057662135367L;

			@Override
			public String getAuthority() {
				String auth = userRepository.findByUserName(username).getRole().getRoleName();
				if (auth != null)
					return auth;

				return "";
			}

			@Override
			public String toString() {
				return this.getAuthority();
			}
		});

		return mappedAuthorities;
	}

}
