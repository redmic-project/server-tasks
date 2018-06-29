package es.redmic.tasks.ingest.common.jobs;

import java.util.List;

import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

public abstract class CsvToDbJobConfig {

	protected DelimitedLineTokenizer createTokenizer(List<String> header, String delimiter) {

		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setDelimiter(delimiter);
		tokenizer.setNames(header.stream().toArray(String[]::new));

		return tokenizer;
	}
}
