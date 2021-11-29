package org.oddjob.groovy;

import org.oddjob.arooa.design.DesignFactory;
import org.oddjob.arooa.design.DesignInstance;
import org.oddjob.arooa.design.DesignProperty;
import org.oddjob.arooa.design.DesignValueBase;
import org.oddjob.arooa.design.SimpleTextAttribute;
import org.oddjob.arooa.design.screem.Form;
import org.oddjob.arooa.design.screem.TextPseudoForm;
import org.oddjob.arooa.parsing.ArooaContext;
import org.oddjob.arooa.parsing.ArooaElement;

public class GroovyExpressionDF implements DesignFactory {

	public DesignInstance createDesign(
			ArooaElement element, 
			ArooaContext arooaContext) {

		return new GroovyExpressionDesign(element, arooaContext);		
	}
}
	
class GroovyExpressionDesign extends DesignValueBase {
		
	private final SimpleTextAttribute expression;

	GroovyExpressionDesign(ArooaElement element, ArooaContext parentContext) {
		super(element, parentContext);

		expression = new SimpleTextAttribute("expression", this);
	}

	public DesignProperty[] children() {
		return new DesignProperty[] { expression };
	}

	public Form detail() {
		return new TextPseudoForm(expression);
	}

}	
