package com.redhat.mailinglistOnline.jsf;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;


import com.redhat.mailinglistOnline.client.UserSession;
import com.redhat.mailinglistOnline.client.entities.Email;
import com.redhat.mailinglistOnline.client.rest.RestClient;


/* It is not only an email but contains information about replies*/
@ManagedBean(name="detailedEmail")
@ViewScoped 
public class DetailedEmail implements Serializable{

	@Inject
	private RestClient restClient;
	@ManagedProperty(value="#{mailinglists}") 
	private MailingListsResponse mailinglistResponse;
	private String selectedId;
	private Email email;
	private List<Email> replies;
	// Now comes input fields:
	private String selectedMailinglist;
	private String newTag;

	public String getSelectedMailinglist() {
		return selectedMailinglist;
	}

	public void setSelectedMailinglist(String selectedMailinglist) {
		this.selectedMailinglist = selectedMailinglist;
	}

	public void load() throws IOException {
		email= restClient.getEmailById(selectedId);
		//email.getMainContent().get(0).getContent().replaceAll("(\r\n|\n)", "<br />"));
		if(isInMailinglist(selectedMailinglist) && selectedMailinglist!=null) {
			replies = restClient.getEmailReplies(email);
		} else {
			//redirect to default mailinglist if there is none in which this email is
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	        ec.redirect(ec.getRequestContextPath() + "/" + mailinglistResponse.getDefaultMailinglist());
		}
	}
	
	/*public String setEmail(String emailId) {
		email =restClient.getEmailById(emailId);
		replies = restClient.getEmailReplies(email);
		return "/email/index.xhtml";
	}*/

	public MailingListsResponse getMailinglistResponse() {
		return mailinglistResponse;
	}

	public void setMailinglistResponse(MailingListsResponse mailinglistResponse) {
		this.mailinglistResponse = mailinglistResponse;
	}

	public Email getEmail() {
		return email;
	}
	
	public String getSelectedId() {
		return selectedId;
	}
	
	
	public void setSelectedId(String selectedId) {
		this.selectedId=selectedId;
	}
	public List<Email> getReplies() {
		return replies;
	}
	
	public void setReplies(List<Email> replies) {
		this.replies=replies;
	}
	
	public RestClient getRestClient() {
		return restClient;
	}
	
	public void setRestClient(RestClient restClient) {
		this.restClient=restClient;
	}
	
	public boolean isInMailinglist(String mailinglist) {
		return email.getMessageMailingLists().get(0).equals(mailinglist);
	}
	
	public void addTag() throws IOException {
		restClient.addTagToEmail(email.getId(),newTag);
	}

	public String getNewTag() {
		return newTag;
	}

	public void setNewTag(String newTag) {
		this.newTag = newTag;
	}
	

}
