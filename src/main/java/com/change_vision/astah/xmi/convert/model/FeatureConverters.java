package com.change_vision.astah.xmi.convert.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Element;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.jude.api.inf.model.IElement;

public class FeatureConverters {
    
    private List<FeatureConverter> converters = new ArrayList<FeatureConverter>();
    
    public FeatureConverters(Map<Element, IElement> converteds, AstahAPIUtil util){
        this.converters.add(new AttributeConverter(converteds, util));
        this.converters.add(new OperationConverter(converteds, util));
    }

    public List<FeatureConverter> getConverters() {
        return Collections.unmodifiableList(converters);
    }

}
