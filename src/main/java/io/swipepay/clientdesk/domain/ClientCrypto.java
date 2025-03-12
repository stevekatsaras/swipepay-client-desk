package io.swipepay.clientdesk.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames = {"data_key", "client_id"})
})
public class ClientCrypto {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "data_key", nullable = false)
	private String dataKey;
	
	@Column(name = "enabled", nullable = false)
	private Boolean enabled;
	
	@Column(name = "modified", nullable = false)
	private LocalDateTime modified;

	@ManyToOne
	@JoinColumn(name = "client_id", nullable = false)
	private Client client;
	
	protected ClientCrypto() {
		
	}
	
	public ClientCrypto(String dataKey, Boolean enabled, LocalDateTime modified, Client client) {
		this.dataKey = dataKey;
		this.enabled = enabled;
		this.modified = modified;
		this.client = client;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDataKey() {
		return dataKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public LocalDateTime getModified() {
		return modified;
	}

	public void setModified(LocalDateTime modified) {
		this.modified = modified;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}