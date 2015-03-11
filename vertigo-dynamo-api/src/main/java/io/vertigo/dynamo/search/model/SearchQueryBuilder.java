package io.vertigo.dynamo.search.model;

import io.vertigo.dynamo.collections.ListFilter;
import io.vertigo.dynamo.collections.metamodel.FacetDefinition;
import io.vertigo.dynamo.collections.metamodel.FacetedQueryDefinition;
import io.vertigo.dynamo.collections.model.FacetedQuery;
import io.vertigo.dynamo.domain.metamodel.DtField;
import io.vertigo.lang.Assertion;
import io.vertigo.lang.Builder;
import io.vertigo.lang.Option;

import java.util.Arrays;

/**
 * @author pchretien
 */
public final class SearchQueryBuilder implements Builder<SearchQuery> {

	private final ListFilter myListFilter;
	//-----
	private DtField mySortField;
	private boolean mySortAsc;
	//-----
	private DtField myBoostedDocumentDateField;
	private Integer myNumDaysOfBoostRefDocument;
	private Integer myMostRecentBoost;
	private FacetedQuery myFacetedQuery;
	private FacetDefinition myClusteringFacetDefinition;

	/**
	 * Constructor.
	 * @param query Query
	 */
	public SearchQueryBuilder(final String query) {
		Assertion.checkNotNull(query);
		//-----
		myListFilter = new ListFilter(query);
	}

	/**
	 * @param sortField Champ utilisé pour le tri
	 * @param sortAsc  Ordre de tri (true pour ascendant)
	 * @return this builder
	*/
	public SearchQueryBuilder withSortStrategy(final DtField sortField, final boolean sortAsc) {
		Assertion.checkNotNull(sortField);
		//-----
		mySortField = sortField;
		mySortAsc = sortAsc;
		return this;
	}

	/**
	 * Defines Boost strategy  including most recents docs.
	 * @param boostedDocumentDateField Nom du champ portant la date du document (null si non utilisé)
	 * @param numDaysOfBoostRefDocument Age des documents servant de référence pour le boost des plus récents par rapport à eux (null si non utilisé)
	 * @param mostRecentBoost Boost relatif maximum entre les plus récents et ceux ayant l'age de référence (doit être > 1) (null si non utilisé)
	 * @return SearchQuery.
	 */
	public SearchQueryBuilder withBoostStrategy(final DtField boostedDocumentDateField, final int numDaysOfBoostRefDocument, final int mostRecentBoost) {
		Assertion.checkNotNull(boostedDocumentDateField);
		Assertion.checkArgument(numDaysOfBoostRefDocument > 1 && mostRecentBoost > 1, "numDaysOfBoostRefDocument et mostRecentBoost doivent être strictement supérieurs à 1.");
		//-----
		myBoostedDocumentDateField = boostedDocumentDateField;
		myNumDaysOfBoostRefDocument = numDaysOfBoostRefDocument;
		myMostRecentBoost = mostRecentBoost;
		return this;
	}

	/**
	 * @param facetedQueryDefinition FacetedQueryDefinition
	 * @param listFilters ListFilter of selected facets
	 * @return this builder
	 */
	public SearchQueryBuilder withFacetStrategy(final FacetedQueryDefinition facetedQueryDefinition, final ListFilter... listFilters) {
		return this.withFacetStrategy(new FacetedQuery(facetedQueryDefinition, Arrays.asList(listFilters)));
	}

	/**
	 * @param facetedQuery FacetedQuery
	 * @return this builder
	 */
	public SearchQueryBuilder withFacetStrategy(final FacetedQuery facetedQuery) {
		Assertion.checkNotNull(facetedQuery);
		//-----
		myFacetedQuery = facetedQuery;
		return this;
	}

	/**
	 * Add a clustering of result by Facet.
	 * @param clusteringFacetDefinition facet used to cluster data
	 * @return this builder
	 */
	public SearchQueryBuilder withFacetClustering(final FacetDefinition clusteringFacetDefinition) {
		Assertion.checkNotNull(clusteringFacetDefinition);
		//-----
		myClusteringFacetDefinition = clusteringFacetDefinition;
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public SearchQuery build() {
		return new SearchQuery(Option.option(myFacetedQuery), myListFilter, mySortField, mySortAsc, myClusteringFacetDefinition, myBoostedDocumentDateField, myNumDaysOfBoostRefDocument, myMostRecentBoost);
	}
}