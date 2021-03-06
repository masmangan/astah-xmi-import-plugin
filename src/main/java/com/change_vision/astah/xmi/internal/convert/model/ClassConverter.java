package com.change_vision.astah.xmi.internal.convert.model;

import static java.lang.String.format;

import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Node;
import org.eclipse.uml2.uml.Stereotype;

import com.change_vision.astah.xmi.AstahAPIUtil;
import com.change_vision.astah.xmi.convert.model.ClassifierConverter;
import com.change_vision.astah.xmi.convert.model.UniqueNameCreator;
import com.change_vision.astah.xmi.internal.convert.ConvertHelper;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IPackage;

public class ClassConverter implements ClassifierConverter {
    
    private AstahAPIUtil apiUtil = new AstahAPIUtil();
    private UniqueNameCreator uniqueNameCreator = new UniqueNameCreator();
    private ConvertHelper helper;

    public ClassConverter(AstahAPIUtil util, ConvertHelper helper) {
        this.apiUtil = util;
        this.helper = helper;
    }

    @Override
    public boolean accepts(Element element) {
        if (element == null) return false;
        if ((element instanceof Class) == false) return false;
        if (element instanceof Behavior) return false;
        if (element instanceof Component) return false;
        if (element instanceof Node) return false;
        if (element instanceof Stereotype) return false;
        return true;
    }
    
    @Override
    public IClass convert(IElement parent, Element element) throws InvalidEditingException {
        if (element instanceof Class == false) {
            throw new IllegalArgumentException(format("target element isn't Class '%s'",element)); 
        }
        Classifier target = (Classifier) element;
        IClass results = null;
        if (parent instanceof IPackage) {
            IPackage parentPackage = (IPackage) parent;
            String name = uniqueNameCreator.getUniqueName(parentPackage.getOwnedElements(), target.getName());
            results = apiUtil.getBasicModelEditor().createClass(parentPackage, name);
        }
        if (parent instanceof IClass) {
            IClass parentClass = (IClass) parent;
            String name = target.getName();
            if(element instanceof Class || element instanceof Interface){
                name = uniqueNameCreator.getUniqueName(parentClass.getNestedClasses(), target.getName());
            }else if(element instanceof DataType){
                name = uniqueNameCreator.getUniqueName(parentClass.getAttributes(), target.getName());                
            }
            results = apiUtil.getBasicModelEditor().createClass(parentClass, name);            
        }
        helper.setStereotype(element, results);
        return results;
    }
    
}
