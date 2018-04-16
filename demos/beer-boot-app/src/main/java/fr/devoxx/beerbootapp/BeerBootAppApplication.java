package fr.devoxx.beerbootapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.facade.SimpleHttpFacade;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class BeerBootAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeerBootAppApplication.class, args);
	}
}

@KeycloakConfiguration
class SecurityConfiguration extends KeycloakWebSecurityConfigurerAdapter {

    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }
    @Bean
    public KeycloakConfigResolver configResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @Autowired
    public void registerAuthProvider(AuthenticationManagerBuilder builder) {
        builder.authenticationProvider(keycloakAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.authorizeRequests()
                .antMatchers("/beers").authenticated();
    }
}

@Controller
class BeerController {

    private final ObjectMapper objectMapper;
    private final AdapterDeploymentContext adapterDeploymentContext;

    BeerController(ObjectMapper objectMapper, AdapterDeploymentContext adapterDeploymentContext) {
        this.objectMapper = objectMapper;
        this.adapterDeploymentContext = adapterDeploymentContext;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/beers")
    public String beers(Model model) throws IOException {
        List<Map<String, Object>> beers =
                objectMapper.readValue(
                        getClass().getResourceAsStream("/beers.json"),
                        new TypeReference<ArrayList<HashMap<String, String>>>() {});
        model.addAttribute("beers", beers);
        return "beers";
    }

    @GetMapping("/change-password")
    public String changePassword(RedirectAttributes attributes, HttpServletRequest request, HttpServletResponse response) {
        HttpFacade facade = new SimpleHttpFacade(request, response);
        KeycloakDeployment deployment = adapterDeploymentContext.resolveDeployment(facade);
        attributes.addAttribute("referrer", deployment.getResourceName());
        return "redirect:" + deployment.getAccountUrl() + "/password";
    }

}
