/**
 * 
 */
package org.topicquests.os.asr.pd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.topicquests.os.asr.pd.api.IDispatcher;
import org.topicquests.os.asr.pd.api.ISentenceParser;
import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class ParserDispatcher implements IDispatcher {
	private ParserDispatcherEnvironment environment;
	private List<ISentenceParser> parsers;
	private final String ParserDispatcherEnvironment = "sentence";
	
	/**
	 * @param env
	 */
	public ParserDispatcher(ParserDispatcherEnvironment env) {
		environment = env;
		parsers = new ArrayList<ISentenceParser>();
		
	}

	@Override
	public IResult processSentence(JSONObject sentence) {
		IResult result = new ResultPojo();
		List<JSONObject> results = new ArrayList<JSONObject>();
		result.setResultObjectA(sentence);
		result.setResultObject(results);
		String text = sentence.getAsString(ParserDispatcherEnvironment);
		IResult r;
		Iterator<ISentenceParser> itr = parsers.iterator();
		while (itr.hasNext()) {
			r = itr.next().processSentence(text);
			if (r.getResultObject() != null)
				results.add((JSONObject)r.getResultObject());
		}
		return result;
	}

	@Override
	public void addParser(ISentenceParser parser) {
		parsers.add(parser);
	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub

	}

}
