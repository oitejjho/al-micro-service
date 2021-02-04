package amlan.common.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PdpaUtilsTest {

    @Test
    public void maskEmailSuccess(){
        String email = "oitejjho@gmail.com";
        String expectedMaskedEmail = "XXXejjhogmailcom";
        String actualMaskedEmail = PdpaUtils.maskEmail(email);

        Assert.assertEquals(expectedMaskedEmail, actualMaskedEmail);
        Assert.assertEquals(expectedMaskedEmail.substring(0,3), "XXX");
    }

    @Test
    public void maskEmailWhereEmailIsNull(){
        String email = null;
        String expectedMaskedEmail = "";
        String actualMaskedEmail = PdpaUtils.maskEmail(email);

        Assert.assertEquals(expectedMaskedEmail, actualMaskedEmail);
    }

    @Test
    public void maskEmailWhereEmailIsEmpty(){
        String email = "";
        String expectedMaskedEmail = "";
        String actualMaskedEmail = PdpaUtils.maskEmail(email);

        Assert.assertEquals(expectedMaskedEmail, actualMaskedEmail);
    }

    @Test
    public void maskEmailWhereEmailLengthIsLessThanMaskLength(){
        String email = "a@";
        String expectedMaskedEmail = "a@";
        String actualMaskedEmail = PdpaUtils.maskEmail(email);

        Assert.assertEquals(expectedMaskedEmail, actualMaskedEmail);
    }

    @Test
    public void maskEmailWhereEmailLengthEqualMaskLength(){
        String email = "a@b";
        String expectedMaskedEmail = "XXX";
        String actualMaskedEmail = PdpaUtils.maskEmail(email);

        Assert.assertEquals(expectedMaskedEmail, actualMaskedEmail);
    }


}
