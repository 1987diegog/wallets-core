package uy.com.demente.ideas.wallets.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uy.com.demente.ideas.wallets.exceptions.UnauthorizedException;


/**
 * @author 1987diegog
 */
@Service
public class SecurityService {

	private static Logger logger = LoggerFactory.getLogger(SecurityService.class);

	/**
	 * Simulate access token authentication
	 * 
	 * @param sessionToken
	 * @throws UnauthorizedException
	 */
	@Transactional
	public void validateSessionToken(String sessionToken) throws UnauthorizedException {

		logger.info("[SECURITY_SERVICE] - Validating access token: " + sessionToken //
				+ ", with authentication server");

		if (sessionToken != null && sessionToken.equals("INVALID_SESSION_TOKEN")) {
			throw new UnauthorizedException("Invalid access token: " + sessionToken);
		}

		logger.info("[SECURITY_SERVICE] - Valid access token: " + sessionToken //
				+ ", authentication OK");
	}
}
