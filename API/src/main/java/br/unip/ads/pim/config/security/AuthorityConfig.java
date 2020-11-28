package br.unip.ads.pim.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * @see https://stackoverflow.com/a/48809501/3072570
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AuthorityConfig {

}
