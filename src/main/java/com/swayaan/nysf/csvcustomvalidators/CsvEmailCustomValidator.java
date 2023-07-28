package com.swayaan.nysf.csvcustomvalidators;

import org.springframework.stereotype.Component;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.constraint.StrRegEx;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.util.CsvContext;

@Component
public class CsvEmailCustomValidator extends CellProcessorAdaptor {

	public CsvEmailCustomValidator() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CsvEmailCustomValidator(CellProcessor next) {
		super(next);
		// TODO Auto-generated constructor stub
	}

	@Override
	public <T> T execute(Object value, CsvContext context) {
		// final String emailRegex = "[a-z0-9\\._]+@[a-z0-9\\.]+";
		 final String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
		
		StrRegEx.registerMessage(emailRegex, "must be a valid email address");

		// validateInputNotNull(value, context); // throws an Exception if the input is
		// null

		if (value instanceof String && !value.toString().trim().matches(emailRegex)) {
			throw new SuperCsvConstraintViolationException(
					String.format("The Email Id - '%s' must be a valid email address", value),
					context, this);
		}

		return next.execute(value, context);
	}
}
