package org.mule.extension.spring.api.spring.editors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;

public class MethodSecurityMetadataSourceEditorTest {

  private MethodSecurityMetadataSourceEditor editor;

  @Before
  public void setUp() {
    editor = new MethodSecurityMetadataSourceEditor();
  }

  @Test
  public void testSetAsTextWithEmptyString() {
    String input = "";

    editor.setAsText(input);

    Object result = editor.getValue();
    assertThat(result, notNullValue());
    assertThat(result instanceof MapBasedMethodSecurityMetadataSource, is(true));

    MapBasedMethodSecurityMetadataSource metadataSource = (MapBasedMethodSecurityMetadataSource) result;
    assertThat(metadataSource.getMethodMapSize(), is(0));
  }

  @Test
  public void testSetAsTextWithNullInput() {
    editor.setAsText(null);

    Object result = editor.getValue();
    assertThat(result, nullValue());
  }

  @Test
  public void testSetAsTextWithWhitespaceOnlyInput() {
    String input = "   \n\t  ";

    editor.setAsText(input);

    Object result = editor.getValue();
    assertThat(result, notNullValue());
    assertThat(result instanceof MapBasedMethodSecurityMetadataSource, is(true));

    MapBasedMethodSecurityMetadataSource metadataSource = (MapBasedMethodSecurityMetadataSource) result;
    assertThat(metadataSource.getMethodMapSize(), is(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetAsTextWithInvalidPropertiesFormat() {
    String input = "invalid format without equals sign";

    editor.setAsText(input);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetAsTextWithInvalidPropertiesFormat2() {
    String input = "key1=value1\ninvalid line without equals\nkey2=value2";

    editor.setAsText(input);
  }
}
