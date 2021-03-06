package org.apereo.cas.web;

import org.apereo.cas.authentication.Authentication;
import org.apereo.cas.support.oauth.DefaultOAuthCasClientRedirectActionBuilder;
import org.apereo.cas.util.OidcAuthorizationRequestSupport;
import org.pac4j.cas.client.CasClient;
import org.pac4j.core.redirect.RedirectAction;
import org.pac4j.core.context.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * This is {@link OidcCasClientRedirectActionBuilder}.
 *
 * @author Misagh Moayyed
 * @since 5.0.0
 */
public class OidcCasClientRedirectActionBuilder extends DefaultOAuthCasClientRedirectActionBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(OidcCasClientRedirectActionBuilder.class);
    
    private final OidcAuthorizationRequestSupport oidcAuthorizationRequestSupport;

    public OidcCasClientRedirectActionBuilder(final OidcAuthorizationRequestSupport oidcAuthorizationRequestSupport) {
        this.oidcAuthorizationRequestSupport = oidcAuthorizationRequestSupport;
    }

    @Override
    public RedirectAction build(final CasClient casClient, final WebContext context) {
        final Optional<Authentication> auth = oidcAuthorizationRequestSupport.isCasAuthenticationAvailable(context);
        auth.ifPresent(authentication -> oidcAuthorizationRequestSupport.configureClientForMaxAgeAuthorizationRequest(casClient, context, authentication));

        OidcAuthorizationRequestSupport.configureClientForPromptLoginAuthorizationRequest(casClient, context);
        OidcAuthorizationRequestSupport.configureClientForPromptNoneAuthorizationRequest(casClient, context);
        
        final RedirectAction action = super.build(casClient, context);
        LOGGER.debug("Final redirect action is [{}]", action);
        return action;
    }
}
