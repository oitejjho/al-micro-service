package amlan.common.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StringUtilsTest {

    @Test
    public void testMaskEmailSuccess(){
        String email = "oitejjho@gmail.com";
        boolean actual = StringUtils.isValidEmail(email, null);

        Assert.assertEquals(true, actual);
    }

    @Test
    public void testMaskEmailSuccessEmpty(){
        String email = "oitejjho@gmail.com";
        boolean actual = StringUtils.isValidEmail(email, "");

        Assert.assertEquals(true, actual);
    }

    @Test
    public void testMaskEmailSuccessInvalidEmail(){
        String email = "gmail.com";
        boolean actual = StringUtils.isValidEmail(email, "");

        Assert.assertEquals(false, actual);
    }


}
