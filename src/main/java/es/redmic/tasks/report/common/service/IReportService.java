package es.redmic.tasks.report.common.service;

import java.util.ArrayList;
import java.util.List;

import es.redmic.models.birt.Wrapper;

public interface IReportService<TWrapper extends Wrapper> {

	public TWrapper getWrapper();

	public ArrayList<TWrapper> getListWrapper(List<String> ids);

	public ArrayList<TWrapper> getListWrapper();
}
