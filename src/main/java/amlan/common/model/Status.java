package amlan.common.model;

import amlan.common.constant.StatusConstants;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Status {

    public Integer code;

    public String message;

    public Status(StatusConstants.HttpConstants httpConstants) {
        this.code = httpConstants.getCode();
        this.message = httpConstants.getDesc();
    }

    public Status(StatusConstants.HttpConstants httpConstants, String message) {
        this.code = httpConstants.getCode();
        this.message = message;
    }

}
