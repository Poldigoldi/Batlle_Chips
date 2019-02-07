package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import java.util.List;
import java.util.ArrayList;

public class Main extends Application {

    private Pane root;
    private List <GameObject> bullets = new ArrayList<>();
    private List <GameObject> enemies = new ArrayList<>();

    private GameObject player;

    private Button button;

    private Parent createContent() {
        root = new Pane();
        root.setPrefSize(600, 600);

        player = new Player ();
        player.setVelocity(new Point2D(1, 0));


        addGameObjetc(player, 300, 300);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();

        return root;
    }

    private void addButton (Button buttonName, String content) {
        buttonName = new Button(content);
        buttonName.setLayoutX(280);
        buttonName.setLayoutY(550);
        root.getChildren().add(buttonName);
    }

    private void addBullet (GameObject bullet, double x, double y) {
        bullets.add(bullet);
        addGameObjetc(bullet, x, y);
    }

    private void addEnemy (GameObject enemy, double x, double y) {
        enemies.add(enemy);
        addGameObjetc(enemy, x, y);
    }

    private void addGameObjetc(GameObject object, double x, double y) {
        object.getView().setTranslateX(x);
        object.getView().setTranslateY(y);
        root.getChildren().add(object.getView());
    }

    private void onUpdate() {
        for (GameObject bullet : bullets) {
            for (GameObject enemy : enemies) {
                if (bullet.iscColliding(enemy)) {
                    bullet.setAlive(false);
                    enemy.setAlive(false);

                    root.getChildren().removeAll(bullet.getView(), enemy.getView());
                }
            }
        }
        bullets.removeIf(GameObject::isDead);
        enemies.removeIf(GameObject::isDead);

        bullets.forEach(GameObject::update);
        enemies.forEach(GameObject::update);

        player.update();


        if (Math.random() < 0.02) {
            addEnemy(new Enemy(), Math.random() * root.getPrefWidth(), Math.random() * root.getPrefHeight());
        }
    }

    private static class Player extends GameObject{
        Player() {
            super(new Rectangle(40, 20, Color.BLUE));
        }
    }
    private static class Enemy extends GameObject {
        Enemy() {
            super(new Circle(15, 15, 15, Color.RED));
        }
    }
    private static class Bullet extends GameObject {
        Bullet() {
            super(new Circle(5, 5, 5, Color.GREEN));
        }
    }



    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setScene(new Scene(createContent()));
        primaryStage.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) {
                player.rotateLeft();
            } else if (e.getCode() == KeyCode.RIGHT) {
                player.rotateRight();
            } else if (e.getCode() == KeyCode.SPACE) {
                Bullet bullet = new Bullet();
                bullet.setVelocity(player.getVelocity().normalize().multiply(5));
                addBullet(bullet, player.getView().getTranslateX(), player.getView().getTranslateY());
            }
        });
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }






}
