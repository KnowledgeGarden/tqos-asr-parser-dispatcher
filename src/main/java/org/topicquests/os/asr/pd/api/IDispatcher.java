/**
 * 
 */
package org.topicquests.os.asr.pd.api;

import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public interface IDispatcher {

	/**
	 * <p>Take a given {@code sentence} and dispatch it, then accumulate and process
	 * the results and return them.</p>
	 * <p>Given a variety of agents used here, results include
	 * <ul><li>Sentence POS data</li>
	 * <li>tuple data - statements</li></ul>
	 * and the final {@link JSONObject} returned must have fields representing
	 * both classes of results.</p>
	 * <p>This returns two objects: the sentence in returnObjectA, and a list of parse
	 * results in returnObject</p>
	 * @param sentence
	 * @return
	 */
	IResult processSentence(JSONObject sentence);
	
	/**
	 * Parsers <em>plug in</em> from external sources
	 * @param parser
	 */
	void addParser(ISentenceParser parser);
	
	
	void shutDown();
}
