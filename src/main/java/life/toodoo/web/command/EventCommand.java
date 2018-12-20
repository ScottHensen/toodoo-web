package life.toodoo.web.command;

import java.math.BigDecimal;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class EventCommand 
{
	public EventCommand() {
		this.title = "";
		this.status = "Not Started";
		this.priority = 999;
		this.completePct = BigDecimal.valueOf(0.0);
	}
	
	private Long id;
	
	@NotBlank
	private String  title; 
	
	@NotNull
	@Min(   1 )
	@Max( 999 )
	private Integer priority;
	
	@NotNull
	@Min( (long)   0.0 )
	@Max( (long) 100.0 )
	private BigDecimal completePct;
	
	@NotBlank
	private String  status;

}
