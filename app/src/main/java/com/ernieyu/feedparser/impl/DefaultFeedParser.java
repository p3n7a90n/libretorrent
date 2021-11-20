package com.ernieyu.feedparser.impl;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.ernieyu.feedparser.XMLInputStream;
import org.xml.sax.XMLReader;

import com.ernieyu.feedparser.Feed;
import com.ernieyu.feedparser.FeedException;
import com.ernieyu.feedparser.FeedParser;

/**
 * Default implementation of FeedParser.  This uses a SAX parser to process
 * the feed.
 */
public class DefaultFeedParser implements FeedParser {

    @Override
    public Feed parse(InputStream inStream) throws FeedException {
        try {
            // Create SAX parser.
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();

            //Disable external dtd and entities
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);



            // Turn on namespace support.
            XMLReader reader = parser.getXMLReader();
            reader.setFeature("http://xml.org/sax/features/namespaces", true);
            reader.setFeature("http://xml.org/sax/features/namespace-prefixes", false);

            // Disable genearal and paramter entities
            reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
            reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            
        	// Create SAX handler.
        	FeedHandler handler = new FeedHandler();
        	
            // Parse feed and return data.
        	parser.parse(new XMLInputStream(inStream), handler);
        	Feed feed = handler.getFeed();
            if (feed == null)
                throw new FeedException("Invalid RSS/Atom feed");
            
            return feed;
        
        } catch (Exception ex) {
        	throw new FeedException(ex);
        }
    }
}
