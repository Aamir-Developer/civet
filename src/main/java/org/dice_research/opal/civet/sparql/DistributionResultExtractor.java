package org.dice_research.opal.civet.sparql;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.access.ResultsContainer;
import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.exceptions.ParsingException;

public class DistributionResultExtractor {

	protected static final Logger LOGGER = LogManager.getLogger();
	protected static final String VAR_DATASET = DatasetQueryBuilder.VAR_DATASET;
	protected static final String VAR_DISTRIBUTION = DistributionQueryBuilder.VAR_DISTRIBUTION;

	public void extractResults(ResultSet resultSet, ResultsContainer resultsContainer) {

		// Process results
		Map<String, Set<String>> data = new HashMap<>();
		while (resultSet.hasNext()) {
			QuerySolution querySolution = resultSet.next();
			String dataset = null;

			// Collect data in result
			Iterator<String> iterator = querySolution.varNames();
			while (iterator.hasNext()) {
				String id = iterator.next();
				if (id.equals(VAR_DISTRIBUTION)) {
					// variable just used to access data
					continue;
				} else if (id.equals(VAR_DATASET)) {
					dataset = querySolution.get(id).toString().trim();
					continue;
				}
				if (!data.containsKey(id)) {
					data.put(id, new HashSet<String>());
				}
				data.get(id).add(querySolution.get(id).toString().trim());
			}

			// Put data in container
			if (dataset != null) {
				DataContainer container = resultsContainer.dataContainers.get(dataset);
				for (String id : data.keySet()) {
					for (String value : data.get(id)) {
						try {
							container.getDataObject(id).addValueUnique(value);
						} catch (ParsingException e) {
							LOGGER.error(e);
						}
					}
				}
			}
		}
	}
}