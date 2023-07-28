package com.swayaan.nysf.csvcustomvalidators;

import org.springframework.stereotype.Component;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.util.CsvContext;

import com.swayaan.nysf.entity.GenderEnum;

@Component
public class CsvGenderCustomValidator extends CellProcessorAdaptor {

	public CsvGenderCustomValidator() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CsvGenderCustomValidator(CellProcessor next) {
		super(next);
		// TODO Auto-generated constructor stub
	}

	@Override
	public <T> T execute(Object value, CsvContext context) {

		validateInputNotNull(value, context); 
		if (!(value instanceof String)) {
			throw new SuperCsvConstraintViolationException(
					String.format("The field value - '%s' should not contain numbers or special characters", value),
					context, this);
		}
		try {
			GenderEnum gender = GenderEnum.valueOf((String) value);
		} catch (Exception e) {
			throw new SuperCsvConstraintViolationException(
					String.format("The field value - '%s' should be Male/Female", value),
					context, this);

		}

		return next.execute(value, context);
	}
}
