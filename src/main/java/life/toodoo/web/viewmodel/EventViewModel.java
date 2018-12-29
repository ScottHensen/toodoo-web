package life.toodoo.web.viewmodel;

import life.toodoo.web.command.EventCommand;
import life.toodoo.web.model.Event;
import lombok.Data;

@Data
public class EventViewModel 
{
	public EventViewModel() {
		this.message            = "";
		this.messageType        = "default";
		this.hasError           = false;
		this.showUpdateButton   = true;
		this.showDeleteButton   = true;
		this.showCompleteButton = true;
	}
	
	
	private Event event;
//	private EventCommand eventCommand;
	private Integer eventDisplayOrder;
	
	private Boolean showUpdateButton;     
	private Boolean showDeleteButton;
	private Boolean showCompleteButton;
	
	private String  message;
	private String  messageType;
	private Boolean hasError;

	
	public void setError(String message) {
		this.message = message;
		this.messageType = "error";
		this.hasError = true;
		this.showUpdateButton   = false;
		this.showDeleteButton   = false;
		this.showCompleteButton = false;
	}
}
