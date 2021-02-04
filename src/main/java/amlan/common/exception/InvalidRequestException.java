package amlan.common.exception;

import amlan.common.constant.StatusConstants;
import lombok.Getter;


@Getter
public class InvalidRequestException extends RuntimeException {

    private StatusConstants.HttpConstants status;

    public InvalidRequestException(StatusConstants.HttpConstants status) {
        super(status.getDesc(), null);
        this.status = status;
    }

    public InvalidRequestException(StatusConstants.HttpConstants status, String desc) {
        super(desc, null);
        this.status = status;
        this.status.setDesc(desc);
    }

}
