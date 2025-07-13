/*
 * Odilon Object Storage
 * (C) Novamens 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dellemuse.webapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * 
 * 
 * @author atolomei@novamens.com (Alejandro Tolomei)
 */
@Configuration
@EnableWebSecurity
public class BasicAuthWebSecurityConfiguration {

	@JsonIgnore
	@Autowired
	private BasicAuthenticationEntryPoint authenticationEntryPoint;

	
	@Autowired
	public BasicAuthWebSecurityConfiguration (BasicAuthenticationEntryPoint authenticationEntryPoint) {
		
		this.authenticationEntryPoint=authenticationEntryPoint;
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		 //http.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/presigned/", "/presigned/object", "/public/").permitAll())
			 //.authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated());
			 //  
			 //.csrf(AbstractHttpConfigurer::disable);
			 
			 /**
			 http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
			 .httpBasic(Customizer.withDefaults())
			 .formLogin(Customizer.withDefaults())
			 .csrf(AbstractHttpConfigurer::disable);
			 **/
		 
		
		http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().permitAll())
		 .csrf(AbstractHttpConfigurer::disable);
		
		 return http.build();
		 
	}

	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		
		UserDetails user = User.withUsername("dellemuse")
	    .password(passwordEncoder().encode("dellemuse"))
	    .roles("USER")
	    .build();
		return new InMemoryUserDetailsManager(user);
	  }
	 
	  @Bean
	  public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	  }
}
