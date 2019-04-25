package com.bd.data.loader;

import org.apache.log4j.BasicConfigurator;

public class Start {
	public static void main(String[] args) {
		BasicConfigurator.configure();
		ElasticLoader dataLoader = new ElasticLoader();
		dataLoader.process();
	}
}
