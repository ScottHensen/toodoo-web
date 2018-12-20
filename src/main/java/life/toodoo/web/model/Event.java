package life.toodoo.web.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event 
{
	private Long id;
	private String  title;
	private Integer priority;
	private String  status;
	private BigDecimal completePct;
}
