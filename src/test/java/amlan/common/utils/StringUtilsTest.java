package amlan.common.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StringUtilsTest {

    @Test
    public void testIsValidEmailSuccess(){
        String email = "oitejjho@gmail.com";
        boolean actual = StringUtils.isValidEmail(email);

        Assert.assertEquals(true, actual);
    }

    @Test
    public void testIsValidEmailSuccessInvalidEmail(){
        String email = "gmail.com";
        boolean actual = StringUtils.isValidEmail(email);

        Assert.assertEquals(false, actual);
    }

    @Test
    public void testIsValidPasswordSuccess(){
        String password = "1Admin@1";
        boolean actual = StringUtils.isValidPassword(password);

        Assert.assertEquals(true, actual);
    }

    @Test
    public void testIsValidPasswordInvalidPassword(){
        String email = "admin";
        boolean actual = StringUtils.isValidEmail(email);

        Assert.assertEquals(false, actual);
    }


}
