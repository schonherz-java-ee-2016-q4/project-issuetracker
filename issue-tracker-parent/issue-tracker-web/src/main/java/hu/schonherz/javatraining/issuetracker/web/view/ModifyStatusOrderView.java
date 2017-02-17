package hu.schonherz.javatraining.issuetracker.web.view;

import java.io.Serializable;
import java.util.ArrayList;
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
	private static final int LINE_WIDTH = 3;

	private DefaultDiagramModel model;
	private List<StatusOrderViewModel> statusOrders;
	
	public void init() {
		model = new DefaultDiagramModel();
		model.setMaxConnections(-1);
		model.getDefaultConnectionOverlays().add(new ArrowOverlay(20, 20, 1, 1));
		FlowChartConnector connector = new FlowChartConnector();
		connector.setPaintStyle(String.format("{strokeStyle:'%s', lineWidth:%d}", ICON_COLOR, LINE_WIDTH));
		connector.setHoverPaintStyle(String.format("{fillStyle:'%s'}", ICON_HOVER_COLOR));
		model.setDefaultConnector(connector);
		statusOrders = new ArrayList<>();
	}
	
	public void generateDiagram(List<StatusVo> statuses, List<StatusOrderViewModel> statusOrders) {
		statuses.forEach(status -> this.addStatus(status.getName()));
		
		if (statusOrders == null)
			return;
		
		this.statusOrders = statusOrders;
		for (StatusOrderViewModel statusOrderViewModel : statusOrders) {
			model.connect(createConnection(statusOrderViewModel.getFrom(), statusOrderViewModel.getTo()));
		}
	}
	
	public void addStatus(String status) {
		Element newElement = new Element(status);
		
		EndPoint outPoint = createRectangleEndPoint(EndPointAnchor.BOTTOM);
		outPoint.setSource(true);
		EndPoint inPoint = createDotEndPoint(EndPointAnchor.TOP);
		inPoint.setTarget(true);
		
		newElement.addEndPoint(outPoint);	//0 out
		newElement.addEndPoint(inPoint);	//1 in
		
		model.addElement(newElement);
	}
	
	public void modifyStatus(String oldStatus, String newStatus) {
		for (Element element : model.getElements()) {
			if (element.getData().equals(oldStatus)) {
				element.setData(newStatus);
				break;
			}
		}
		
		for (StatusOrderViewModel statusOrderViewModel : statusOrders) {
			if (statusOrderViewModel.getFrom().equals(oldStatus)) {
				statusOrderViewModel.setFrom(newStatus);
			}
			
			if (statusOrderViewModel.getTo().equals(oldStatus)) {
				statusOrderViewModel.setTo(newStatus);
			}
		}
	}
	
	public void deleteStatus(String status) {
		for (Element element : model.getElements()) {
			if (element.getData().equals(status)) {
				model.removeElement(element);
				statusOrders.removeIf(x -> x.getTo().equals(status) || x.getFrom().equals(status));
				return;
			}
		}
	}

	private Connection createConnection(String from, String to) {
		Element fromElement = model.getElements().stream().filter(x -> x.getData().equals(from)).findFirst().get();
		EndPoint fromEndPoint = fromElement.getEndPoints().get(0);
		Element toElement = model.getElements().stream().filter(x -> x.getData().equals(to)).findFirst().get();
		EndPoint toEndPoint = toElement.getEndPoints().get(1);
		
		Connection conn = new Connection(fromEndPoint, toEndPoint);
		conn.getOverlays().add(new ArrowOverlay(20, 20, 1, 1));
		return conn;
	}

	public void onConnect(ConnectEvent event) {
		String from = (String)event.getSourceElement().getData();
		String to = (String)event.getTargetElement().getData();
		log.debug("Connected from " + from + " to " + to);
		statusOrders.add(StatusOrderViewModel.builder()
				.from(from)
				.to(to)
				.build());
	}

	public void onDisconnect(DisconnectEvent event) {
		String from = (String)event.getSourceElement().getData();
		String to = (String)event.getTargetElement().getData();
		log.debug("Disconnected from " + from + " to " + to);
		statusOrders.removeIf(x -> from.equals(x.getFrom()) && to.equals(x.getTo()) );
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