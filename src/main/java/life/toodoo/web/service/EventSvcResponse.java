package life.toodoo.web.service;

import java.util.HashSet;
import java.util.Set;

import life.toodoo.web.model.Event;
import lombok.Data;

@Data
public class EventSvcResponse 
{
	private Event event;
	private Set<Event> events = new HashSet<>();
	
	private Integer code;
	private String  message;
	private Boolean success;
	
	public Boolean isSuccess() {
		return success;
	}
}
