package es.redmic.test.tasks.utils.mapper;

import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

import es.redmic.es.common.objectFactory.ModelESFactory;
import es.redmic.es.geodata.common.objectfactory.GeometryESFactory;
import es.redmic.models.es.common.model.BaseES;
import ma.glasnost.orika.Converter;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.Filter;
import ma.glasnost.orika.Mapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.ObjectFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;

/**
 * A bean mapper designed for Spring suitable for dependency injection.
 * 
 * Provides an implementation of {@link MapperFacade} which can be injected. In
 * addition it is "Spring aware" in that it can autodiscover any implementations
 * of {@link Mapper} or {@link Converter} that are managed beans within it's
 * parent {@link ApplicationContext}.
 * 
 * @author Ken Blair
 */
@Component
public class OrikaScanBeanTest extends ConfigurableMapper {

	public OrikaScanBeanTest() {
		super(false);
		factory = new DefaultMapperFactory.Builder().build();

		addDefaultActions();
	}

	private MapperFactory factory;

	private void addDefaultActions() {

		addConverter(new PassThroughConverter(org.joda.time.DateTime.class));
		addMapper(new CustomMapper<Point, Point>() {
		});
		addObjectFactory(new GeometryESFactory<Point>(), TypeFactory.<Point>valueOf(Point.class));
		addObjectFactory(new ModelESFactory(), TypeFactory.<BaseES<?>>valueOf(BaseES.class));
		addConverter(new PassThroughConverter(DateTime.class));
		addConverter(new PassThroughConverter(Point.class));
		addConverter(new PassThroughConverter(LineString.class));
	}

	public <T> void addObjectFactory(ObjectFactory<T> objectFactory, Type<T> targetType) {

		factory.registerObjectFactory(objectFactory, targetType);
	}

	public void addConverter(final Converter<?, ?> converter) {

		factory.getConverterFactory().registerConverter(converter);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addMapper(final CustomMapper<?, ?> mapper) {

		factory.classMap(mapper.getAType(), mapper.getBType()).byDefault().customize((CustomMapper) mapper).register();
	}

	public void addFilter(final Filter<?, ?> filter) {
		factory.registerFilter(filter);
	}

	public MapperFacade getMapperFacade() {
		return factory.getMapperFacade();
	}

	@Override
	public void configureFactoryBuilder(final DefaultMapperFactory.Builder factoryBuilder) {
	}

	@Override
	public void configure(final MapperFactory factory) {
	}
}