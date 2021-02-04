package amlan.common.exception;

import amlan.common.constant.StatusConstants;
import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

    private final StatusConstants.HttpConstants status;

    public ServiceException(StatusConstants.HttpConstants status) {
        super(status.getDesc(), null);
        this.status = status;
    }

    public ServiceException(StatusConstants.HttpConstants status, String message) {
        super(message, null);
        this.status = status;
    }

}
