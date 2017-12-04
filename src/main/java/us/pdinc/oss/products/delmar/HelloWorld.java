package us.pdinc.oss.products.delmar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxSvgCanvas;
import com.mxgraph.view.mxGraph;

public class HelloWorld
{
	public HelloWorld()
	{
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		// Build graph
		graph.getModel().beginUpdate();
		try
		{
			Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80,
					30);
			Object v2 = graph.insertVertex(parent, null, "World!", 240, 150,
					80, 30);
			graph.insertEdge(parent, null, "Edge", v1, v2);
		}
		finally
		{
			graph.getModel().endUpdate();
		}

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try
		{
			docBuilder = docFactory.newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			// ParserConfigurationException should never occur with no configuration, so...
			throw new RuntimeException(e);
		}

		// Prepare SVG document for mxSvgCanvas
		Document doc = docBuilder.newDocument();
		Element svg = doc.createElement("svg");
		svg.setAttribute("xmlns", "http://www.w3.org/2000/svg");
		svg.setAttribute("version", "1.1"); // TODO: What SVG version is jgraphx compliant with and why?
		doc.appendChild(svg);

		// Render to SVG
		mxICanvas canvas = new mxSvgCanvas(doc);
		graph.drawGraph(canvas);

		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try
		{
			transformer = tFactory.newTransformer();
		}
		catch (TransformerConfigurationException e)
		{
			// TransformerConfigurationException should never occur with no configuration, so...
			throw new RuntimeException(e);
		}

		// Output SVG to System.out
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(System.out);
		try
		{
			transformer.transform(source, result);
		}
		catch (TransformerException e)
		{
			// Cannot recover from TransformerException in this case.
			throw new RuntimeException(e);
		}
	}
}
