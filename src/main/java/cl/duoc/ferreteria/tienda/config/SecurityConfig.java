package cl.duoc.ferreteria.tienda.config;

import cl.duoc.ferreteria.tienda.security.JwtAuthenticationFilter;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.*;
import org.springframework.web.cors.*;

@Configuration
public class SecurityConfig {

  // Públicos: ver categorías y productos, comprar (POST /api/ordenes) y enviar una consulta.
  // Todo lo demás de /api (crear, editar y eliminar, ver órdenes y consultas) es solo para ADMIN.
  // Sin token válido responde 401; con un rol sin permiso, 403 con { "error": "Acceso denegado" }.
  @Bean SecurityFilterChain filterChain(HttpSecurity http,
      JwtAuthenticationFilter jwtFilter) throws Exception {
    return http.csrf(csrf -> csrf.disable())
      .cors(Customizer.withDefaults())
      .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        .requestMatchers("/swagger-ui/**", "/swagger-ui.html",
          "/v3/api-docs/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/categorias/**", "/api/productos/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/ordenes", "/api/consultas").permitAll()
        .requestMatchers("/api/**").hasRole("ADMIN")
        .anyRequest().denyAll())
      .exceptionHandling(e -> e
        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        .accessDeniedHandler((request, response, ex) -> {
          response.setStatus(HttpStatus.FORBIDDEN.value());
          response.setContentType("application/json;charset=UTF-8");
          response.getWriter().write("{\"error\":\"Acceso denegado\"}");
        }))
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
      .build();
  }

  @Bean CorsConfigurationSource corsConfigurationSource(
      @Value("${app.cors.allowed-origin}") String allowedOrigin) {
    CorsConfiguration c = new CorsConfiguration();
    c.setAllowedOrigins(List.of(allowedOrigin));
    c.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    c.setAllowedHeaders(List.of("Content-Type", "Authorization"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", c);
    return source;
  }
}
