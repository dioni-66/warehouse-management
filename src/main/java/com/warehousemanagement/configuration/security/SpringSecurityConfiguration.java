package com.warehousemanagement.configuration.security;

import com.warehousemanagement.configuration.jwt.JwtConfigurationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.ShallowEtagHeaderFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Profile("security")
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * SECURITY_LOGIN path
     */
    private static final String SECURITY_LOGIN = "/rest/security/login";

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringSecurityConfiguration.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomMd5PasswordEncoder customMd5PasswordEncoder;

    /**
     * log info on startup
     */
    public SpringSecurityConfiguration() {
        super();
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        SpringSecurityConfiguration.LOGGER.info("SpringSecurityConfiguration is initialized.");
    }

    /**
     * Configure userDetailsService and password encryption.
     *
     * @param authenticationManagerBuilder the <code>AuthenticationManagerBuilder</code> instantiated by Spring Boot.
     * @throws Exception if anything goes wrong.
     */
    @Override
    public void configure(final AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder.authenticationProvider(authProvider());
    }

    /**
     * Auth provider
     *
     * @return {@link DaoAuthenticationProvider}
     */
    @Bean
    public DaoAuthenticationProvider authProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(customMd5PasswordEncoder);
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * The JwtAuthenticationTokenFilter to be used for authentication.
     *
     * @return the <code>JwtAuthenticationTokenFilter</code>.
     */
    @Bean
    public JwtConfigurationFilter authenticationTokenFilterBean() {
        return new JwtConfigurationFilter();
    }

    /**
     * A CorsConfigurationSource to enable Cross-Origin Resource Sharing (CORS)
     *
     * @return a <code>CorsConfigurationSource</code>
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return new WebCorsConfigurationSource();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // deactivate CSRF when using JWT
                .csrf().disable()
                // do not create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()
                // allow version request without authentication
                .antMatchers("/**/version").permitAll()
                // Swagger permission, only relevant if swagger profile activated
                .antMatchers("/v3/api-docs/**").permitAll() // needed for Swagger rest api
                .antMatchers("/swagger-resources/**").permitAll() // needed for Swagger ui
                .antMatchers("/webjars/springfox-swagger-ui/**").permitAll() // needed for Swagger ui
                .antMatchers("/swagger-ui/**").permitAll() // needed for Swagger ui

                // allow web frontend without authentication
                .antMatchers("/assets/**").permitAll()
                .antMatchers("/*").permitAll()

                // allow authentication without being authenticated
                .antMatchers(SpringSecurityConfiguration.SECURITY_LOGIN).permitAll()
                // all other services need to be authenticated
                .anyRequest().authenticated();

        // Add a cors filter to the security filter
        httpSecurity.cors();

        // add custom JWT based security filter
        httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().cacheControl();
        httpSecurity.addFilterAfter(new ShallowEtagHeaderFilter(), FilterSecurityInterceptor.class);
    }

    /**
     * Method used to request context listener
     *
     * @return {@link RequestContextListener}
     */
    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }

}
