package com.change_vision.astah.xmi.convert.relationship;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static util.UML2TestUtil.createGeneralization;

import org.eclipse.uml2.uml.Relationship;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.ConvertHelper;
import com.change_vision.astah.xmi.convert.relationship.GeneralizationConverter;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IPackage;

public class GeneralizationConverterTest {

    @Mock
    private AstahAPIUtil util;

    @Mock
    private BasicModelEditor basicModelEditor;

    @Mock
    private IPackage parentPackage;
    
    @Mock
    private IClass parentClass;
    
    @Mock
    private ConvertHelper helper;

    private GeneralizationConverter converter;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(util.getBasicModelEditor()).thenReturn(basicModelEditor);
        converter = new GeneralizationConverter(util, helper);
    }
    
    @Test
    public void rejectWithNull() throws Exception {
        boolean result = converter.accepts(null);
        assertThat(result,is(false));        
    }
    
    @Test
    public void acceptGeneralization() {
        Relationship element = createGeneralization();
        boolean result = converter.accepts(element);
        assertThat(result,is(true));
    }

}