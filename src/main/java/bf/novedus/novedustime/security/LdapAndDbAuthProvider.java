package bf.novedus.novedustime.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

import bf.novedus.novedustime.model.User;
import bf.novedus.novedustime.repository.UserRepository;

public class LdapAndDbAuthProvider extends LdapAuthenticationProvider {

	@Autowired
	UserRepository userDao;

	public LdapAndDbAuthProvider(LdapAuthenticator authenticator, LdapAuthoritiesPopulator authoritiesPopulator) {
		super(authenticator, authoritiesPopulator);
	}

	@Override
	protected DirContextOperations doAuthentication(UsernamePasswordAuthenticationToken authentication) {

		// log.debug("Checking if user <{}> is defined at database to use this
		// application.", authentication.getName());

		// Here is the part we need to check in the database if user has
		// required role to log into the application.
		// After check if user has the role, do nothing, otherwise throw
		// exception like below example.
		boolean canUserAuthenticate = isActiveUserExist(authentication.getName());
		// log.debug("canUserAuthenticate: {}", canUserAuthenticate);

		if (!canUserAuthenticate)
			throw new DisabledException("User does not have access to Application!");

		return super.doAuthentication(authentication);
	}

	private boolean isActiveUserExist(String userId) {
		List<User> logins = (List<User>) userDao.findAll();

		for (User login : logins) {
			if (login.getUserName().equals(userId)) {
				return true;
			}
		}
		return false;
	}
}