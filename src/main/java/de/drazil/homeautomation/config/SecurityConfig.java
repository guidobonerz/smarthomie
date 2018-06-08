package de.drazil.homeautomation.config;

//@EnableWebSecurity
public class SecurityConfig {// extends WebSecurityConfigurerAdapter {
	/*
	 * @Override protected void configure(HttpSecurity http) throws Exception {
	 * http.authorizeRequests().antMatchers("/css/**", "/index",
	 * "/overview/**").permitAll() .antMatchers("/user/**",
	 * "/request/**").hasRole("USER").and().formLogin().loginPage("/login")
	 * .failureUrl("/login-error"); //
	 * http.authorizeRequests().antMatchers("/**").permitAll(); }
	 */

	/*
	 * @Autowired public void configureGlobal(AuthenticationManagerBuilder auth)
	 * throws Exception { auth.inMemoryAuthentication()
	 * .withUser(User.withDefaultPasswordEncoder().username("user").password(
	 * "password").roles("USER")); }
	 */
}
