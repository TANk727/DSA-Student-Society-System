
package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.*;

// === Custom DSA Structures ===
class Node<T> {
    T data;
    Node<T> next;
    Node(T data) { this.data = data; }
}

class LinkedListDS<T> {
    Node<T> head;
    void add(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.next = head;
        head = newNode;
    }
    void delete(String name) {
        Node<T> curr = head, prev = null;
        while (curr != null) {
            if (curr.data.toString().equals(name)) {
                if (prev == null) head = curr.next;
                else prev.next = curr.next;
                return;
            }
            prev = curr;
            curr = curr.next;
        }
    }
    String[] getAllNames() {
        ArrayList<String> names = new ArrayList<>();
        Node<T> temp = head;
        while (temp != null) {
            names.add(temp.data.toString());
            temp = temp.next;
        }
        return names.toArray(new String[0]);
    }
}

class StackDS<T> {
    Node<T> top;
    void push(T data) {
        Node<T> node = new Node<>(data);
        node.next = top;
        top = node;
    }
    T pop() {
        if (top == null) return null;
        T data = top.data;
        top = top.next;
        return data;
    }
    ArrayList<T> toList() {
        ArrayList<T> list = new ArrayList<>();
        Node<T> curr = top;
        while (curr != null) {
            list.add(curr.data);
            curr = curr.next;
        }
        return list;
    }
}

class QueueDS<T> {
    Node<T> front, rear;
    void enqueue(T data) {
        Node<T> node = new Node<>(data);
        if (rear != null) rear.next = node;
        rear = node;
        if (front == null) front = node;
    }
    ArrayList<T> toList() {
        ArrayList<T> list = new ArrayList<>();
        Node<T> curr = front;
        while (curr != null) {
            list.add(curr.data);
            curr = curr.next;
        }
        return list;
    }
}

// === Main Application ===
public class ManagementSystem extends Application {

    // Hardcoded Admin User
    private final String adminUsername = "admin";
    private final String adminPassword = "admin";

    // DSA structures
    LinkedListDS<String> societies = new LinkedListDS<>();
    LinkedListDS<String> events = new LinkedListDS<>();
    StackDS<String> notifications = new StackDS<>();
    QueueDS<String> feedbackQueue = new QueueDS<>();
    Map<String, Integer> reportData = new HashMap<>();

    private StackPane root = new StackPane();

    private Button createStyledButton(String iconPath, String text) {
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        icon.setFitWidth(40);
        icon.setFitHeight(40);
        Label label = new Label(text);
        label.setTextFill(Color.BLACK);
        VBox box = new VBox(icon, label);
        box.setAlignment(Pos.CENTER);
        Button btn = new Button();
        btn.setGraphic(box);
        btn.setStyle("-fx-background-color: transparent;");
        return btn;
    }

    private void switchScene(Pane pane) {
        root.getChildren().clear();
        root.getChildren().add(pane);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("DSA Society Management");

        VBox main = new VBox(20);
        main.setAlignment(Pos.CENTER);
        Label title = new Label("Welcome to Society Management System");
        title.setFont(new Font(24));

        Button adminBtn = createStyledButton("/icons/admin.png", "Admin Login");
        Button studentBtn = createStyledButton("/icons/student.png", "Student Access");
        main.getChildren().addAll(title, adminBtn, studentBtn);
        switchScene(main);

        // Admin Panel
        VBox adminPanel = new VBox(15);
        adminPanel.setAlignment(Pos.CENTER);
        Button addSoc = createStyledButton("/icons/society.png", "Add Society");
        Button delSoc = createStyledButton("/icons/del_society.png", "Delete Society");
        Button addEvt = createStyledButton("/icons/event.png", "Add Event");
        Button delEvt = createStyledButton("/icons/del_event.png", "Delete Event");
        Button reportBtn = createStyledButton("/icons/report.png", "View Report");
        Button backAdmin = createStyledButton("/icons/back.png", "Back");
        adminPanel.getChildren().addAll(addSoc, delSoc, addEvt, delEvt, reportBtn, backAdmin);

        adminBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("Enter admin password:");
            dialog.showAndWait().ifPresent(input -> {
                if (input.equals(adminPassword)) switchScene(adminPanel);
                else new Alert(Alert.AlertType.ERROR, "Incorrect password!").showAndWait();
            });
        });

        addSoc.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("Enter society name");
            dialog.showAndWait().ifPresent(name -> {
                societies.add(name);
                reportData.put(name, reportData.getOrDefault(name, 0) + 1);
            });
        });

        delSoc.setOnAction(e -> {
            ChoiceDialog<String> dialog = new ChoiceDialog<>(null, societies.getAllNames());
            dialog.setHeaderText("Delete society");
            dialog.showAndWait().ifPresent(societies::delete);
        });

        addEvt.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("Enter event title");
            dialog.showAndWait().ifPresent(titlee -> {
                events.add(titlee);
                reportData.put(titlee, reportData.getOrDefault(titlee, 0) + 1);
            });
        });

        delEvt.setOnAction(e -> {
            ChoiceDialog<String> dialog = new ChoiceDialog<>(null, events.getAllNames());
            dialog.setHeaderText("Delete event");
            dialog.showAndWait().ifPresent(events::delete);
        });

        reportBtn.setOnAction(e -> {
            PieChart chart = new PieChart();
            for (var e1 : reportData.entrySet()) {
                chart.getData().add(new PieChart.Data(e1.getKey(), e1.getValue()));
            }
            VBox v = new VBox(chart);
            Stage reportStage = new Stage();
            reportStage.setScene(new Scene(v, 400, 300));
            reportStage.show();
        });

        backAdmin.setOnAction(e -> switchScene(main));

        // Student Panel
        VBox studentPanel = new VBox(15);
        studentPanel.setAlignment(Pos.CENTER);
        Button joinSoc = createStyledButton("/icons/join_s.png", "Join Society");
        Button joinEvt = createStyledButton("/icons/join_e.png", "Join Event");
        Button feedBtn = createStyledButton("/icons/feedback.png", "Give Feedback");
        Button notifyBtn = createStyledButton("/icons/notification.png", "View Notifications");
        Button backStu = createStyledButton("/icons/back.png", "Back");
        studentPanel.getChildren().addAll(joinSoc, joinEvt, feedBtn, notifyBtn, backStu);

        studentBtn.setOnAction(e -> switchScene(studentPanel));

        joinSoc.setOnAction(e -> {
            ChoiceDialog<String> dialog = new ChoiceDialog<>(null, societies.getAllNames());
            dialog.setHeaderText("Join a society");
            dialog.showAndWait().ifPresent(sel -> reportData.put(sel, reportData.getOrDefault(sel, 0) + 1));
        });

        joinEvt.setOnAction(e -> {
            ChoiceDialog<String> dialog = new ChoiceDialog<>(null, events.getAllNames());
            dialog.setHeaderText("Join an event");
            dialog.showAndWait().ifPresent(sel -> reportData.put(sel, reportData.getOrDefault(sel, 0) + 1));
        });

        feedBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("Enter feedback");
            dialog.showAndWait().ifPresent(feedbackQueue::enqueue);
        });

        notifyBtn.setOnAction(e -> {
            notifications.push("Event reminder: tomorrow at 10 AM");
            Alert alert = new Alert(Alert.AlertType.INFORMATION, String.join("\n", notifications.toList().stream().map(Object::toString).toList()));
            alert.setHeaderText("Notifications");
            alert.showAndWait();
        });

        backStu.setOnAction(e -> switchScene(main));

        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
