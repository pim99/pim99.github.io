package br.unip.ads.pim.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * @see https://www.baeldung.com/spring-security-authentication-provider
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
 
    @Autowired
    private CustomAuthenticationProvider authProvider;
 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
	    	// Libera os endpoints de Registro e Login.
	    	.antMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
	    	.antMatchers(HttpMethod.POST, "/api/login").permitAll()
        	// Habilita a segurança apenas a partir do path da nossa API RESTful: "/api".
        	.antMatchers("/api/**").authenticated()
        	// Habilita o Basic Authentication.
        	.and().httpBasic()
        	// Faz com que a API seja Stateless, ou seja, não mantenha estado (sessão).
        	// Referência: https://stackoverflow.com/a/37827078/3072570
        	.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        	// Habilita o Cross-Origin Resource Sharing (CORS)
        	// Referência: https://www.baeldung.com/spring-security-cors-preflight
        	.and().cors()
        	// Desabilita o Cross Site Request Forgery (CSRF).
        	.and().csrf().disable();
    }
}
