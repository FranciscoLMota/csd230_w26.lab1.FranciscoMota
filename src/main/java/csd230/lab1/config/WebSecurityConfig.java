
package csd230.lab1.config;

import csd230.lab1.auth.JwtAuthorizationFilter; // NEW
import csd230.lab1.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager; // NEW
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer; // NEW
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // NEW
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // NEW
import jakarta.servlet.http.HttpServletResponse; // NEW
import org.springframework.http.HttpStatus; // NEW
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthorizationFilter jwtAuthorizationFilter; // NEW: Added

    public WebSecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthorizationFilter jwtAuthorizationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    // NEW: AuthenticationManager is needed by AuthController to verify login credentials
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        // 1. Allow public access to specific endpoints
//                        .requestMatchers(
//                                "/h2-console/**",
//                                "/login", "/register",
//                                "/css/**", "/js/**",
//                                // "/api/rest/**", //REMOVING this line as we need to authorize it now
//                                "/error").permitAll()
//                        // --- ADD THESE LINES FOR SWAGGER ---
//                        .requestMatchers(
//                                "/v3/api-docs",          // The actual JSON data
//                                "/v3/api-docs/**",       // Support for groups
//                                "/swagger-ui/**",        // UI static resources
//                                "/swagger-ui.html",      // UI entry point
//                                "/v3/api-docs.yaml"      // YAML version
//                        ).permitAll()
//                        // NEW Rest auth end points so users can auth
//                        .requestMatchers("/api/rest/auth/**").permitAll()
//                        // ------------------------------------
//                        // 2. Admin only endpoints (CRUD operations on books)
//                        .requestMatchers("/books/add", "/books/edit/**", "/books/delete/**").hasRole("ADMIN")
//                        // NEW REST API Security (Requires Role)
//                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/rest/**").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers("/api/rest/**").hasRole("ADMIN")
//                        // 3. All other requests (view books, cart) require login
//                        .anyRequest().authenticated()

                        // TEMPORARY: early React exercises only; restore protection in 2.12
                                .requestMatchers("/api/rest/auth/**").permitAll()
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/rest/carts/mine").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/rest/carts/mine/products/*").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/rest/carts/mine/products/*").hasAnyRole("USER", "ADMIN")
                                // Original whole-cart CRUD is administrative, including list/by-ID reads.
                                .requestMatchers("/api/rest/carts", "/api/rest/carts/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/rest/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/rest/**").hasRole("ADMIN")
                                .requestMatchers("/login", "/register", "/", "/css/**", "/js/**").permitAll()
                                .requestMatchers("/books/add", "/books/edit/**", "/books/delete/**", "/magazines/add", "/magazines/edit/**", "/magazines/delete/**", "/discmags/add", "/discmags/edit/**", "/discmags/delete/**", "/tickets/add", "/tickets/edit/**", "/tickets/delete/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                // REST API Error Handling:
                // If the URL starts with /api/rest/, return 401. Otherwise, redirect to /login.
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            if (request.getRequestURI().startsWith("/api/rest/")) {
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.setContentType("application/json");
                                response.getWriter().write("{\"message\":\"Authentication required\"}");
                            } else {
                                response.sendRedirect("/login");
                            }
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            if (request.getRequestURI().startsWith("/api/rest/")) {
                                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                response.setContentType("application/json");
                                response.getWriter().write("{\"message\":\"Access denied\"}");
                            } else {
                                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                            }
                        })
                )
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/books", true)
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        http.cors(Customizer.withDefaults());
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**", "/api/rest/**"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // FIX: Pass userDetailsService to the constructor
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}








