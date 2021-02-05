package amlan.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class StatusConstants {

    @Getter
    @AllArgsConstructor
    public enum HttpConstants {

        SUCCESS(0, "Success"),

        EMPLOYEE_FIRST_NAME_IS_REQUIRED(1, "Employee first name is required"),

        EMPLOYEE_LAST_NAME_IS_REQUIRED(2, "Employee last name is required"),

        EMPLOYEE_EMAIL_IS_REQUIRED(3, "Employee email is required"),

        EMPLOYEE_EMAIL_ALREADY_EXISTS(4, "Employee email already exists"),

        EMPLOYEE_DOES_NOT_EXIST(5, "Employee does not exists"),

        EMAIL_IS_INVALID(7, "Email is invalid"),

        PASSWORD_IS_INVALID(8, "Password is invalid"),

        USERNAME_ALREADY_EXISTS(9, "Username already exists"),

        UNAUTHORIZED_ACCESS(10, "Unauthorized access"),

        EXTERNAL_SERVER_ERROR(35_998, "External Server Error"),

        INTERNAL_SERVER_ERROR(35_999, "Internal Server Error");

        private final Integer code;

        private String desc;

        public void setDesc(String desc) {
            this.desc = desc;
        }

    }

}
