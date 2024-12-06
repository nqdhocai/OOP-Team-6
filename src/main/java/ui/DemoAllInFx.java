package ui;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DemoAllInFx extends Application {
    protected static String styleSheet =
            "graph {" +
                    "   padding: 60px;" +
                    "}" +
                    "node {" +
                    "   text-size: 20;" +
                    "   text-color: black;" +
                    "   text-background-mode: rounded;" +
                    "   text-background-color: white;" +
                    "}";

    public static void main(String args[]) {
        Application.launch(DemoAllInFx.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Graph graph  = new MultiGraph("mg");

        // Tạo viewer và gán nó vào GUI
        FxViewer viewer = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);

        // Thêm các node và edge
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");

        // Thiết lập thuộc tính đồ thị và chất lượng hiển thị
        graph.setAttribute("ui.antialias");
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.stylesheet", styleSheet);

        // Thiết lập nhãn cho các node
        graph.getNode("A").setAttribute("ui.label", "Node A");
        graph.getNode("B").setAttribute("ui.label", "Node B");
        graph.getNode("C").setAttribute("ui.label", "Node C");

        // Thêm và hiển thị view chính của đồ thị trong JavaFX
        FxViewPanel v = (FxViewPanel) viewer.addDefaultView(false);
        Scene scene = new Scene(v, 800, 600);
        primaryStage.setScene(scene);

        // Hiển thị cửa sổ JavaFX
        primaryStage.show();
    }
}
