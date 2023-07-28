package com.swayaan.nysf.csvcustomvalidators;


import org.springframework.stereotype.Component;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.util.CsvContext;
@Component
public class CsvStringCustomValidator extends CellProcessorAdaptor {

	public CsvStringCustomValidator() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CsvStringCustomValidator(CellProcessor next) {
		super(next);
		// TODO Auto-generated constructor stub
	}

	@Override
	public <T> T execute(Object value, CsvContext context) {

		String regex = "([a-zA-z]*){0,45}";
	//	validateInputNotNull(value, context); // throws an Exception if the input is null
			
		if (value instanceof String && !value.toString().trim().matches(regex)) {
			throw new SuperCsvConstraintViolationException(
					String.format("The field value - '%s' should not contain numbers or special characters", value), context, this);
		}

		return next.execute(value, context);
	}
}
