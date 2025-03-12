package io.swipepay.clientdesk.form;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class PasswordForm {
	public interface ValidationReset {}
	public interface ValidationChange {}
	
	@NotBlank(groups = {ValidationChange.class, ValidationReset.class}, message = "Email address is required")
	@Email(groups = {ValidationChange.class, ValidationReset.class}, message = "Email address is invalid")
	private String emailAddress;
	
	@NotBlank(groups = {ValidationChange.class}, message = "Current password is required")
	private String currentPassword;
	
	@NotBlank(groups = {ValidationChange.class}, message = "New password is required")
	private String newPassword;
	
	@NotBlank(groups = {ValidationChange.class}, message = "Confirm new password is required")
	private String confirmNewPassword;
	
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}

	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}