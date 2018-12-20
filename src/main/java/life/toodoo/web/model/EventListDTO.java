package life.toodoo.web.model;

import java.util.List;

import lombok.Data;

@Data
public class EventListDTO 
{
	private List<EventDTO> events;
}
