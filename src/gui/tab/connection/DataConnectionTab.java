package gui.tab.connection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import gui.tab.AbstractTab;
import gui.tab.DBComboBox;
import gui.tab.DBNode;
import gui.utils.GUIUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import model.ConcourseManager;
import model.CrimeRecord;
import util.DBException;
import util.TextFileReader;

/**
 * The tab allowing crimes to be "connected", or linked. Allows for both unidirectional or bidirectional linkages.
 * The tab is automatically populated, so this class need not be edited if further nodes need to be added.
 * 
 * @author adityasrinivasan
 *
 */
public class DataConnectionTab extends AbstractTab {
	
	/**
	 * Constants
	 */
	private static final String TAB_NAME = "Manage Connections";
	private static final String NODES_FILE_NAME = "data_connection_nodes.txt";
	private static final String UNI_LINK_LABEL = "Make Unidirectional Link";
	private static final String UNI_LINK_ID = "LinkUnidirectional";
	private static final String BI_LINK_LABEL = "Make Bidirectional Link";
	private static final String BI_LINK_ID = "LinkBidirectional";
	private static final String DELIMITER = "\n";
	private static final String PACKAGE = "gui.tab.connection.nodes.%s";

	
	private List<DBNode> inputs;
	
	public DataConnectionTab(EventHandler<ActionEvent> buttonEvent) {
		super(buttonEvent);
		this.establishTab(TAB_NAME);
	}

	/**
	 * Populates the tab with nodes.
	 */
	@Override
	protected void populate() {
		pane = new VBox();
		pane.getChildren().add(controls());
		pane.getChildren().add(GUIUtils.makeRow(GUIUtils.makeButton(UNI_LINK_LABEL, UNI_LINK_ID, this.buttonEvent),
												GUIUtils.makeButton(BI_LINK_LABEL, BI_LINK_ID, this.buttonEvent)));
	}
	
	/**
	 * Reads in a text file of class names representing nodes, and adds each to the view. This process
	 * is completely automated, so that adding new nodes requires simply creating a new class and adding
	 * its name to a text file.
	 * @return
	 */
	private VBox controls() {
		VBox column = GUIUtils.makeColumn();
		inputs = new ArrayList<>();
		TextFileReader.read(NODES_FILE_NAME, DELIMITER).stream().forEach(a -> {
			Class<?> clazz;
			try {
				clazz = Class.forName(String.format(PACKAGE, a));
				DBNode node = (DBNode) clazz.getConstructor().newInstance();
				column.getChildren().add((Node) node);
				inputs.add(node);
			} catch(Exception e) {
				throw new DBException(e.getMessage());
			}
		});
		return column;
	}

	/**
	 * Returns the nodes of this tab.
	 */
	@Override
	public Collection<?> getInputs() {
		return this.inputs;
	}

	/**
	 * Reads the database and refreshes the comboboxes to update their values.
	 */
	@SuppressWarnings("unchecked")
	public void repopulateBoxes() {
		inputs.stream().forEach(a -> {
			((DBComboBox<CrimeRecord>) a).populateCB(ConcourseManager.getInstance().getRecords());
		});
	}

}
