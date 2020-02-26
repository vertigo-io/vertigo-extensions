package io.vertigo.datafactory.search.data;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.vertigo.commons.transaction.VTransactionManager;
import io.vertigo.core.node.component.Component;
import io.vertigo.core.node.definition.DefinitionProvider;
import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.node.definition.DefinitionSupplier;
import io.vertigo.core.util.ListBuilder;
import io.vertigo.datafactory.collections.metamodel.FacetDefinition.FacetOrder;
import io.vertigo.datafactory.collections.metamodel.FacetRangeDefinitionSupplier;
import io.vertigo.datafactory.collections.metamodel.FacetTermDefinitionSupplier;
import io.vertigo.datafactory.collections.metamodel.FacetedQueryDefinitionSupplier;
import io.vertigo.datafactory.collections.model.FacetedQueryResult;
import io.vertigo.datafactory.collections.model.SelectedFacetValues;
import io.vertigo.datafactory.search.SearchManager;
import io.vertigo.datafactory.search.data.domain.Item;
import io.vertigo.datafactory.search.metamodel.SearchIndexDefinition;
import io.vertigo.datafactory.search.metamodel.SearchIndexDefinitionSupplier;
import io.vertigo.datafactory.search.model.SearchQuery;
import io.vertigo.datafactory.search.model.SearchQueryBuilder;
import io.vertigo.datamodel.structure.model.DtListState;
import io.vertigo.datamodel.structure.model.UID;

public class ItemSearchClient implements Component, DefinitionProvider {

	private final SearchManager searchManager;
	private final VTransactionManager transactionManager;

	/**
	 * Contructeur.
	 * @param searchManager Search Manager
	 * @param transactionManager Transaction Manager
	 */
	@Inject
	public ItemSearchClient(

			final SearchManager searchManager,
			final VTransactionManager transactionManager) {
		this.searchManager = searchManager;
		this.transactionManager = transactionManager;
	}

	public SearchQueryBuilder createSearchQueryBuilderItemFacet(final String criteria, final SelectedFacetValues selectedFacetValues) {
		return SearchQuery.builder("QryItemFacet")
				.withCriteria(criteria)
				.withFacet(selectedFacetValues);
	}

	public SearchQueryBuilder createSearchQueryBuilderItemOptionalFacet(final java.lang.String criteria, final SelectedFacetValues selectedFacetValues) {
		return SearchQuery.builder("QryItemOptionalFacet")
				.withCriteria(criteria)
				.withFacet(selectedFacetValues);
	}

	public SearchQueryBuilder createSearchQueryBuilderItemFacetMulti(final String criteria, final SelectedFacetValues selectedFacetValues) {
		return SearchQuery.builder("QryItemFacetMulti")
				.withCriteria(criteria)
				.withFacet(selectedFacetValues);
	}

	public SearchQueryBuilder createSearchQueryBuilderItemFacetGeo(final Item criteria, final SelectedFacetValues selectedFacetValues) {
		return SearchQuery.builder("QryItemFacetGeo")
				.withCriteria(criteria)
				.withFacet(selectedFacetValues);

	}

	/**
	 * Récupération du résultat issu d'une requête.
	 * @param searchQuery critères initiaux
	 * @param listState Etat de la liste (tri et pagination)
	 * @return Résultat correspondant à la requête (de type BaseIndex)
	 */
	public FacetedQueryResult<Item, SearchQuery> loadList(final SearchQuery searchQuery, final DtListState listState) {
		final SearchIndexDefinition indexDefinition = searchManager.findFirstIndexDefinitionByKeyConcept(Item.class);
		return searchManager.loadList(indexDefinition, searchQuery, listState);
	}

	/**
	 * Mark an entity as dirty. Index of these elements will be reindexed if Tx commited.
	 * Reindexation isn't synchrone, strategy is dependant of plugin's parameters.
	 *
	 * @param entityUID Key concept's UID
	 */
	public void markAsDirty(final UID<Item> entityUID) {
		transactionManager.getCurrentTransaction().addAfterCompletion((final boolean txCommitted) -> {
			if (txCommitted) {// reindex only is tx successful
				searchManager.markAsDirty(Arrays.asList(entityUID));
			}
		});
	}

	/**
	 * Mark an entity as dirty. Index of these elements will be reindexed if Tx commited.
	 * Reindexation isn't synchrone, strategy is dependant of plugin's parameters.
	 *
	 * @param entity Key concept
	 */
	public void markAsDirty(final Item entity) {
		markAsDirty(UID.of(entity));
	}

	/** {@inheritDoc} */
	@Override
	public List<DefinitionSupplier> get(final DefinitionSpace definitionSpace) {
		return new ListBuilder<DefinitionSupplier>()
				//---
				// SearchIndexDefinition
				//-----
				.add(new SearchIndexDefinitionSupplier("IdxItem")
						.withKeyConcept("DtItem")
						.withIndexDtDefinition("DtItem")
						.withLoaderId("ItemSearchLoader")
						.withCopyToFields("allText", "manufacturer", "model", "description", "year", "kilo", "price", "motorType"))

				//---
				// FacetTermDefinition
				//-----
				.add(new FacetTermDefinitionSupplier("FctDescriptionItem")
						.withDtDefinition("DtItem")
						.withFieldName("description")
						.withLabel("Description")
						.withOrder(FacetOrder.count))

				.add(new FacetTermDefinitionSupplier("FctOptionalStringItem")
						.withDtDefinition("DtItem")
						.withFieldName("optionalString")
						.withLabel("optionalString")
						.withOrder(FacetOrder.count))

				.add(new FacetTermDefinitionSupplier("FctManufacturerItem")
						.withDtDefinition("DtItem")
						.withFieldName("manufacturer")
						.withLabel("Par constructeur")
						.withOrder(FacetOrder.count))

				.add(new FacetTermDefinitionSupplier("FctManufacturerItemAlpha")
						.withDtDefinition("DtItem")
						.withFieldName("manufacturer")
						.withLabel("Par constructeur")
						.withOrder(FacetOrder.alpha))

				.add(new FacetTermDefinitionSupplier("FctManufacturerItemMulti")
						.withDtDefinition("DtItem")
						.withFieldName("manufacturer")
						.withLabel("Par constructeur")
						.withMultiSelectable()
						.withOrder(FacetOrder.alpha))

				.add(new FacetRangeDefinitionSupplier("FctYearItem")
						.withDtDefinition("DtItem")
						.withFieldName("year")
						.withLabel("Par date")
						.withRange("R1", "year:[* TO 2000]", "avant 2000")
						.withRange("R2", "year:[2000 TO 2005]", "2000-2005")
						.withRange("R3", "year:[2005 TO *]", "apres 2005")
						.withOrder(FacetOrder.definition))

				.add(new FacetRangeDefinitionSupplier("FctLocalisationItem")
						.withDtDefinition("DtItem")
						.withLabel("Par distance")
						.withFieldName("localisation")
						//.withOrigin("#loc1#")
						.withRange("R1", "localisation:#loc1#~5km", " < 5km")
						.withRange("R2", "localisation:#loc1#~10km", "< 10km")
						.withRange("R3", "localisation:#loc1#~20km", "< 20km")
						.withRange("R4", "localisation:#loc1#~50km", "< 50km")
						.withOrder(FacetOrder.definition))

				//---
				// FacetedQueryDefinition
				//-----

				.add(new FacetedQueryDefinitionSupplier("QryItemFacet")
						.withListFilterBuilderClass(io.vertigo.dynamox.search.DslListFilterBuilder.class)
						.withListFilterBuilderQuery("description:#query# manufacturer:#query#")
						.withCriteriaSmartType("STyString")
						.withFacet("FctDescriptionItem")
						.withFacet("FctManufacturerItem")
						.withFacet("FctManufacturerItemAlpha")
						.withFacet("FctYearItem"))

				.add(new FacetedQueryDefinitionSupplier("QryItemOptionalFacet")
						.withListFilterBuilderClass(io.vertigo.dynamox.search.DslListFilterBuilder.class)
						.withListFilterBuilderQuery("description:#query# manufacturer:#query#")
						.withCriteriaSmartType("STyString")
						.withFacet("FctOptionalStringItem"))

				.add(new FacetedQueryDefinitionSupplier("QryItemFacetMulti")
						.withListFilterBuilderClass(io.vertigo.dynamox.search.DslListFilterBuilder.class)
						.withListFilterBuilderQuery("description:#query# manufacturer:#query#")
						.withCriteriaSmartType("STyString")
						.withFacet("FctDescriptionItem")
						.withFacet("FctManufacturerItemMulti")
						.withFacet("FctYearItem"))

				.add(new FacetedQueryDefinitionSupplier("QryItemFacetGeo")
						.withListFilterBuilderClass(io.vertigo.dynamox.search.DslListFilterBuilder.class)
						.withListFilterBuilderQuery("description:#+description*#")
						.withGeoSearchQuery("localisation:#loc1#~20km") // distance
						//						"geo_distance" : {
						//		                    "distance" : "12km",
						//		                    "pin.location" : "40,-70"
						//		                }
						//.withGeoSearchQuery("localisation:[#locUL# TO #locBR#]") // boundingBox
						//						"geo_bounding_box" : {
						//		                    "pin.location" : {
						//		                        "wkt" : "BBOX (-74.1, -71.12, 40.73, 40.01)"
						//		                    }
						//		                }

						//.withGeoSearchQuery("localisation:#loc1#/5") // geohash precision 5 (~ 4.9km x 4.9km)
						.withCriteriaSmartType("STyString")
						.withFacet("FctLocalisationItem"))

				.build();
	}

}