package hu.schonherz.javatraining.issuetracker.web.view;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.diagram.ConnectEvent;
import org.primefaces.event.diagram.DisconnectEvent;
import org.primefaces.model.diagram.Connection;
import org.primefaces.model.diagram.DefaultDiagramModel;
import org.primefaces.model.diagram.Element;
import org.primefaces.model.diagram.connector.FlowChartConnector;
import org.primefaces.model.diagram.connector.StraightConnector;
import org.primefaces.model.diagram.endpoint.DotEndPoint;
import org.primefaces.model.diagram.endpoint.EndPoint;
import org.primefaces.model.diagram.endpoint.EndPointAnchor;
import org.primefaces.model.diagram.endpoint.RectangleEndPoint;
import org.primefaces.model.diagram.overlay.ArrowOverlay;

import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusVo;
import lombok.Data;
import lombok.extern.log4j.Log4j;

@ManagedBean(name = "modifyStatusOrderView")
@ViewScoped
@Log4j
@Data
public class ModifyStatusOrderView implements Serializable {

	private static final String ICON_COLOR = "#98AFC7";
	private static final String ICON_HOVER_COLOR = "#5C738B";

	private DefaultDiagramModel model;
	
	public void init() {
		model = new DefaultDiagramModel();
		model.setMaxConnections(-1);
		model.getDefaultConnectionOverlays().add(new ArrowOverlay(20, 20, 1, 1));
		FlowChartConnector connector = new FlowChartConnector();
		connector.setPaintStyle("{strokeStyle:'#98AFC7', lineWidth:3}");
		connector.setHoverPaintStyle("{strokeStyle:'#5C738B'}");
		model.setDefaultConnector(connector);
	}
	
	public void generateDiagram(List<StatusVo> statuses) {
		statuses.forEach(status -> this.addStatus(status.getName()));
	}
	
	public void addStatus(String status) {
		Element newElement = new Element(status);
		
		EndPoint outPoint = createRectangleEndPoint(EndPointAnchor.BOTTOM);
		outPoint.setSource(true);
		EndPoint inPoint = createDotEndPoint(EndPointAnchor.TOP);
		inPoint.setTarget(true);
		
		newElement.addEndPoint(outPoint);
		newElement.addEndPoint(inPoint);
		
		model.addElement(newElement);
	}
	
	public void modifyStatus(String oldStatus, String newStatus) {
		for (Element element : model.getElements()) {
			if (element.getData().equals(oldStatus)) {
				element.setData(newStatus);
				return;
			}
		}
	}
	
	public void deleteStatus(String status) {
		for (Element element : model.getElements()) {
			if (element.getData().equals(status)) {
				model.removeElement(element);
				return;
			}
		}
	}

	private Connection createConnection(EndPoint from, EndPoint to) {
		Connection conn = new Connection(from, to);
		conn.getOverlays().add(new ArrowOverlay(20, 20, 1, 1));
		return conn;
	}

	public void onConnect(ConnectEvent event) {
		log.debug("Connected from " + event.getSourceElement().getData() + " to " + event.getTargetElement().getData());
	}

	public void onDisconnect(DisconnectEvent event) {
		log.debug("Disconnected from " + event.getSourceElement().getData() + " to " + event.getTargetElement().getData());
	}

	private EndPoint createDotEndPoint(EndPointAnchor anchor) {
		DotEndPoint endPoint = new DotEndPoint(anchor);
		endPoint.setScope("network");
		endPoint.setTarget(true);
		endPoint.setStyle(String.format("{fillStyle:'%s'}", ICON_COLOR));
		endPoint.setHoverStyle(String.format("{fillStyle:'%s'}", ICON_HOVER_COLOR));
		return endPoint;
	}

	private EndPoint createRectangleEndPoint(EndPointAnchor anchor) {
		RectangleEndPoint endPoint = new RectangleEndPoint(anchor);
		endPoint.setScope("network");
		endPoint.setSource(true);
		endPoint.setStyle(String.format("{fillStyle:'%s'}", ICON_COLOR));
		endPoint.setHoverStyle(String.format("{fillStyle:'%s'}", ICON_HOVER_COLOR));
		return endPoint;
	}
}