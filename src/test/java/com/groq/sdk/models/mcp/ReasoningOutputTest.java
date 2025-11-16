package com.groq.sdk.models.mcp;

import com.groq.sdk.models.responses.ReasoningContent;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
*
* @author Debajit Kumar Phukan
* @since 16-Nov-2025
*
*/
class ReasoningOutputTest {

    @Test
    void testDefaultConstructor() {
        ReasoningOutput output = new ReasoningOutput();
        
        assertEquals("reasoning", output.getType());
        assertNull(output.getContent());
    }

    @Test
    void testSettersAndGetters() {
        ReasoningOutput output = new ReasoningOutput();
        
        output.setType("custom_reasoning");
        
        ReasoningContent content1 = new ReasoningContent();
        content1.setType("text");
        content1.setText("First reasoning step");
        
        ReasoningContent content2 = new ReasoningContent();
        content2.setType("text");
        content2.setText("Second reasoning step");
        
        List<ReasoningContent> content = Arrays.asList(content1, content2);
        output.setContent(content);

        assertEquals("custom_reasoning", output.getType());
        assertEquals(2, output.getContent().size());
        assertEquals("First reasoning step", output.getContent().get(0).getText());
        assertEquals("Second reasoning step", output.getContent().get(1).getText());
    }
}