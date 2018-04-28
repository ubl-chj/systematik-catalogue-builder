import de.ubleipzig.scb.templates.TemplateBody;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

/**
 * TemplateBodyTest.
 *
 * @author christopher-johnson
 */
public class TemplateBodyTest {

    @Mock
    private TemplateBody mockBody;

    @Test
    void testSerialization() {
        mockBody = new TemplateBody();
        mockBody.setResourceHeight(1000);
    }
}
