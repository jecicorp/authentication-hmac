/**
* Copyright 2021 - Jeci SARL - https://jeci.fr
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program.  If not, see http://www.gnu.org/licenses/.
*/
package fr.jeci.repo.security.authentication;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.alfresco.repo.security.authentication.AbstractAuthenticationComponent;
import org.alfresco.repo.security.authentication.AuthenticationException;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author cindy
 *
 */
public class HMacAuthenticationComponentImpl extends AbstractAuthenticationComponent {

	private Log logger = LogFactory.getLog(getClass());
	private String secretKey;
	private String validateTimeToken;

	public HMacAuthenticationComponentImpl() {
		super();
	}

	/**
	 * Implement the authentication method
	 */
	@Override
	protected void authenticateImpl(String userName, final char[] password) throws AuthenticationException {
		if (logger.isTraceEnabled()) {
			logger.trace("Start HMac Authentication for user : " + AuthenticationUtil.maskUsername(userName));
		}

		// password = uid|timestamp|token
		String hMacPassword = new String(password);
		// split password with '|' as separator
		String[] elements = hMacPassword.split("\\|");

		if (elements.length != 3) {
			throw new AuthenticationException("Not request hMac authentication");
		}

		StringBuilder message = new StringBuilder();
		message.append(elements[0]);
		message.append("|");
		message.append(elements[1]);

		// Test si l'utilisateur à authentifier est bien le owner du token
		if (!userName.equals(elements[0])) {
			throw new AuthenticationException("User is not the owner of the token");
		}

		try {
			// Test si le timestamp date de moins de 5min
			checkTokenIsValid(Long.parseLong(elements[1]));

			// Test si le mot de passe est identique
			checkPassword(message.toString(), userName, elements[2]);

			if (logger.isTraceEnabled()) {
				logger.trace("Token hMac validated for user : " + AuthenticationUtil.maskUsername(userName));
			}

			// Authentication validated
			setCurrentUser(userName);
		} catch (NumberFormatException e) {
			throw new AuthenticationException("Timestamp is not a long number");
		} catch (AuthenticationException e) {
			throw new AuthenticationException("Login Failed", e);
		}
	}

	/**
	 * Test si le timestamp fourni est supérieur à 5 minutes on rejette l'authentification
	 * 
	 * @param timestamphMac
	 * @throws AuthenticationException
	 */
	private void checkTokenIsValid(long timestamphMac) throws AuthenticationException {
		long milliseconds = System.currentTimeMillis() - timestamphMac;
		int seconds = (int) milliseconds / 1000;
		int hours = seconds / 3600;
		int minutes = (seconds % 3600) / 60;
		seconds = (seconds % 3600) % 60;

		String[] validateTimes = validateTimeToken.split("\\:");
		int limitHour = Integer.parseInt(validateTimes[0]);
		int limitMinute = Integer.parseInt(validateTimes[1]);
		int limitSecond = Integer.parseInt(validateTimes[2]);
		if ((hours > limitHour) || (hours == limitHour && minutes > limitMinute)
				|| (hours == limitHour && minutes == limitMinute && seconds > limitSecond)) {
			throw new AuthenticationException("The token is no longer valid");
		}

	}

	/**
	 * Test si le mot de passe est identique après avoir appliqué le hash
	 * 
	 * @param message
	 * @param userName
	 * @param tokenhMac
	 * @throws AuthenticationException
	 */
	private void checkPassword(String message, String userName, String tokenhMac) throws AuthenticationException {
		try {

			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKey = new SecretKeySpec(getSecretKey().getBytes(), "HmacSHA256");
			mac.init(secretKey);
			byte[] digest = mac.doFinal(message.getBytes());
			StringBuilder token = new StringBuilder();
			for (byte b : digest) {
				token.append(String.format("%02x", b));
			}
			if (token == null || userName == null) {
				throw new AuthenticationException("Token and userName required");
			}

			if (!tokenhMac.equals(token.toString())) {
				throw new AuthenticationException("Login with token hMac failed");
			}
		} catch (NoSuchAlgorithmException e) {
			throw new AuthenticationException("Failed to calculate hmac-sha256", e);
		} catch (InvalidKeyException e) {
			throw new AuthenticationException("Failed to init secret key", e);
		}
	}

	@Override
	protected boolean implementationAllowsGuestLogin() {
		return false;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	/**
	 * @return the validateTimeToken
	 */
	public String getValidateTimeToken() {
		return validateTimeToken;
	}

	/**
	 * @param validateTimeToken the validateTimeToken to set
	 */
	public void setValidateTimeToken(String validateTimeToken) {
		this.validateTimeToken = validateTimeToken;
	}
}
