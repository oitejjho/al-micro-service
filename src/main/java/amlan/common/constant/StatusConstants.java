package amlan.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class StatusConstants {

    @Getter
    @AllArgsConstructor
    public enum HttpConstants {

        SUCCESS(0, "Success"),

        EMPLOYEE_NOT_FOUND(1, "Employee not found"),

        EXTERNAL_SERVER_ERROR(35_998, "External Server Error"),

        INTERNAL_SERVER_ERROR(35_999, "Internal Server Error");

        private final Integer code;

        private String desc;

        public void setDesc(String desc) {
            this.desc = desc;
        }

    }

}
