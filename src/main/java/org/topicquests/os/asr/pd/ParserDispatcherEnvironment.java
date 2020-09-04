/**
 * 
 */
package org.topicquests.os.asr.pd;

import org.topicquests.os.asr.pd.api.IDispatcher;
import org.topicquests.support.RootEnvironment;

/**
 * @author jackpark
 *
 */
public class ParserDispatcherEnvironment extends RootEnvironment {
	private IDispatcher dispatcher;
	
	/**
	 */
	public ParserDispatcherEnvironment() {
		super("pd-props.xml", "logger.properties");
		dispatcher = new ParserDispatcher(this);
	}

	/**
	 * Boot and inject parsers into dispatcher
	 */
	private void bootDispatcher() {
		//TODO
	}
	
	public IDispatcher getDispatcher() {
		return dispatcher;
	}
	
	@Override
	public void shutDown() {
		System.out.println("ParserDispatcherEnvironment.shutDown");
		//TODO

	}

}
