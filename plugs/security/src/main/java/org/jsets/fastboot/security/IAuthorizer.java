package org.jsets.fastboot.security;

import java.util.Set;

public interface IAuthorizer {

	boolean hasRole(String roleIdentifier);

	boolean hasAnyRole(Set<String> roleIdentifiers);

	boolean hasAllRoles(Set<String> roleIdentifiers);
	
	boolean isPermitted(String permission);
	
	boolean isPermittedAny(Set<String> permissions);
	
	boolean isPermittedAll(Set<String> permissions);
}