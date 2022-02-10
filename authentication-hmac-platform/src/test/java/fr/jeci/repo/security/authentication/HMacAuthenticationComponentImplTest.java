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

import org.alfresco.repo.security.authentication.AuthenticationException;
import org.junit.Test;

public class HMacAuthenticationComponentImplTest {
  private static String SECRET_KEY = "123";
  private static String VALIDATE_TIME_TOKEN = "00:05:00";

  @Test
  public void test() {
    String loginHMac = "test1.dsi";
    String passwordHMac = "test1.dsi|1606821488798|8ded7e7a3238da741ef9673df9c9878b6c004b324853d10666dee2ba2d69ec3e";

    HMacAuthenticationComponentImpl alfrescoHMacAuthentication = new HMacAuthenticationComponentImpl();
    alfrescoHMacAuthentication.setSecretKey(SECRET_KEY);
    alfrescoHMacAuthentication.setValidateTimeToken(VALIDATE_TIME_TOKEN);
    try {
      alfrescoHMacAuthentication.authenticateImpl(loginHMac, passwordHMac.toCharArray());
    } catch (AuthenticationException e) {
      System.out.println(e.getCause());
    }
  }
}
