package life.toodoo.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//NOTE: THIS IS NOT USED.
//      Replaced exception with a response object that delivers not-fnd msg.

@ResponseStatus( HttpStatus.NOT_FOUND )
public class NotFoundException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public NotFoundException() {
		super();
	}
	
	public NotFoundException( String message ) {
		super(message);
	}
	
	public NotFoundException( String message, Throwable cause ) {
		super(message, cause);
	}
}
