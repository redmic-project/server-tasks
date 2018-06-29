package es.redmic.tasks.report.species.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.redmic.es.administrative.taxonomy.service.SpeciesESService;
import es.redmic.es.administrative.taxonomy.service.TaxonESService;
import es.redmic.es.config.OrikaScanBeanESItfc;
import es.redmic.models.birt.SpeciesWrapper;
import es.redmic.models.es.administrative.dto.ActivityDTO;
import es.redmic.models.es.administrative.dto.DocumentDTO;
import es.redmic.models.es.administrative.taxonomy.dto.SpeciesDTO;
import es.redmic.models.es.administrative.taxonomy.dto.TaxonDTO;
import es.redmic.models.es.common.dto.JSONCollectionDTO;
import es.redmic.models.es.common.query.dto.DataQueryDTO;
import es.redmic.models.es.common.query.dto.MgetDTO;
import es.redmic.models.es.common.utils.HierarchicalUtils;
import es.redmic.tasks.report.common.repository.ReportRepository;
import es.redmic.tasks.report.common.service.ReportService;

@Service
public class SpeciesTaskReportESService extends ReportService<SpeciesWrapper> {

	@Autowired
	public SpeciesTaskReportESService(ReportRepository repository) {
		super(repository);
	}

	private static String designInfoPath = "SpeciesInfoReport";
	private static String designListPath = "SpeciesListReport";

	@Autowired
	SpeciesESService service;

	@Autowired
	TaxonESService taxonService;

	@Autowired
	OrikaScanBeanESItfc orikaMapper;

	private void setDesigns() {
		setDesignInfo(designInfoPath);
		setDesignList(designListPath);
	}

	@Override
	public SpeciesWrapper getWrapper() {
		return null;
	}

	@Override
	public ArrayList<SpeciesWrapper> getListWrapper() {
		return null;
	}

	@Override
	public ArrayList<SpeciesWrapper> getListWrapper(List<String> ids) {

		setDesigns();

		ArrayList<SpeciesWrapper> data = new ArrayList<SpeciesWrapper>();
		for (int i = 0; i < ids.size(); i++) {
			SpeciesDTO species = service.get(ids.get(i));
			SpeciesWrapper wrap = new SpeciesWrapper();
			wrap.setSpecies(species);

			setSpeciesAncestors(wrap, species.getPath());

			String speciesId = species.getId().toString();
			setSpeciesDocuments(wrap, speciesId);
			setSpeciesActivities(wrap, speciesId);

			data.add(wrap);
		}
		return data;
	}

	@SuppressWarnings({ "unchecked" })
	public void setSpeciesAncestors(SpeciesWrapper wrap, String path) {

		String[] ancestorIds = HierarchicalUtils.getAncestorsIds(path);

		MgetDTO dto = new MgetDTO(Arrays.asList(ancestorIds));
		JSONCollectionDTO result = taxonService.mget(dto);

		List<TaxonDTO> data = result.getData();
		for (int i = 0; i < data.size(); i++) {
			TaxonDTO ancestor = data.get(i);
			Long rankId = ancestor.getRank().getId();
			if (rankId == 1) {
				wrap.setKingdom(ancestor);
			} else if (rankId == 3) {
				wrap.setPhylum(ancestor);
			} else if (rankId == 5) {
				wrap.setSubphylum(ancestor);
			} else if (rankId == 6) {
				wrap.setClasss(ancestor);
			} else if (rankId == 7) {
				wrap.setOrder(ancestor);
			} else if (rankId == 8) {
				wrap.setFamily(ancestor);
			} else if (rankId == 9) {
				wrap.setGenus(ancestor);
			}
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void setSpeciesDocuments(SpeciesWrapper wrap, String id) {

		DataQueryDTO dtoDocuments = new DataQueryDTO();
		List<DocumentDTO> responseDocuments = service.getDocuments(dtoDocuments, id).getData();
		if (responseDocuments.size() > 0) {
			ArrayList<DocumentDTO> documents = (ArrayList<DocumentDTO>) orikaMapper.getMapperFacade()
					.mapAsList(responseDocuments, DocumentDTO.class);
			wrap.setDocuments(documents);
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void setSpeciesActivities(SpeciesWrapper wrap, String id) {

		DataQueryDTO dtoActivities = new DataQueryDTO();
		List<ActivityDTO> responseActivities = service.getActivities(dtoActivities, id).getData();
		if (responseActivities.size() > 0) {
			ArrayList<ActivityDTO> activities = (ArrayList<ActivityDTO>) orikaMapper.getMapperFacade()
					.mapAsList(responseActivities, ActivityDTO.class);
			wrap.setActivities(activities);
		}
	}
}
