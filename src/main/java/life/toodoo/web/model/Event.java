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
//	@Builder.Default
	private BigDecimal completePct; //  = BigDecimal.valueOf(0.00);
	
	public Boolean isComplete()	
	{
		if (completePct == null) {
			return false;
		}
		return (completePct.doubleValue() >= 1.00);
	}
}
