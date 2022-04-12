package bf.novedus.novedustime.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import bf.novedus.novedustime.controller.LoggingAccessDeniedHandler;
import bf.novedus.novedustime.security.LdapAndDbAuthPopulator;
import bf.novedus.novedustime.security.LdapAndDbAuthProvider;

/**
 * @author Idrissa Ganemtore
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private LoggingAccessDeniedHandler accessDeniedHandler;

	@Override 
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(this.ldapAndDatabaseAuthenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//TODO To check after implementation of all features
		http.csrf().disable().authorizeRequests().antMatchers("/webresources", "/error", "/viewimage_/*").anonymous()
				.antMatchers("/", "/user/*", "/common/*")
				.access("hasRole('ROLE_HR') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_TEAMLEAD')")
				.antMatchers("/teamleader/*")
				.access("hasRole('ROLE_HR') or hasRole('ROLE_ADMIN') or hasRole('ROLE_TEAMLEAD')").antMatchers("/hr/*")
				.access("hasRole('ROLE_HR') or hasRole('ROLE_ADMIN')").antMatchers("/admin/*")
				.access("hasRole('ROLE_ADMIN')").and().formLogin().loginPage("/login").and().logout()
				.invalidateHttpSession(true).clearAuthentication(true)
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout")
				.permitAll().and().exceptionHandling().accessDeniedHandler(accessDeniedHandler);
		;
	}

	@Bean(name = "ldapAuthenticationProvider")
	public AuthenticationProvider ldapAndDatabaseAuthenticationProvider() {
		LdapUserDetailsMapper userDetailsMapper = new LdapUserDetailsMapper();
		LdapAuthenticationProvider provider = new LdapAndDbAuthProvider(this.ldapAuthenticator(),
				this.ldapAuthoritiesPopulator());
		provider.setUserDetailsContextMapper(userDetailsMapper);

		return provider;
	}

	@Bean(name = "ldapAuthenticator")
	public LdapAuthenticator ldapAuthenticator() {
		BindAuthenticator authenticator = new BindAuthenticator(this.contextSource());
		authenticator.setUserDnPatterns(new String[] { "uid={0}" });
		return authenticator;
	}

	@Bean
	public LdapAndDbAuthPopulator ldapAuthoritiesPopulator() {
		return new LdapAndDbAuthPopulator(this.contextSource(), "");
	}

	@Bean(name = "contextSource")
	public DefaultSpringSecurityContextSource contextSource() {
		DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(
				"ldap://ldap.forumsys.com:389");
		contextSource.setBase("dc=example,dc=com");
		contextSource.setUserDn("cn=read-only-admin,dc=example,dc=com");
		contextSource.setPassword("password");
		return contextSource;
	}

}
