package com.home.nms.client;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class NMS implements EntryPoint {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final NMSServiceAsync nmsService = GWT.create(NMSService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		MessageBox.info("Message", "Hello World!!", null);
		}

	}
