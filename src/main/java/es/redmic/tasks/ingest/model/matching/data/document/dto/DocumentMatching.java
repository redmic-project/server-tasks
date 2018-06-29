package es.redmic.tasks.ingest.model.matching.data.document.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaNotNull;

import es.redmic.tasks.ingest.model.matching.common.dto.ItemCodeDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemKeywordDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemLanguageDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemRemarkDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemURLDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.MatchingCommonDTO;

@JsonSchemaNotNull
public class DocumentMatching extends MatchingCommonDTO {

	@NotNull
	@Valid
	private ItemTitleDTO title;

	@NotNull
	@Valid
	private ItemCodeDTO code;

	@Valid
	private ItemRemarkDTO remark;

	@NotNull
	@Valid
	private ItemAuthorDTO author;

	@NotNull
	@Valid
	private ItemYearDTO year;

	@NotNull
	@Valid
	private ItemSourceDTO source;

	@Valid
	private ItemLanguageDTO language;

	@Valid
	private ItemKeywordDTO keywords;

	@NotNull
	@Valid
	private ItemDocumentTypeDTO documentType;

	@Valid
	private ItemURLDTO url;

	public DocumentMatching() {
		super();
	}

	public ItemTitleDTO getTitle() {
		return title;
	}

	public void setTitle(ItemTitleDTO title) {
		this.title = title;

		if (title != null)
			matchingColumns.addAll(title.getColumns());
	}

	public ItemCodeDTO getCode() {
		return code;
	}

	public void setCode(ItemCodeDTO code) {
		this.code = code;

		if (code != null)
			matchingColumns.addAll(code.getColumns());
	}

	public ItemRemarkDTO getRemark() {
		return remark;
	}

	public void setRemark(ItemRemarkDTO remark) {
		this.remark = remark;

		if (remark != null)
			matchingColumns.addAll(remark.getColumns());
	}

	public ItemAuthorDTO getAuthor() {
		return author;
	}

	public void setAuthor(ItemAuthorDTO author) {

		this.author = author;

		if (author != null)
			matchingColumns.addAll(author.getColumns());
	}

	public ItemYearDTO getYear() {
		return year;
	}

	public void setYear(ItemYearDTO year) {
		this.year = year;

		if (year != null)
			matchingColumns.addAll(year.getColumns());
	}

	public ItemSourceDTO getSource() {
		return source;
	}

	public void setSource(ItemSourceDTO source) {
		this.source = source;

		if (source != null)
			matchingColumns.addAll(source.getColumns());
	}

	public ItemLanguageDTO getLanguage() {
		return language;
	}

	public void setLanguage(ItemLanguageDTO language) {
		this.language = language;

		if (language != null)
			matchingColumns.addAll(language.getColumns());
	}

	public ItemKeywordDTO getKeywords() {
		return keywords;
	}

	public void setKeywords(ItemKeywordDTO keywords) {
		this.keywords = keywords;

		if (keywords != null)
			matchingColumns.addAll(keywords.getColumns());
	}

	public ItemDocumentTypeDTO getDocumentType() {
		return documentType;
	}

	public void setDocumentType(ItemDocumentTypeDTO documentType) {
		this.documentType = documentType;

		if (documentType != null)
			matchingColumns.addAll(documentType.getColumns());
	}

	public ItemURLDTO getUrl() {
		return url;
	}

	public void setUrl(ItemURLDTO url) {
		this.url = url;
	}
}