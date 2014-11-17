/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.vega.rest;

import io.vertigo.commons.resource.ResourceManager;
import io.vertigo.dynamo.collections.CollectionsManager;
import io.vertigo.dynamo.collections.DtListFunction;
import io.vertigo.dynamo.domain.metamodel.DtDefinition;
import io.vertigo.dynamo.domain.metamodel.DtField;
import io.vertigo.dynamo.domain.model.DtList;
import io.vertigo.dynamo.domain.model.DtObject;
import io.vertigo.dynamo.domain.util.DtObjectUtil;
import io.vertigo.dynamo.export.ExportManager;
import io.vertigo.dynamo.export.model.Export;
import io.vertigo.dynamo.export.model.ExportBuilder;
import io.vertigo.dynamo.export.model.ExportFormat;
import io.vertigo.dynamo.file.FileManager;
import io.vertigo.dynamo.file.model.KFile;
import io.vertigo.dynamo.impl.collections.functions.filter.DtListChainFilter;
import io.vertigo.dynamo.impl.collections.functions.filter.DtListFilter;
import io.vertigo.dynamo.impl.collections.functions.filter.DtListRangeFilter;
import io.vertigo.dynamo.impl.collections.functions.filter.DtListValueFilter;
import io.vertigo.dynamo.impl.collections.functions.filter.FilterFunction;
import io.vertigo.lang.MessageText;
import io.vertigo.lang.Option;
import io.vertigo.lang.VUserException;
import io.vertigo.persona.security.KSecurityManager;
import io.vertigo.util.DateUtil;
import io.vertigo.vega.rest.engine.UiContext;
import io.vertigo.vega.rest.exception.VSecurityException;
import io.vertigo.vega.rest.model.DtListDelta;
import io.vertigo.vega.rest.model.DtObjectExtended;
import io.vertigo.vega.rest.model.UiListState;
import io.vertigo.vega.rest.stereotype.AccessTokenConsume;
import io.vertigo.vega.rest.stereotype.AccessTokenMandatory;
import io.vertigo.vega.rest.stereotype.AccessTokenPublish;
import io.vertigo.vega.rest.stereotype.AnonymousAccessAllowed;
import io.vertigo.vega.rest.stereotype.AutoSortAndPagination;
import io.vertigo.vega.rest.stereotype.DELETE;
import io.vertigo.vega.rest.stereotype.Doc;
import io.vertigo.vega.rest.stereotype.ExcludedFields;
import io.vertigo.vega.rest.stereotype.GET;
import io.vertigo.vega.rest.stereotype.HeaderParam;
import io.vertigo.vega.rest.stereotype.IncludedFields;
import io.vertigo.vega.rest.stereotype.InnerBodyParam;
import io.vertigo.vega.rest.stereotype.POST;
import io.vertigo.vega.rest.stereotype.PUT;
import io.vertigo.vega.rest.stereotype.PathParam;
import io.vertigo.vega.rest.stereotype.PathPrefix;
import io.vertigo.vega.rest.stereotype.QueryParam;
import io.vertigo.vega.rest.stereotype.ServerSideRead;
import io.vertigo.vega.rest.stereotype.ServerSideSave;
import io.vertigo.vega.rest.stereotype.SessionInvalidate;
import io.vertigo.vega.rest.stereotype.SessionLess;
import io.vertigo.vega.rest.stereotype.Validate;
import io.vertigo.vega.rest.validation.UiMessageStack;
import io.vertigo.vega.rest.validation.ValidationUserException;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

//basé sur http://www.restapitutorial.com/lessons/httpmethods.html

@PathPrefix("/test")
public final class TesterRestServices implements RestfulService {

	@Inject
	private KSecurityManager securityManager;
	@Inject
	private CollectionsManager collectionsManager;
	@Inject
	private ExportManager exportManager;
	@Inject
	private ResourceManager resourcetManager;
	@Inject
	private FileManager fileManager;
	@Inject
	private ContactDao contactDao;

	@AnonymousAccessAllowed
	@GET("/login")
	public void login() {
		//code 200
		securityManager.getCurrentUserSession().get().authenticate();
	}

	@SessionInvalidate
	@GET("/logout")
	public void logout() {
		//code 200
	}

	@SessionLess
	@AnonymousAccessAllowed
	@GET("/anonymousTest")
	public List<Contact> anonymousTest() {
		//offset + range ?
		//code 200
		return contactDao.getList();
	}

	@GET("/authentifiedTest")
	public List<Contact> authentifiedTest() {
		//offset + range ?
		//code 200
		return contactDao.getList();
	}

	@Doc("send param type='Confirm' or type = 'Contact' \n Return 'OK' or 'Contact'")
	@GET("/twoResult")
	public UiContext testTwoResult(@QueryParam("type") final String type) {
		final UiContext result = new UiContext();
		if ("Confirm".equals(type)) {
			result.put("message", "Are you sure");
		} else {
			result.put("contact", contactDao.get(1L));
		}
		//offset + range ?
		//code 200
		return result;
	}

	@Doc("Use passPhrase : RtFM")
	@GET("/docTest/{passPhrase}")
	public List<Contact> docTest(@PathParam("passPhrase") final String passPhrase) throws VSecurityException {
		if (!"RtFM".equals(passPhrase)) {
			throw new VSecurityException("Bad passPhrase, check the doc in /catalog");
		}
		return contactDao.getList();
	}

	@GET("/{conId}")
	public Contact testRead(@PathParam("conId") final long conId) {
		final Contact contact = contactDao.get(conId);
		if (contact == null) {
			//404 ?
			throw new VUserException(new MessageText("Contact #" + conId + " unknown", null));
		}
		//200
		return contact;
	}

	@GET("/export/pdf/")
	public KFile testExportContacts() {
		final DtList<Contact> fullList = asDtList(contactDao.getList(), Contact.class);
		final Export export = new ExportBuilder(ExportFormat.PDF, "contacts")
				.beginSheet(fullList, "Contacts").endSheet()
				.withAuthor("vertigo-test")
				.build();

		final KFile result = exportManager.createExportFile(export);
		//200
		return result;
	}

	@GET("/export/pdf/{conId}")
	public KFile testExportContact(@PathParam("conId") final long conId) {
		final Contact contact = contactDao.get(conId);
		final Export export = new ExportBuilder(ExportFormat.PDF, "contact" + conId)
				.beginSheet(contact, "Contact").endSheet()
				.withAuthor("vertigo-test").build();

		final KFile result = exportManager.createExportFile(export);
		//200
		return result;
	}

	@GET("/grantAccess")
	@AccessTokenPublish
	public void testAccessToken() {
		//access token publish
	}

	@GET("/limitedAccess/{conId}")
	@AccessTokenMandatory
	public Contact testAccessToken(@PathParam("conId") final long conId) {
		return testRead(conId);
	}

	@GET("/oneTimeAccess/{conId}")
	@AccessTokenConsume
	public Contact testAccessTokenConsume(@PathParam("conId") final long conId) {
		return testRead(conId);
	}

	@GET("/filtered/{conId}")
	@ExcludedFields({ "birthday", "email" })
	@ServerSideSave
	public Contact testFilteredRead(@PathParam("conId") final long conId) {
		final Contact contact = contactDao.get(conId);
		if (contact == null) {
			//404 ?
			throw new VUserException(new MessageText("Contact #" + conId + " unknown", null));
		}
		//200
		return contact;
	}

	//@POST is non-indempotent
	@POST("/contact")
	public Contact testPost(
			final @Validate({ ContactValidator.class, EmptyPkValidator.class }) Contact contact) {
		if (contact.getName() == null || contact.getName().isEmpty()) {
			throw new VUserException(new MessageText("Name is mandatory", null));
		}
		contactDao.post(contact);
		//code 201 + location header : GET route
		return contact;
	}

	//PUT is indempotent : ID obligatoire
	@PUT("/contact")
	public Contact testUpdate(
			final @Validate({ ContactValidator.class, MandatoryPkValidator.class }) Contact contact) {
		if (contact.getName() == null || contact.getName().isEmpty()) {
			//400
			throw new VUserException(new MessageText("Name is mandatory", null));
		}

		contactDao.put(contact);
		//200
		return contact;
	}

	//PUT is indempotent : ID obligatoire
	@PUT("/contact/{conId}")
	public Contact testUpdateByPath(@PathParam("conId") final long conId,
			final @Validate({ ContactValidator.class, EmptyPkValidator.class }) Contact contact) {
		if (contact.getName() == null || contact.getName().isEmpty()) {
			//400
			throw new VUserException(new MessageText("Name is mandatory", null));
		}
		contact.setConId(conId);
		contactDao.put(contact);
		//200
		return contact;
	}

	//PUT is indempotent : ID obligatoire
	@Doc("Exclude conId and name.")
	@PUT("/filtered/{conId}")
	@ServerSideSave
	public Contact filteredUpdateByExclude(
			final @Validate({ ContactValidator.class, MandatoryPkValidator.class }) @ServerSideRead @ExcludedFields({ "conId", "name" }) Contact contact) {
		if (contact.getName() == null || contact.getName().isEmpty()) {
			//400
			throw new VUserException(new MessageText("Name is mandatory", null));
		}

		contactDao.put(contact);
		//200
		return contact;
	}

	//PUT is indempotent : ID obligatoire
	@Doc("Only accept firstName and email. Will blocked if other fields are send.")
	@PUT("/filteredInclude/{conId}")
	@ServerSideSave
	public Contact filteredUpdateByInclude(//
			final @Validate({ ContactValidator.class, MandatoryPkValidator.class })//
			@ServerSideRead//
			@IncludedFields({ "firstName", "email" }) Contact contact) {
		if (contact.getName() == null || contact.getName().isEmpty()) {
			//400
			throw new VUserException(new MessageText("Name is mandatory", null));
		}

		contactDao.put(contact);
		//200
		return contact;
	}

	@DELETE("/{conId}")
	public void delete(@PathParam("conId") final long conId) {
		if (!contactDao.containsKey(conId)) {
			//404
			throw new VUserException(new MessageText("Contact #" + conId + " unknown", null));
		}
		if (conId < 5) {
			//401
			throw new VUserException(new MessageText("You don't have enought rights", null));
		}
		//200
		contactDao.remove(conId);
	}

	@Doc("Test ws-rest multipart body with objects. Send a body with an object of to field : contactFrom, contactTo. Each one should be an json of Contact.")
	@POST("/innerbody")
	public List<Contact> testInnerBodyObject(@InnerBodyParam("contactFrom") final Contact contactFrom, @InnerBodyParam("contactTo") final Contact contactTo) {
		final List<Contact> result = new ArrayList<>(2);
		result.add(contactFrom);
		result.add(contactTo);
		//offset + range ?
		//code 200
		return result;
	}

	@Doc("Test ws-rest multipart body with primitives. Send a body with an object of to field : contactId1, contactId2. Each one should be an json of long.")
	@ServerSideSave
	@ExcludedFields({ "address", "tels" })
	@POST("/innerLong")
	public DtList<Contact> testInnerBodyLong(@InnerBodyParam("contactId1") final long contactIdFrom, @InnerBodyParam("contactId2") final long contactIdTo) {
		final DtList<Contact> result = new DtList<>(Contact.class);
		result.add(contactDao.get(contactIdFrom));
		result.add(contactDao.get(contactIdTo));
		//offset + range ?
		//code 200
		return result;
	}

	@Doc("Test ws-rest returning UiContext. Send a body with an object of to field : contactId1, contactId2. Each one should be an json of long. You get partial Contacts with clientId in each one")
	@ServerSideSave
	@ExcludedFields({ "conId", "email", "birthday", "address", "tels" })
	@POST("/uiContext")
	public UiContext testInnerBody(@InnerBodyParam("contactId1") final long contactIdFrom, @InnerBodyParam("contactId2") final long contactIdTo) {
		final UiContext uiContext = new UiContext();
		uiContext.put("contactFrom", contactDao.get(contactIdFrom));
		uiContext.put("contactTo", contactDao.get(contactIdTo));
		uiContext.put("testLong", 12);
		uiContext.put("testString", "the String test");
		uiContext.put("testDate", DateUtil.newDate());
		uiContext.put("testEscapedString", "the EscapedString \",} test");
		//offset + range ?
		//code 200
		return uiContext;
	}

	@Doc("Test ws-rest with multiple path params.")
	@ExcludedFields({ "address", "tels" })
	@POST("/multiPath/from/{conIdFrom}/to/{conIdTo}")
	public DtList<Contact> testMultiPathParam(//
			@PathParam("conIdFrom") final long contactIdFrom, //
			@PathParam("conIdTo") final long contactIdTo) {
		final DtList<Contact> result = new DtList<>(Contact.class);
		result.add(contactDao.get(contactIdFrom));
		result.add(contactDao.get(contactIdTo));
		//offset + range ?
		//code 200
		return result;
	}

	@Doc("Test ws-rest multipart body with serverSide objects. Send a body with an object of to field : contactFrom, contactTo. Each one should be an partial json of Contact with clientId.")
	@POST("/innerBodyServerClient")
	public List<Contact> testInnerBodyClientId(//
			@InnerBodyParam("contactFrom") @ServerSideRead final Contact contactFrom, //
			@InnerBodyParam("contactTo") @ServerSideRead final Contact contactTo) {
		final List<Contact> result = new ArrayList<>(2);
		result.add(contactFrom);
		result.add(contactTo);
		//offset + range ?
		//code 200
		return result;
	}

	@POST("/search")
	@ExcludedFields({ "conId", "email", "birthday", "address", "tels" })
	public List<Contact> testSearch(//
			@ExcludedFields({ "conId", "email", "birthday", "address", "tels" }) final ContactCriteria contact) {
		final DtListFunction<Contact> filterFunction = createDtListFunction(contact, Contact.class);
		final DtList<Contact> fullList = asDtList(contactDao.getList(), Contact.class);
		final DtList<Contact> result = filterFunction.apply(fullList);
		//offset + range ?
		//code 200
		return result;
	}

	@POST("/searchPagined")
	@ExcludedFields({ "conId", "email", "birthday", "address", "tels" })
	public List<Contact> testSearchServicePagined(//
			@InnerBodyParam("criteria") final ContactCriteria contact, //
			@InnerBodyParam("listState") final UiListState uiListState) {
		final DtListFunction<Contact> filterFunction = createDtListFunction(contact, Contact.class);
		final DtList<Contact> fullList = asDtList(contactDao.getList(), Contact.class);
		final DtList<Contact> result = filterFunction.apply(fullList);

		//offset + range ?
		//code 200
		return applySortAndPagination(result, uiListState);
	}

	@POST("/searchQueryPagined")
	@ExcludedFields({ "conId", "email", "birthday", "address", "tels" })
	public List<Contact> testSearchServiceQueryPagined(final ContactCriteria contact,
			@QueryParam("") final UiListState uiListState) {
		final DtListFunction<Contact> filterFunction = createDtListFunction(contact, Contact.class);
		final DtList<Contact> fullList = asDtList(contactDao.getList(), Contact.class);
		final DtList<Contact> result = filterFunction.apply(fullList);

		//offset + range ?
		//code 200
		return applySortAndPagination(result, uiListState);
	}

	@AutoSortAndPagination
	@POST("/searchAutoPagined")
	@ExcludedFields({ "conId", "email", "birthday", "address", "tels" })
	public List<Contact> testSearchServiceAutoPagined(final ContactCriteria contact) {
		final DtListFunction<Contact> filterFunction = createDtListFunction(contact, Contact.class);
		final DtList<Contact> fullList = asDtList(contactDao.getList(), Contact.class);
		final DtList<Contact> result = filterFunction.apply(fullList);
		//offset + range ?
		//code 200
		return result;
	}

	@POST("/uiMessage")
	public UiMessageStack testUiMessage(final Contact contact, final UiMessageStack uiMessageStack) {
		uiMessageStack.success("Your message have been received");
		uiMessageStack.info("We can complete messageStack : globaly or field by field");
		uiMessageStack.warning("This field must be read twice !!", contact, "birthday");
		if (uiMessageStack.hasErrors()) {
			throw new ValidationUserException();
		}
		return uiMessageStack;
	}

	@POST("/uploadFile")
	public KFile testUploadFile(final @QueryParam("upfile") KFile inputFile, //
			final @QueryParam("id") Integer id, //
			final @QueryParam("note") String note) {

		return inputFile;
	}

	@GET("/downloadFile")
	public KFile testDownloadFile(final @QueryParam("id") Integer id) {
		final URL imageUrl = resourcetManager.resolve("npi2loup.png");
		final File imageFile = asFile(imageUrl);
		final KFile imageKFile = fileManager.createFile("image" + id + ".png", "image/png", imageFile);
		return imageKFile;
	}

	@GET("/downloadNotModifiedFile")
	public KFile testDownloadNotModifiedFile(final @QueryParam("id") Integer id, final HttpServletResponse response) {
		if (Math.random() > 0.5) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return null;
		}
		return testDownloadFile(id);
	}

	private File asFile(final URL url) {
		File f;
		try {
			f = new File(url.toURI());
		} catch (final URISyntaxException e) {
			f = new File(url.getPath());
		}
		return f;
	}

	@POST("/saveListDelta")
	public String saveListDelta(final DtListDelta<Contact> myList) {
		return "OK : add " + myList.getCreated().size() + " contacts, update " + myList.getUpdated().size() + " contacts, removed " + myList.getDeleted().size();
	}

	@GET("/headerParams")
	@Doc("Just send x-test-param:\"i'ts fine\"")
	public String testHeaderParams(final @HeaderParam("x-test-param") String testParam) {
		if (!"i'ts fine".equals(testParam)) {
			throw new VUserException(new MessageText("Bad param value. Read doc.", null));
		}
		return "OK";
	}

	//PUT is indempotent : ID obligatoire
	@PUT("/contactAliasName/{conId}")
	public Contact testUpdateByPath(@PathParam("conId") final long conId,
			final @Validate({ ContactValidator.class, EmptyPkValidator.class }) Contact contact,
			@InnerBodyParam("itsatoolongaliasforfieldcontactname") final String aliasName) {
		if (contact.getName() == null || contact.getName().isEmpty()) {
			//400
			throw new VUserException(new MessageText("Name is mandatory", null));
		}
		contact.setConId(conId);
		contact.setName(aliasName);
		contactDao.put(contact);
		//200
		return contact;
	}

	@GET("/contactExtended/{conId}")
	public DtObjectExtended<Contact> testGetExtended(@PathParam("conId") final long conId) {
		final Contact contact = contactDao.get(conId);
		final DtObjectExtended<Contact> result = new DtObjectExtended<>(contact);
		result.put("vanillaUnsupportedMultipleIds", new int[] { 1, 2, 3 });
		//200
		return result;
	}

	@PUT("/contactExtended/{conId}")
	public DtObjectExtended<Contact> testGetExtended(@PathParam("conId") final long conId,
			final @Validate({ ContactValidator.class, EmptyPkValidator.class }) Contact contact,
			@InnerBodyParam("vanillaUnsupportedMultipleIds") final int[] multipleIds) {
		contact.setConId(conId);
		contactDao.put(contact);
		final DtObjectExtended<Contact> result = new DtObjectExtended<>(contact);
		result.put("vanillaUnsupportedMultipleIds", multipleIds);
		//200
		return result;
	}

	/*@GET("/searchFacet")
	public FacetedQueryResult<DtObject, ContactCriteria> testSearchServiceFaceted(final ContactCriteria contact) {
		final DtListFunction<Contact> filterFunction = createDtListFunction(contact, Contact.class);
		final DtList<Contact> result = filterFunction.apply((DtList<Contact>) contacts.values());

		//offset + range ?
		//code 200
		return result;
	}*/

	private <D extends DtObject> DtList<D> asDtList(final Collection<D> values, final Class<D> dtObjectClass) {
		final DtList<D> result = new DtList<>(dtObjectClass);
		for (final D element : values) {
			result.add(element);
		}
		return result;
	}

	private <D extends DtObject> DtList<D> applySortAndPagination(final DtList<D> unFilteredList, final UiListState uiListState) {
		final DtList<D> sortedList;
		if (uiListState.getSortFieldName() != null) {
			sortedList = collectionsManager.createDtListProcessor()
					.sort(uiListState.getSortFieldName(), uiListState.isSortDesc(), true, true)
					.apply(unFilteredList);
		} else {
			sortedList = unFilteredList;
		}
		final DtList<D> filteredList;
		if (uiListState.getTop() > 0) {
			final int listSize = sortedList.size();
			final int usedSkip = Math.min(uiListState.getSkip(), listSize);
			final int usedTop = Math.min(usedSkip + uiListState.getTop(), listSize);
			filteredList = collectionsManager.createDtListProcessor()
					.filterSubList(usedSkip, usedTop)
					.apply(sortedList);
		} else {
			filteredList = sortedList;
		}
		return filteredList;
	}

	private static <C extends DtObject, O extends DtObject> DtListFunction<O> createDtListFunction(final C criteria, final Class<O> resultClass) {
		final List<DtListFilter<O>> filters = new ArrayList<>();
		final DtDefinition criteriaDefinition = DtObjectUtil.findDtDefinition(criteria);
		final DtDefinition resultDefinition = DtObjectUtil.findDtDefinition(resultClass);
		final Set<String> alreadyAddedField = new HashSet<>();
		for (final DtField field : criteriaDefinition.getFields()) {
			final String fieldName = field.getName();
			if (!alreadyAddedField.contains(fieldName)) { //when we consume two fields at once (min;max)
				final Object value = field.getDataAccessor().getValue(criteria);
				if (value != null) {
					if (fieldName.endsWith("_MIN") || fieldName.endsWith("_MAX")) {
						final String filteredField = fieldName.substring(0, fieldName.length() - "_MIN".length());
						final DtField resultDtField = resultDefinition.getField(filteredField);
						final DtField minField = fieldName.endsWith("_MIN") ? field : criteriaDefinition.getField(filteredField + "_MIN");
						final DtField maxField = fieldName.endsWith("_MAX") ? field : criteriaDefinition.getField(filteredField + "_MAX");
						final Comparable minValue = (Comparable) minField.getDataAccessor().getValue(criteria);
						final Comparable maxValue = (Comparable) maxField.getDataAccessor().getValue(criteria);
						filters.add(new DtListRangeFilter<O, Comparable>(resultDtField.getName(), Option.<Comparable> option(minValue), Option.<Comparable> option(maxValue), true, false));
					} else {
						filters.add(new DtListValueFilter<O>(field.getName(), (String) value));
					}
				}
			}
			//si null, alors on ne filtre pas
		}
		return new FilterFunction<>(new DtListChainFilter(filters.toArray(new DtListFilter[filters.size()])));
	}

}
