package es.redmic.tasks.ingest.model.matching.common.dto;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.batch.item.file.transform.FieldSet;

import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaIgnore;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaNotNull;

@JsonSchemaNotNull
public class ItemDateDTO extends ItemCommonDTO {

	private static final String FIELD = "date";

	private DateTimeFormatter dtf;

	public ItemDateDTO() {
	}

	@NotNull
	private String format;

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
		dtf = DateTimeFormat.forPattern(format);
	}

	@JsonSchemaIgnore
	@JsonIgnore
	@Override
	public String getField() {
		return FIELD;
	}

	@Override
	public DateTime returnValue(FieldSet fs) {

		return dtf.parseDateTime(fs.readString(columns.get(0)));
	}

	@Override
	public Object returnValue(SimpleFeature fs) {

		return dtf.parseDateTime(fs.getAttribute(columns.get(0)).toString());
	}
}