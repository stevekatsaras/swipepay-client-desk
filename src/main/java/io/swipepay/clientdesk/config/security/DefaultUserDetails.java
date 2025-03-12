package io.swipepay.clientdesk.config.security;

import java.util.Collection;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientUser;

public class DefaultUserDetails implements UserDetails {
	private static final long serialVersionUID = 1L;
	private ClientUser clientUser;
	Collection<GrantedAuthority> grantedAuthorities;
	
	public DefaultUserDetails(ClientUser clientUser) {
		this.clientUser = clientUser;
		grantedAuthorities = AuthorityUtils.createAuthorityList(clientUser.getRole());
	}
	
	@Override
	public String getUsername() {
		return clientUser.getEmailAddress();
	}
	
	@Override
	public String getPassword() {
		return clientUser.getPassword();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}
	
	@Override
	public boolean isEnabled() {
		return clientUser.getEnabled();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return !clientUser.getExpired();
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return !clientUser.getLocked();
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return !clientUser.getPasswordExpired();
	}
	
	public Client getClient() {
		return clientUser.getClient();
	}

	public void setClient(Client client) {
		clientUser.setClient(client);
	}
	
	public ClientUser getClientUser() {
		return clientUser;
	}

	public void setClientUser(ClientUser clientUser) {
		this.clientUser = clientUser;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}