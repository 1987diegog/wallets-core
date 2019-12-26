package uy.com.demente.ideas.wallets.business.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uy.com.demente.ideas.wallets.business.exceptions.InvalidSessionException;


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
	 * @throws InvalidSessionException
	 */
	@Transactional
	public void validateSessionToken(String sessionToken) throws InvalidSessionException {

		logger.info("[SECURITY_SERVICE] - Validating access token: " + sessionToken //
				+ ", with authentication server");

		if (sessionToken != null && sessionToken.equals("INVALID_SESSION_TOKEN")) {
			throw new InvalidSessionException("Invalid access token: " + sessionToken);
		}

		logger.info("[SECURITY_SERVICE] - Valid access token: " + sessionToken //
				+ ", authentication OK");
	}
}
