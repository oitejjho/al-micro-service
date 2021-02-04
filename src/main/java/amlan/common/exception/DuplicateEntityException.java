package amlan.common.exception;

import amlan.common.constant.StatusConstants;
import lombok.Getter;

@Getter
public class DuplicateEntityException extends RuntimeException {

    private final StatusConstants.HttpConstants status;

    public DuplicateEntityException(StatusConstants.HttpConstants status) {
        super(status.getDesc(), null);
        this.status = status;
    }

}
