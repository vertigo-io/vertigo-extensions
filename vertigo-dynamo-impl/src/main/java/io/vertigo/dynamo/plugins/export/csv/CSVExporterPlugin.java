package io.vertigo.dynamo.plugins.export.csv;

import io.vertigo.commons.codec.CodecManager;
import io.vertigo.dynamo.export.Export;
import io.vertigo.dynamo.export.ExportFormat;
import io.vertigo.dynamo.impl.export.ExporterPlugin;
import io.vertigo.dynamo.impl.export.core.ExportHelper;
import io.vertigo.dynamo.persistence.PersistenceManager;
import io.vertigo.kernel.lang.Assertion;

import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;

/**
 * Plugin d'export CSV.
 *
 * @author pchretien, npiedeloup
 * @version $Id: CSVExporterPlugin.java,v 1.6 2014/01/28 18:49:44 pchretien Exp $
 */
public final class CSVExporterPlugin implements ExporterPlugin {
	private final CodecManager codecManager;
	private final ExportHelper exportHelper;

	/**
	 * Constructeur.
	 * @param codecManager Manager des m�canismes de codage/d�codage. 
	 */
	@Inject
	public CSVExporterPlugin(final PersistenceManager persistenceManager, final CodecManager codecManager) {
		Assertion.checkNotNull(codecManager);
		//---------------------------------------------------------------------
		this.codecManager = codecManager;
		exportHelper = new ExportHelper(persistenceManager);
	}

	/** {@inheritDoc}*/
	public void exportData(final Export export, final OutputStream out) throws IOException {
		new CSVExporter(codecManager, exportHelper).exportData(export, out);
	}

	/** {@inheritDoc}*/
	public boolean accept(final ExportFormat exportFormat) {
		return ExportFormat.CSV.equals(exportFormat);
	}
}
