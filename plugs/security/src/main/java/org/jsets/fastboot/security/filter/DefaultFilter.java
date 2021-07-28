package org.jsets.fastboot.security.filter;

import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsets.fastboot.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultFilter extends AbstractInnerFilter {

	@Override
	public boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response, Set<String> props)
			throws Exception {
		if (this.isLoginRequest(request)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onAccessDenied(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String token = getAuthorization(request);
		if (StringUtils.isBlank(token)) {
			log.warn("Token为空，PATH:{}", request.getServletPath());
			return true;
		} else {
			this.getAuthenticator().authenticate(token);
		}
		return true;
	}
}
