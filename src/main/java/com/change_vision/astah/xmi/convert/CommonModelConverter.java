package com.change_vision.astah.xmi.convert;

import java.util.Map;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.model.ClassifierModelConverters;
import com.change_vision.astah.xmi.convert.model.ModelConverter;
import com.change_vision.astah.xmi.convert.relationship.RelationshipConverter;
import com.change_vision.astah.xmi.convert.relationship.RelationshipConverters;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.INamedElement;

public class CommonModelConverter {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(CommonModelConverter.class);
    
    private Map<Element, IElement> converteds;
    private Map<String, Relationship> relationships;
    private final RelationshipConverters relationshipConverters;
    private final ClassifierModelConverters modelConverters;

    public CommonModelConverter(ConvertHelper helper, Map<Element, IElement> converteds, Map<String, Relationship> relationships,AstahAPIUtil util) {
        this.converteds = converteds;
        this.relationships = relationships;
        this.modelConverters = new ClassifierModelConverters(util,helper);
        this.relationshipConverters = new RelationshipConverters(converteds,util);
    }
    
    public void convert(INamedElement astahElement, Element parent) throws InvalidEditingException, ClassNotFoundException {
        if(parent == null){
            throw new IllegalArgumentException("parent is null.");            
        }
        if(astahElement == null){
            return;
        }
        for (Element uml2Element : parent.getOwnedElements()) {
            INamedElement newUMLModel = null;
            try {
                if (uml2Element instanceof Relationship){
                    Relationship relationship = (Relationship) uml2Element;
                    boolean converted = false;
                    for (RelationshipConverter converter : relationshipConverters.getConverters()){
                        if (converter.accepts(relationship)) {
                            rememberRelationship(relationship);
                            converted = true;
                            break;
                        }
                    }
                    if(converted == false) {
                        notConvertedElement(uml2Element);
                    }
                } else {
                    boolean converted = false;
                    for (ModelConverter converter : modelConverters.getConverters()) {
                        if(converter.accepts(uml2Element)){
                            newUMLModel = converter.convert(astahElement, uml2Element);
                            if(newUMLModel != null){
                                converted = true;
                            }
                        }
                    }
                    if(converted == false) {
                        notConvertedElement(uml2Element);
                    }
                }
            } catch (InvalidEditingException ex) {
                logger.error("Exception by InvalidEditing", ex);
                continue;
            }
            if (newUMLModel != null) {
                converteds.put(uml2Element, newUMLModel);
            }
            convert(newUMLModel, uml2Element);
        }

    }

    private void notConvertedElement(Element uml2Element) {
        java.lang.Class<? extends Element> baseClass = uml2Element.getClass();
        String className = baseClass.getSimpleName();
        logger.trace("doesn't target of CommonModelConverter:{}",className);
    }
    
    private void rememberRelationship(Element e) {
        if (e instanceof Relationship) {
            relationships.put(XMILoader.getId(e), (Relationship) e);
        }
    }
    
}
