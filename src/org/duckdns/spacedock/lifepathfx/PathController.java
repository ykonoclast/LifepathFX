/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.lifepathfx;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.duckdns.spacedock.liblifepath.PathNavigator;

/**
 *
 * classe servant à la fois de DAO vers liblifepath et controlant les actions
 * dans l'UI.
 *
 * @author ykonoclast
 */
public class PathController implements Initializable
{

    /**
     * le principal élément métier, issu de liblifepath
     */
    private final PathNavigator m_navigator = new PathNavigator();

    @FXML
    private VBox fx_vBox;

    @FXML
    private TextFlow fx_tFlow;

    public PathController() throws FileNotFoundException
    {//TODO faire quelque chose si l'éxception est levée...
    }

    /**
     * callback du bouton Back de l'interface : retour au menu principal si on
     * est au node initial et retour arrière dans l'arbre des nodes sinon
     *
     * @param event non exploité
     */
    @FXML
    protected void rollback(ActionEvent event)
    {
	if (m_navigator.canRollback())
	{//on n'est pas dans le node initial : on fait donc un retour arrière
	    removeDisplayedButtons();//on retire les boutons actuellement affichés

	    m_navigator.rollback();//après la suppression des boutons, sinon on cherchera à enlever ceux du choix d'avant (déjà retirés)

	    ObservableList<Node> tFlowChildren = fx_tFlow.getChildren();
	    int totalItemsNumber = tFlowChildren.size();
	    tFlowChildren.remove(totalItemsNumber - 1);//on retire le texte actuellement affiché

	    displayCurrentChoiceButtons();//on replace les boutons correspondant au choix précédent
	}
	//TODO SINON retour au menu principal, rien pour l'instant
    }

    /**
     * appelé automatiquement par le framework, attention la Scene n'est pas
     * encore disponible pour l'instant, ne pas y faire appel ici où dans les
     * méthodes appelées
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
	displayCurrentChoice();//on amorce le système en affichant les tous premiers éléments
    }

    /**
     * On avance dans l'arbre des choix en adaptant l'affichage
     *
     * @param p_id
     */
    public void decide(String p_id)
    {
	if (p_id != null)
	{
	    removeDisplayedButtons();
	    m_navigator.decide(p_id);//après suppression des boutons, sinon il tentera de supprimer ceux d'après, le nombre pourrait ne pas correspondre
	    displayCurrentChoice();
	}
	else
	{
	    throw new NullPointerException("id null");//TODO i18n, strings
	}
    }

    /**
     * supprime les boutons correspondant au choix actuel dans le PathNavigator,
     * si on appelle cette méthode au mauvais moment, elle cherchera à supprimer
     * un nombre de boutons pouvant ne pas correspondre au besoin et
     * l'application ne pourra pas s'en remettre
     */
    private void removeDisplayedButtons()
    {
	int toDelete = m_navigator.getCurrentChoice().decisionsPossibles.size();
	ObservableList<Node> mainBoxChildren = fx_vBox.getChildren();
	int totalItemsNumber = mainBoxChildren.size();

	mainBoxChildren.remove(totalItemsNumber - toDelete, totalItemsNumber);
    }

    /**
     * affiche les boutons correspondants au choix actuel dans le PathNavigator
     * si on appelle cette méthode au mauvais moment, elle cherchera à supprimer
     * un nombre de boutons pouvant ne pas correspondre au besoin et
     * l'application ne pourra pas s'en remettre
     */
    private void displayCurrentChoiceButtons()
    {
	ObservableList<Node> mainBoxChildren = fx_vBox.getChildren();

	for (Map.Entry<String, String> entry : m_navigator.getCurrentChoice().decisionsPossibles.entrySet())
	{//pour chaque entrée dans les choix possibles du node actuel
	    Button choiceButton = new Button(entry.getValue());//on place le libellé du choix
	    choiceButton.setUserData(entry.getKey());//on place l'id du choix

	    //listener sur le nouveau bouton
	    choiceButton.setOnAction(e ->
	    {
		Button button = (Button) e.getSource();
		decide((String) button.getUserData());//on appelle la méthode avec l'id du choix placée en userdata
	    });

	    mainBoxChildren.add(choiceButton);//ajout effectif du bouton
	}
    }

    /**
     * affiche le texte et les boutons du choix actuel, dans le PathNavigator si
     * on appelle cette méthode au mauvais moment, elle cherchera à supprimer un
     * nombre de boutons pouvant ne pas correspondre au besoin et l'application
     * ne pourra pas s'en remettre
     */
    private void displayCurrentChoice()
    {
	Text text = new Text(m_navigator.getCurrentChoice().desc + "\n");
	//text.wrappingWidthProperty().bind(bigPane.widthProperty().subtract(15));

	fx_tFlow.getChildren().add(text);

	displayCurrentChoiceButtons();
    }
}
