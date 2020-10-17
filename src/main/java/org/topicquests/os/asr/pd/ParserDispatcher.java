/**
 * 
 */
package org.topicquests.os.asr.pd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.topicquests.backside.kafka.consumer.api.IMessageConsumerListener;
import org.topicquests.ks.kafka.KafkaHandler;
import org.topicquests.os.asr.pd.api.IDispatcher;
import org.topicquests.os.asr.pd.api.ISentenceParser;
import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

/**
 * @author jackpark
 *
 */
public class ParserDispatcher implements IDispatcher, IMessageConsumerListener {
	private ParserDispatcherEnvironment environment;
	private List<ISentenceParser> parsers;
	private KafkaHandler handler;
	
	/**
	 * @param env
	 */
	public ParserDispatcher(ParserDispatcherEnvironment env) {
		environment = env;
		parsers = new ArrayList<ISentenceParser>();
		
	}

	protected void setKafkaHandler(KafkaHandler h) {
		handler = h;
	}

	@Override
	public IResult processSentence(JSONObject sentence) {
		IResult result = new ResultPojo();
		List<JSONObject> results = new ArrayList<JSONObject>();
		// this carries the original, including docId, paraId, sentId, and text
		result.setResultObject(sentence);
		// this carries all the guts of the parses + results here
		result.setResultObject(results);
		String text = sentence.getAsString("text");
		IResult r;
		Iterator<ISentenceParser> itr = parsers.iterator();
		while (itr.hasNext()) {
			r = itr.next().processSentence(text);
			if (r.getResultObject() != null)
				results.add((JSONObject)r.getResultObject());
		}
		sentence.put("results", results);
		return result;
	}

	@Override
	public void addParser(ISentenceParser parser) {
		parsers.add(parser);
		System.out.println("Disp "+parsers);
	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub

	}

	boolean processRecord(String jsonRecord) {
		boolean result = true;
		JSONObject record = toJSONObject(jsonRecord);
		// cargo is in the form
		// { docId, paraId, sentId, text }
		JSONObject sentence = (JSONObject)record.get("cargo");
		IResult r = this.processSentence(sentence);
		if (r.hasError())
			return false;
		return result;
	}
	
	@Override
	public boolean acceptRecord(ConsumerRecord record) {
		String json = (String)record.value();
		environment.logDebug("SentenceProcessor.acceptRecord "+json);
		boolean result = processRecord(json);

		return result;	
	}
	
	JSONObject toJSONObject(String json) {
		JSONObject result = null;
		try {
			JSONParser p = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
			result = (JSONObject)p.parse(json);
		} catch (Exception e) {
			e.printStackTrace();
			environment.logError(e.getMessage(), e);
		}
		return result;
	}

}
