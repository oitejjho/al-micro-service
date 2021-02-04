package amlan.common.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PdpaUtilsTest {

    @Test
    public void testMaskEmailSuccess(){
        String email = "oitejjho@gmail.com";
        String expectedMaskedEmail = "XXXejjhogmailcom";
        String actualMaskedEmail = PdpaUtils.maskEmail(email);

        Assert.assertEquals(expectedMaskedEmail, actualMaskedEmail);
        Assert.assertEquals(expectedMaskedEmail.substring(0,3), "XXX");
    }

    @Test
    public void testMaskEmailWhereEmailIsNull(){
        String email = null;
        String expectedMaskedEmail = "";
        String actualMaskedEmail = PdpaUtils.maskEmail(email);

        Assert.assertEquals(expectedMaskedEmail, actualMaskedEmail);
    }

    @Test
    public void testMaskEmailWhereEmailIsEmpty(){
        String email = "";
        String expectedMaskedEmail = "";
        String actualMaskedEmail = PdpaUtils.maskEmail(email);

        Assert.assertEquals(expectedMaskedEmail, actualMaskedEmail);
    }

    @Test
    public void testMaskEmailWhereEmailLengthIsLessThanMaskLength(){
        String email = "a@";
        String expectedMaskedEmail = "a@";
        String actualMaskedEmail = PdpaUtils.maskEmail(email);

        Assert.assertEquals(expectedMaskedEmail, actualMaskedEmail);
    }

    @Test
    public void testMaskEmailWhereEmailLengthEqualMaskLength(){
        String email = "a@b";
        String expectedMaskedEmail = "XXX";
        String actualMaskedEmail = PdpaUtils.maskEmail(email);

        Assert.assertEquals(expectedMaskedEmail, actualMaskedEmail);
    }


}
