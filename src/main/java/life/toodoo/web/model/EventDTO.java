package life.toodoo.web.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class EventDTO 
{
	private Long    id;
	private String  title;
	private Integer priority;
	private String  status;
	private BigDecimal completePct;
}
