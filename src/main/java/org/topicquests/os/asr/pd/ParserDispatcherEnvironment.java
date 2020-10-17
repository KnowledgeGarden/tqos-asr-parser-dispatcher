/**
 * 
 */
package org.topicquests.os.asr.pd;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.topicquests.ks.kafka.KafkaHandler;
import org.topicquests.os.asr.pd.api.IDispatcher;
import org.topicquests.os.asr.pd.api.ISentenceParser;
import org.topicquests.support.RootEnvironment;
import org.topicquests.support.config.Configurator;
import org.topicquests.backside.kafka.consumer.api.IMessageConsumerListener;

/**
 * @author jackpark
 *
 */
public class ParserDispatcherEnvironment extends RootEnvironment {
	private IDispatcher dispatcher;
	private KafkaHandler consumer;
	private Map<String,Object>kafkaProps;

	/**
	 */
	public ParserDispatcherEnvironment() {
		super("pd-props.xml", "logger.properties");
		dispatcher = new ParserDispatcher(this);
		kafkaProps = Configurator.getProperties("kafka-topics.xml");
		consumer = new KafkaHandler(this, (IMessageConsumerListener)dispatcher);
		((ParserDispatcher)dispatcher).setKafkaHandler(consumer);
		bootDispatcher();
	}

	/**
	 * Boot and inject parsers into dispatcher
	 */
	private void bootDispatcher() {
		List<List<String>> parsers = (List<List<String>>)getProperty("Parsers");
		//System.out.println("PSRS "+parsers);
		if (parsers != null && !parsers.isEmpty()) { 
			Iterator<List<String>> itr = parsers.iterator();
			List<String> l;
			ISentenceParser p;
			try {
				while (itr.hasNext()) {
					l = itr.next();
					Class cls = Class.forName(l.get(1));
		         
					// returns the name and package of the class
					System.out.println("Class found = " + cls.getName());
					p = (ISentenceParser)cls.newInstance();
					dispatcher.addParser(p);
				}
		      } catch(Exception ex) {
		    	  ex.printStackTrace();
		    	  logError(ex.getMessage(), ex);
		      }
		}
	       
	}
	
	public Map<String, Object> getKafkaTopicProperties() {
		return kafkaProps;
	}

	
	public IDispatcher getDispatcher() {
		return dispatcher;
	}
	
	@Override
	public void shutDown() {
		System.out.println("ParserDispatcherEnvironment.shutDown");
		dispatcher.shutDown();

	}

}
