package com.martindengler.proj.FIXSimple;

import com.martindengler.proj.FIXSimple.spec.MsgType;
import com.martindengler.proj.FIXSimple.spec.Tag;
import org.junit.Assert;
import org.junit.Test;

/**
 * Example test case
 */
public class FIXMessageTest {

    @Test
    public void testFactory() throws Exception {
        FIXMessage test = FIXMessage.factory(MsgType.LOGON);
        Assert.assertTrue(test != null);
    }

    @Test
    public void testValidate() throws Exception {
        FIXMessage test = FIXMessage.factory(MsgType.LOGON);
        Assert.assertTrue(test != null);
        Assert.assertTrue(!FIXMessage.validate(test));
        test = test.putM(Tag.BODYLENGTH, test.calculateFIXBodyLength())
            .putM(Tag.CHECKSUM, test.calculateFIXChecksum());
        Assert.assertTrue(FIXMessage.validate(test));
    }

}
