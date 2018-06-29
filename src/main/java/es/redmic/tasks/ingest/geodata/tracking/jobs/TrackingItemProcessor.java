package es.redmic.tasks.ingest.geodata.tracking.jobs;

import java.util.List;

import org.joda.time.DateTime;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.vividsolutions.jts.geom.Coordinate;

import es.redmic.exception.tasks.ingest.IngestItemProcessorDataValueException;
import es.redmic.exception.tasks.ingest.IngestItemProcessorGeometryException;
import es.redmic.exception.tasks.ingest.IngestItemProcessorOrderValueException;
import es.redmic.models.es.geojson.common.utils.GeometryUtils;
import es.redmic.models.es.geojson.tracking.common.ElementTrackingDTO;
import es.redmic.models.es.geojson.tracking.common.ElementTrackingPropertiesDTO;

public class TrackingItemProcessor implements ItemProcessor<List<ElementTrackingDTO>, List<ElementTrackingDTO>> {

	private ElementTrackingDTO previousItem;

	private static String CRS_NAME_DEFAULT = "EPSG:4326";
	private CoordinateReferenceSystem CRS_DEFAULT = GeometryUtils.getCRS(CRS_NAME_DEFAULT);

	private String taskId;

	private final Logger LOGGER = LoggerFactory.getLogger(TrackingItemProcessor.class);

	public TrackingItemProcessor(String taskId) {
		this.taskId = taskId;
	}

	@Override
	public List<ElementTrackingDTO> process(List<ElementTrackingDTO> items) throws Exception {

		for (ElementTrackingDTO item : items) {

			Boolean isValidItem = checkItemIsValid(item);

			if (!isValidItem) {
				// completa los datos calculados a 0
				resetCalculatedValues(item);
			} else {
				if (previousItem == null)
					resetCalculatedValues(item);
				else
					calculateValues(previousItem, item);
				previousItem = item;
			}
		}
		return items;
	}

	private Boolean checkItemIsValid(ElementTrackingDTO item) {

		if (item.getProperties() != null && item.getProperties().getqFlag().equals('1'))
			return true;
		return false;
	}

	private void resetCalculatedValues(ElementTrackingDTO item) {

		item.getProperties().setSpeedKph(0.0);
		item.getProperties().setHours(0.0);
		item.getProperties().setCumulativeTime(0.0);
		item.getProperties().setCumulativeKm(0.0);
		item.getProperties().setLastDistanceKm(0.0);
	}

	private void calculateValues(ElementTrackingDTO previousItem, ElementTrackingDTO currentItem) {

		ElementTrackingPropertiesDTO previousProperties = previousItem.getProperties(),
				currentProperties = currentItem.getProperties();

		Double timeToPreviousPoint = getTimeToPreviousPoint(previousProperties.getDate(), currentProperties.getDate()),
				cumulativeTime = getCumulativeTime(previousProperties.getCumulativeTime(), timeToPreviousPoint),
				// TODO: Cuando se especifique en el dto el crs, usarlo, en caso
				// contrario, usar el de por defecto.
				distanceToPreviousPoint = getDistanceToPreviousPoint(previousItem.getGeometry().getCoordinate(),
						currentItem.getGeometry().getCoordinate(), CRS_DEFAULT),
				cumulativeDistance = getCumulativeDistance(previousProperties.getCumulativeKm(),
						distanceToPreviousPoint),
				speedToPreviousPoint = getSpeedToPreviousPoint(distanceToPreviousPoint, timeToPreviousPoint);

		currentItem.getProperties().setHours(timeToPreviousPoint);
		currentItem.getProperties().setCumulativeTime(cumulativeTime);
		currentItem.getProperties().setLastDistanceKm(distanceToPreviousPoint);
		currentItem.getProperties().setCumulativeKm(cumulativeDistance);
		currentItem.getProperties().setSpeedKph(speedToPreviousPoint);
	}

	private Double getTimeToPreviousPoint(DateTime previousDate, DateTime currentDate) {

		if (previousDate == null || currentDate == null) {
			LOGGER.debug("Error calculando el tiempo al punto anterior, previousDate o currentDate = null");
			throw new IngestItemProcessorDataValueException(taskId);
		}
		if (previousDate.getMillis() > currentDate.getMillis()) {
			LOGGER.debug("Error calculando el tiempo al punto anterior. Fechas desordenadas");
			throw new IngestItemProcessorOrderValueException(taskId);
		}
		Double diffInMillis = (double) (currentDate.getMillis() - previousDate.getMillis());

		if (diffInMillis == null || diffInMillis == 0) {
			LOGGER.debug("Error calculando el tiempo al punto anterior. Fechas idénticas");
			throw new IngestItemProcessorDataValueException(taskId);
		}
		return (diffInMillis / 3600000);
	}

	private Double getCumulativeTime(Double previusCumulativeTime, Double timeToPreviousPoint) {

		if (previusCumulativeTime == null || timeToPreviousPoint == null) {
			LOGGER.debug("Error calculando el tiempo acumulado, previusCumulativeTime o timeToPreviousPoint = null");
			throw new IngestItemProcessorDataValueException(taskId);
		}

		return previusCumulativeTime + timeToPreviousPoint;
	}

	private Double getDistanceToPreviousPoint(Coordinate previousPointCoordinate, Coordinate currentPointCoordinate,
			CoordinateReferenceSystem crs) {

		if (previousPointCoordinate == null || currentPointCoordinate == null) {
			LOGGER.debug(
					"Error calculando la distancia al punto anterior, previousPointCoordinate o currentPointCoordinate = null");
			throw new IngestItemProcessorGeometryException(taskId);
		}
		Double distanceInMeters = 0.0;

		try {
			distanceInMeters = GeometryUtils.getDistanceInMeters(previousPointCoordinate, currentPointCoordinate, crs);
		} catch (Exception e) {
			LOGGER.debug("Error calculando la distancia al punto anterior, geometría incorrecta");
			throw new IngestItemProcessorGeometryException(taskId);
		}

		if (distanceInMeters.equals(0.0))
			return 0.0;

		return distanceInMeters / 1000;
	}

	private Double getCumulativeDistance(Double previusCumulativeDistance, Double distanceToPreviousPoint) {

		if (previusCumulativeDistance == null || distanceToPreviousPoint == null) {
			LOGGER.debug(
					"Error calculando la distancia acumulada, previusCumulativeDistance o distanceToPreviousPoint = null");
			throw new IngestItemProcessorDataValueException(taskId);
		}
		return previusCumulativeDistance + distanceToPreviousPoint;
	}

	private Double getSpeedToPreviousPoint(Double distanceToPreviousPoint, Double timeToPreviousPoint) {

		if (distanceToPreviousPoint == null || timeToPreviousPoint == null || timeToPreviousPoint == 0) {
			LOGGER.debug(
					"Error calculando velocidad, distanceToPreviousPoint o timeToPreviousPoint = null o timeToPreviousPoint = 0");
			throw new IngestItemProcessorDataValueException(taskId);
		}
		return (distanceToPreviousPoint / timeToPreviousPoint);
	}
}