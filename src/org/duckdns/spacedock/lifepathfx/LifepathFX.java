/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.lifepathfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author ykonoclast
 */
public class LifepathFX extends Application
{

    @Override
    public void start(Stage stage) throws Exception
    {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("Lifepath.fxml"));//TODO voir si on peut pas éviter ce getclass
	Parent root = loader.load();

	Scene scene = new Scene(root);//pas de taille, on utilise un stage fullscreen ensuite

	stage.setTitle("FXML Welcome");//TODO Strings i18n
	stage.setScene(scene);

	//scene.getStylesheets().add(Form.class.getResource("Form.css").toExternalForm());//incantation pour le css TODO pourquoi le class.getressource?
	stage.setFullScreen(true);
	stage.show();

	//LES DEUX LIGNES SUIVANTES SONT CONSERVEES POUR DOCUMENTATION : voici comment accéder au contrôleur (et donc, éventuellement, lui passer une callback)
	//PathController controller = (PathController) loader.getController();
	//controller.turlututu();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
	launch(args);
    }

}
