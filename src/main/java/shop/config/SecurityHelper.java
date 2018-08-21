package shop.config;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityHelper {
    public SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }
    
    public boolean isAuthenticated() {
        return !isAnonymous();
    }
    
    public boolean isAnonymous() {
        return getSecurityContext().getAuthentication() instanceof AnonymousAuthenticationToken;
    }
    
    public UserDetails getUserDetails() {
        if (isAnonymous()) {
            return null;
        } else {
            return (UserDetails) getSecurityContext().getAuthentication().getPrincipal();
        }
    }
}
